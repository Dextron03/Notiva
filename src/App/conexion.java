package App;

import Modules.Email;
import Modules.Sesion;
import Modules.TokenGenerator;
import Modules.User;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/notiva";
    private static final String USER = "root";
    private static final String PASSWORD = "033004";

    public static Connection obtenerConexion() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println(" Error al conectar: " + e.getMessage());
            return null;
        }
    }

    public static void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static User loginAndGetUser(String email, String password) {
        String query = "SELECT id_usuario, username, correo, password FROM usuario WHERE correo = ? AND password = ?";

        try (Connection conn = obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id_usuario");
                String username = rs.getString("username");
                String correo = rs.getString("correo");
                String pass = rs.getString("password");

                Sesion sesion = new Sesion();
                sesion.login();

                return new User(id, username, correo, pass, sesion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Obtener ruta del avatar por usuario
    public static String obtenerRutaAvatarPorUsuario(int idUsuario) {
        String ruta = null;
        String sql = "SELECT a.direccion_url FROM usuario u JOIN avatar a ON u.id_avatar = a.id_avatar WHERE u.id_usuario = ?";

        try (Connection conn = obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ruta = rs.getString("direccion_url");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ruta;
    }

    // Actualizar avatar del usuario
    public static boolean actualizarAvatarUsuario(int idUsuario, int idAvatar) {
        String sql = "UPDATE usuario SET id_avatar = ? WHERE id_usuario = ?";
        try (Connection conn = obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAvatar);
            stmt.setInt(2, idUsuario);

            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean verificarUsuarioYEnviarCorreo(String email, String senderEmail, String senderPassword) {
        String query = "SELECT id_usuario FROM usuario WHERE correo = ?";
        try (Connection conn = obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int idUsuario = rs.getInt("id_usuario");
                String username = null;
                String getUsernameQuery = "SELECT username FROM usuario WHERE id_usuario = ?";
                try (PreparedStatement usernameStmt = conn.prepareStatement(getUsernameQuery)) {
                    usernameStmt.setInt(1, idUsuario);
                    ResultSet usernameRs = usernameStmt.executeQuery();
                    if (usernameRs.next()) {
                        username = usernameRs.getString("username");
                    }
                }

                String tokenString = TokenGenerator.generarToken();
                int token = Integer.parseInt(tokenString);

                // Insert token into token table
                String insertTokenQuery = "INSERT INTO token (token, fecha_emision, fecha_expiracion, estado) VALUES (?, ?, ?, ?)";
                long now = System.currentTimeMillis();
                Timestamp emision = new Timestamp(now);
                Timestamp expiracion = new Timestamp(now + 600000); // 10 minutes

                try (PreparedStatement tokenStmt = conn.prepareStatement(insertTokenQuery, Statement.RETURN_GENERATED_KEYS)) {
                    tokenStmt.setInt(1, token);
                    tokenStmt.setTimestamp(2, emision);
                    tokenStmt.setTimestamp(3, expiracion);
                    tokenStmt.setString(4, "emitido");
                    tokenStmt.executeUpdate();

                    ResultSet generatedKeys = tokenStmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int idToken = generatedKeys.getInt(1);

                        // Link user and token
                        String insertUsuarioTokenQuery = "INSERT INTO usuario_token (id_usuario, id_token) VALUES (?, ?)";
                        try (PreparedStatement usuarioTokenStmt = conn.prepareStatement(insertUsuarioTokenQuery)) {
                            usuarioTokenStmt.setInt(1, idUsuario);
                            usuarioTokenStmt.setInt(2, idToken);
                            usuarioTokenStmt.executeUpdate();
                        }

                        // Send email
                        Email emailService = new Email("http://localhost:8000");
                        Map<String, String> context = new HashMap<>();
                        context.put("token", tokenString);
                        if (username != null) {
                            context.put("username", username);
                        }

                        try {
                            emailService.sendTemplateEmail(senderEmail, email, senderPassword, "Código de Verificación", "templates/emails", "verificate_code.html", context);
                            return true;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
