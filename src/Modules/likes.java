package Modules;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import App.conexion;

public class likes {

    public static boolean usuarioYaDioLike(int idTarea, int idUsuario) {
        String sql = "SELECT COUNT(*) FROM likes WHERE id_tarea = ? AND id_usuario = ?";
        try (Connection con = conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTarea);
            ps.setInt(2, idUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static void registrarLike(int idTarea, int idUsuario) {
        String sql = "INSERT INTO likes (id_tarea, fecha, id_usuario) VALUES (?, NOW(), ?)";
        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTarea);
            ps.setInt(2, idUsuario);
            ps.executeUpdate();
            System.out.println("Like registrado para tarea " + idTarea);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static int obtenerLikes(int idTarea) {
        String sql = "SELECT COUNT(*) FROM likes WHERE id_tarea = ?";
        try (Connection con = conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTarea);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }
}
