package Modules;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import App.conexion;

public class publicacionDatos {
    public int idTarea;
    public String titulo;
    public String descripcion;
    public String categoria;
    public String fechaCreacion;

    public publicacionDatos(int idTarea, String titulo, String descripcion, String categoria, String fechaCreacion) {
        this.idTarea = idTarea;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.fechaCreacion = fechaCreacion;
    }

    public static List<publicacionDatos> obtenerPublicaciones() {
        List<publicacionDatos> publicaciones = new ArrayList<>();

        String sql = """
            SELECT t.id_tarea, t.titulo, t.descripcion, c.categoria AS nombre_categoria, t.fecha_creacion FROM tarea t 
            INNER JOIN categoria c ON t.categoria = c.id_categoria WHERE t.visibilidad = 1 ORDER BY t.fecha_creacion DESC
        		""";

        try (Connection conn = conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                publicaciones.add(new publicacionDatos(
                    rs.getInt("id_tarea"),
                    rs.getString("titulo"),
                    rs.getString("descripcion"),
                    rs.getString("nombre_categoria"),
                    rs.getString("fecha_creacion")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return publicaciones;
    }    
} // final de la clase