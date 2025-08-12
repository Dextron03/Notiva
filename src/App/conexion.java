package App;

import Modules.Sesion;
import Modules.User;

import java.sql.*;

public class conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/notiva";
    private static final String USER = "root";
    private static final String PASSWORD = "mr_garcia";

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

}