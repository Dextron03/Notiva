package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import App.conexion;

public class ChangePassword {

    public JFrame frame;
    private JTextField txtNewPassword;
    private JTextField txtNewPasswordConfirm;

    private int idUsuario;
    private int idToken;

    public ChangePassword(int idUsuario, int idToken) {
        this.idUsuario = idUsuario;
        this.idToken = idToken;
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("Cambia tu Contraseña");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 28));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(69, 11, 308, 54);
        frame.getContentPane().add(lblNewLabel);

        JLabel lblNewPassword = new JLabel("Nueva Contraseña:");
        lblNewPassword.setBounds(69, 76, 150, 14);
        frame.getContentPane().add(lblNewPassword);

        txtNewPassword = new JTextField();
        txtNewPassword.setBounds(69, 96, 308, 31);
        frame.getContentPane().add(txtNewPassword);
        txtNewPassword.setColumns(10);

        JLabel lblConfirmPassword = new JLabel("Confirmar Nueva Contraseña:");
        lblConfirmPassword.setBounds(69, 149, 200, 14);
        frame.getContentPane().add(lblConfirmPassword);

        txtNewPasswordConfirm = new JTextField();
        txtNewPasswordConfirm.setColumns(10);
        txtNewPasswordConfirm.setBounds(69, 169, 308, 31);
        frame.getContentPane().add(txtNewPasswordConfirm);

        JButton btnConfirmar = new JButton("Confirmar");
        btnConfirmar.addActionListener((ActionEvent e) -> cambiarContrasena());
        btnConfirmar.setBounds(175, 229, 100, 23);
        frame.getContentPane().add(btnConfirmar);
    }

    private void cambiarContrasena() {
        String nuevaPass = txtNewPassword.getText().trim();
        String confirmarPass = txtNewPasswordConfirm.getText().trim();

        if (nuevaPass.isEmpty() || confirmarPass.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Complete todos los campos.");
            return;
        }

        if (!nuevaPass.equals(confirmarPass)) {
            JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden.");
            return;
        }

        try (Connection conn = conexion.obtenerConexion()) {
            // Actualizar contraseña
            String updatePass = "UPDATE usuario SET password = ? WHERE id_usuario = ?";
            try (PreparedStatement stmt1 = conn.prepareStatement(updatePass)) {
                stmt1.setString(1, nuevaPass); // Aquí podrías aplicar hash si es necesario
                stmt1.setInt(2, idUsuario);
                stmt1.executeUpdate();
            }

            // Marcar token como usado
            String updateToken = "UPDATE token SET estado = 'usado' WHERE id_token = ?";
            try (PreparedStatement stmt2 = conn.prepareStatement(updateToken)) {
                stmt2.setInt(1, idToken);
                stmt2.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Contraseña actualizada correctamente.");
            frame.dispose();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar la contraseña.");
        }
    }
}
