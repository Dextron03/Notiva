package Modules;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import App.conexion;

public class comentario {

    public static class ComentarioDatos {
        public String usuario;
        public String comentario;
        public Timestamp fecha;

        public ComentarioDatos(String usuario, String comentario, Timestamp fecha) {
            this.usuario = usuario;
            this.comentario = comentario;
            this.fecha = fecha;
        }
    }

    public static List<ComentarioDatos> obtenerComentarios(int idTarea) {
        List<ComentarioDatos> lista = new ArrayList<>();
        try (Connection conn = conexion.obtenerConexion()) {
            String sql = "SELECT c.comentario, c.fecha, u.username " +
                         "FROM comentario c " +
                         "JOIN usuario u ON c.id_usuario = u.id_usuario " +
                         "WHERE c.id_tarea = ? " +
                         "ORDER BY c.fecha ASC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idTarea);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new ComentarioDatos(
                    rs.getString("username"),
                    rs.getString("comentario"),
                    rs.getTimestamp("fecha")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static void agregarComentario(int idTarea, String texto, int user) {
        try (Connection con = conexion.obtenerConexion()) {
            String sql = "INSERT INTO comentario (id_tarea, comentario, fecha, id_usuario) VALUES (?, ?, NOW(), ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idTarea);
            ps.setString(2, texto);
            ps.setInt(3, user);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }  
}
