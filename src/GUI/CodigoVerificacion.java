package GUI;

import java.awt.EventQueue;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

import App.conexion;

public class CodigoVerificacion {

    public JFrame frame;
    private JTextField txtVerificationCode;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CodigoVerificacion window = new CodigoVerificacion();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public CodigoVerificacion() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("Escribe el código de verificación enviado a tu correo:");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(50, 30, 340, 45);
        frame.getContentPane().add(lblNewLabel);

        txtVerificationCode = new JTextField();
        txtVerificationCode.setBounds(94, 99, 246, 26);
        frame.getContentPane().add(txtVerificationCode);
        txtVerificationCode.setColumns(10);

        JButton btnEnviar = new JButton("Enviar");
        btnEnviar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String code = txtVerificationCode.getText().trim();

                if (code.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese un token.");
                    return;
                }

                String query = """
                        SELECT u.id_usuario, t.id_token, t.estado, t.fecha_expiracion
                        FROM token t
                        INNER JOIN usuario_token ut ON t.id_token = ut.id_token
                        INNER JOIN usuario u ON ut.id_usuario = u.id_usuario
                        WHERE t.token = ?
                        """;

                try (Connection conn = conexion.obtenerConexion();
                     PreparedStatement stmt = conn.prepareStatement(query)) {

                    stmt.setString(1, code);

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            int idUsuario = rs.getInt("id_usuario");
                            int idToken = rs.getInt("id_token");
                            String estado = rs.getString("estado");
                            Timestamp fechaExp = rs.getTimestamp("fecha_expiracion");

                            // Validar estado
                            if ("expirado".equalsIgnoreCase(estado)) {
                                JOptionPane.showMessageDialog(null, "Su token ha expirado.");
                                return;
                            }

                            // Validar fecha de expiración
                            if (fechaExp != null && fechaExp.before(new java.util.Date())) {
                                JOptionPane.showMessageDialog(null, "El token ha caducado por tiempo.");
                                return;
                            }

                            JOptionPane.showMessageDialog(null, "Token válido. Puede continuar.");
                            ChangePassword chpass = new ChangePassword(idUsuario, idToken);
                            chpass.frame.setVisible(true);
                            frame.dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "El token ingresado no coincide.");
                        }
                    }

                } catch (SQLException error) {
                    error.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Ocurrió un error al validar el token.");
                }
            }
        });

        btnEnviar.setBounds(174, 136, 89, 23);
        frame.getContentPane().add(btnEnviar);
    }
}
