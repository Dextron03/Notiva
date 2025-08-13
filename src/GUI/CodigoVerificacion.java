package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import App.conexion;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class CodigoVerificacion {

	public JFrame frame;
	private JTextField txtVerificationCode;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CodigoVerificacion window = new CodigoVerificacion();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CodigoVerificacion() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		JLabel lblNewLabel = new JLabel("Escribe el codigo de verificacion enviado tu correo:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(76, 52, 270, 45);
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

		        String query = "SELECT token, estado, fecha_expiracion FROM token WHERE token = ?";

		        try (Connection conn = conexion.obtenerConexion();
		             PreparedStatement stmt = conn.prepareStatement(query)) {

		            stmt.setString(1, code);

		            try (ResultSet rs = stmt.executeQuery()) {
		                if (rs.next()) {
		                    String estado = rs.getString("estado");
		                    Timestamp fechaExp = rs.getTimestamp("fecha_expiracion");

		                    // Validar estado
		                    if (estado != null && estado.equalsIgnoreCase("expirado")) {
		                        JOptionPane.showMessageDialog(null, "Su token ha expirado.");
		                        return;
		                    }

		                    // Validar fecha de expiración
		                    if (fechaExp != null && fechaExp.before(new java.util.Date())) {
		                        JOptionPane.showMessageDialog(null, "El token ha caducado por tiempo.");
		                        return;
		                    }

		                    JOptionPane.showMessageDialog(null, "Token válido. Puede continuar.");
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
