package GUI.notas;

import App.conexion;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import Modules.User;

public class NuevaNota extends notasBase {
    private JButton btnGuardar;
    private notasFeedInicio ventanaPrincipal;
    private User idUsuario;

    public NuevaNota(notasFeedInicio ventanaPrincipal) {
        super();
        setTitle("Nueva Nota");
        this.idUsuario = User.getCurrentUser();
        this.ventanaPrincipal = ventanaPrincipal;
    }

    @Override
    protected void configurarComponentes() {
        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(20, 311, 100, 30);
        add(btnGuardar);

        btnGuardar.addActionListener((e) -> {
            String titulo = txtTitulo.getText();
            String contenido = textArea.getText();
            if (!titulo.isEmpty() && !contenido.isEmpty()) {
                try (Connection conn = conexion.obtenerConexion()) {
                    String sql = "INSERT INTO notas (titulo, contenido, fecha_creacion, propietario) VALUES (?, ?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, titulo);
                    stmt.setString(2, contenido);
                    LocalDateTime ahora = LocalDateTime.now();
                    stmt.setTimestamp(3, Timestamp.valueOf(ahora));
                    stmt.setInt(4, idUsuario.getCurrentUserId());
                    stmt.executeUpdate();

                    // Pasa el título y la fecha como String al método
                    ventanaPrincipal.agregarNotaAlFeed(titulo, ahora.toString());

                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al guardar la nota.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Completa ambos campos.");
            }
        });
    }
}