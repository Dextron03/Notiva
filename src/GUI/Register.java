package GUI;

import App.conexion;
import Modules.Sesion;
import Modules.User;

import java.awt.EventQueue;
import javax.swing.*;
import java.awt.Font;
import java.awt.Image;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class Register {

    JFrame frame;
    private JTextField txtUserName;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfimPassword;

    public Register() {
        initialize();
    }

    private void goToLogin () {
        Login loginWindow = new Login();
        loginWindow.frame.setVisible(true);
        frame.dispose();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBackground(new Color(64, 128, 128));
        frame.getContentPane().setBackground(new Color(255, 255, 255));
        frame.setBounds(100, 100, 363, 557);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        ImageIcon iconoM = new ImageIcon(getClass().getResource("/iconos/logoS.jpg"));
        Image ima = iconoM.getImage().getScaledInstance(80, 70, Image.SCALE_SMOOTH);
        ImageIcon iconoA = new ImageIcon(ima);

        JLabel lblNewLabel = new JLabel("",iconoA,JLabel.LEFT);
        lblNewLabel.setBounds(114, 11, 99, 50);
        frame.getContentPane().add(lblNewLabel);
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 21));

        JLabel lblNewLabel_1 = new JLabel("Nombre de usuario");
        lblNewLabel_1.setBounds(46, 91, 122, 13);
        frame.getContentPane().add(lblNewLabel_1);

        txtUserName = new JTextField();
        txtUserName.setBounds(46, 112, 245, 27);
        frame.getContentPane().add(txtUserName);
        txtUserName.setColumns(10);

        JLabel lblNewLabel_1_1 = new JLabel("Correo");
        lblNewLabel_1_1.setBounds(46, 159, 122, 13);
        frame.getContentPane().add(lblNewLabel_1_1);

        txtEmail = new JTextField();
        txtEmail.setBounds(46, 180, 245, 27);
        frame.getContentPane().add(txtEmail);
        txtEmail.setColumns(10);

        JLabel lblNewLabel_1_2 = new JLabel("Contraseña");
        lblNewLabel_1_2.setBounds(46, 237, 122, 13);
        frame.getContentPane().add(lblNewLabel_1_2);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(46, 260, 245, 25);
        frame.getContentPane().add(txtPassword);

        JLabel lblNewLabel_1_2_1 = new JLabel("Confirmar contraseña");
        lblNewLabel_1_2_1.setBounds(46, 315, 146, 13);
        frame.getContentPane().add(lblNewLabel_1_2_1);

        txtConfimPassword = new JPasswordField();
        txtConfimPassword.setBounds(46, 338, 245, 25);
        frame.getContentPane().add(txtConfimPassword);

        JButton btnNewButton = new JButton("Registrarse");

        btnNewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                String username = txtUserName.getText().trim();
                String email = txtEmail.getText().trim();
                String password = new String(txtPassword.getPassword());
                String confirmPassword = new String(txtConfimPassword.getPassword());

                // 1. Validar campos vacíos
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 2. Validar formato de correo
                if (!email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
                    JOptionPane.showMessageDialog(frame, "Por favor, ingresa un correo válido", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 3. Validar longitud de contraseña
                if (password.length() < 8) {
                    JOptionPane.showMessageDialog(frame, "La contraseña debe tener al menos 8 caracteres", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 4. Validar que las contraseñas coincidan
                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(frame, "Las contraseñas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Connection conexion = App.conexion.obtenerConexion();
                if (conexion == null) {
                    JOptionPane.showMessageDialog(frame, "No se pudo conectar a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // 5. Verificar si el correo ya existe
                    String checkQuery = "SELECT COUNT(*) FROM usuario WHERE correo = ?";
                    try (PreparedStatement checkStmt = conexion.prepareStatement(checkQuery)) {
                        checkStmt.setString(1, email);
                        try (ResultSet rs = checkStmt.executeQuery()) {
                            if (rs.next() && rs.getInt(1) > 0) {
                                JOptionPane.showMessageDialog(frame, "El correo ya está registrado", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                    }

                    // 6. Registrar usuario
                    Sesion sesion = new Sesion();
                    User user = new User(username, email, password, sesion);

                    String query = "INSERT INTO usuario (username, correo, password, ultimo_acceso, id_avatar) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement stmt = conexion.prepareStatement(query)) {
                        stmt.setString(1, user.getUserName());
                        stmt.setString(2, user.getEmail());
                        stmt.setString(3, user.getPassword()); // aquí podrías usar BCrypt
                        stmt.setTimestamp(4, java.sql.Timestamp.valueOf(user.getLastSesion().getDate()));
                        stmt.setString(5, null);

                        int filas = stmt.executeUpdate();
                        if (filas > 0) {
                            JOptionPane.showMessageDialog(frame, "Usuario registrado correctamente", "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
                            goToLogin();
                        }
                    }

                } catch (SQLException error) {
                    JOptionPane.showMessageDialog(frame, "Error al registrar usuario:\n" + error.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
                    error.printStackTrace();
                } finally {
                    App.conexion.cerrarConexion(conexion);
                }
            }
        });

        btnNewButton.setBounds(106, 396, 107, 21);
        frame.getContentPane().add(btnNewButton);
        btnNewButton.setBackground(new Color(192, 192, 192));

        JLabel lblNewLabel_2 = new JLabel("¿Ya tienes cuenta?");
        lblNewLabel_2.setBounds(62, 449, 107, 13);
        frame.getContentPane().add(lblNewLabel_2);

        JLabel lblNewLabel_3 = new JLabel("Acceder al Login");
        lblNewLabel_3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                goToLogin();
            }
        });
        lblNewLabel_3.setBounds(180, 449, 97, 13);
        frame.getContentPane().add(lblNewLabel_3);
    }
}
