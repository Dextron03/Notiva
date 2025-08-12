//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package GUI;

import App.conexion;
import Modules.User;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends JFrame {
    public JFrame frame;
    private JTextField txtEmail;
    private JPasswordField txtPassword;

    public Login() {
        this.initialize();
    }

    private void initialize() {
        this.frame = new JFrame();
        this.frame.getContentPane().setBackground(new Color(255, 255, 255));
        this.frame.setBounds(100, 100, 842, 524);
        this.frame.setDefaultCloseOperation(3);
        this.frame.getContentPane().setLayout((LayoutManager)null);
        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255));
        panel.setBounds(32, 31, 359, 424);
        this.frame.getContentPane().add(panel);
        panel.setLayout((LayoutManager)null);
        JLabel lblNewLabel_2 = new JLabel("Bienvenidos a Notiva");
        lblNewLabel_2.setHorizontalAlignment(0);
        lblNewLabel_2.setFont(new Font("Calibri", 3, 30));
        lblNewLabel_2.setBounds(0, 25, 339, 50);
        panel.add(lblNewLabel_2);
        ImageIcon icono1 = new ImageIcon(this.getClass().getResource("/iconos/logoS.jpg"));
        Image imageH = icono1.getImage().getScaledInstance(270, 235, 4);
        ImageIcon iconoS = new ImageIcon(imageH);
        JLabel lblNewLabel_3 = new JLabel("", iconoS, 2);
        lblNewLabel_3.setBounds(41, 86, 273, 211);
        panel.add(lblNewLabel_3);
        JLabel lblMensaje = new JLabel("<html><div style='text-align:center;'> Tu espacio digital para organizar tus tareas/actividades y colaborar con tus compañeros.</div></html>");
        lblMensaje.setForeground(new Color(0, 0, 0));
        lblMensaje.setFont(new Font("Calibri", 0, 16));
        lblMensaje.setBounds(10, 236, 319, 200);
        panel.add(lblMensaje);
        JPanel panel_1 = new JPanel();
        panel_1.setBackground(new Color(255, 255, 255));
        panel_1.setBounds(401, 31, 394, 424);
        this.frame.getContentPane().add(panel_1);
        panel_1.setLayout((LayoutManager)null);
        JLabel lblNewLabel = new JLabel("Login");
        lblNewLabel.setFont(new Font("Times New Roman", 0, 25));
        lblNewLabel.setBounds(147, 11, 98, 48);
        panel_1.add(lblNewLabel);
        this.txtEmail = new JTextField();
        this.txtEmail.setFont(new Font("Tahoma", 0, 15));
        this.txtEmail.setBounds(55, 181, 281, 42);
        panel_1.add(this.txtEmail);
        this.txtEmail.setColumns(10);
        final String userPlaceholder = "Email";
        this.txtEmail.setText(userPlaceholder);
        this.txtEmail.setForeground(Color.GRAY);
        this.txtEmail.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (Login.this.txtEmail.getText().equals(userPlaceholder)) {
                    Login.this.txtEmail.setText("");
                    Login.this.txtEmail.setForeground(Color.BLACK);
                }

            }

            public void focusLost(FocusEvent e) {
                if (Login.this.txtEmail.getText().isEmpty()) {
                    Login.this.txtEmail.setForeground(Color.GRAY);
                    Login.this.txtEmail.setText(userPlaceholder);
                }

            }
        });
        JCheckBox chckbxNewCheckBox = new JCheckBox("Remember me");
        chckbxNewCheckBox.setBackground(new Color(255, 255, 255));
        chckbxNewCheckBox.setBounds(47, 308, 117, 21);
        panel_1.add(chckbxNewCheckBox);
        JLabel lblNewLabel_1 = new JLabel("Forgot password?");
        lblNewLabel_1.setBounds(233, 312, 113, 13);
        panel_1.add(lblNewLabel_1);
        JButton btnIngresar = new JButton("Ingresar");
        btnIngresar.setBackground(new Color(192, 192, 192));
        btnIngresar.setFont(new Font("Tahoma", 0, 17));
        btnIngresar.setBounds(147, 354, 117, 29);
        panel_1.add(btnIngresar);
        btnIngresar.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String email = Login.this.txtEmail.getText();
                String password = new String(Login.this.txtPassword.getPassword());
                User user = conexion.loginAndGetUser(email, password);
                if (user != null) {
                    System.out.println("Bienvenido");
                    User.setCurrentUser(user);
                    Dashboard dashboard = new Dashboard();
                    dashboard.frame.setVisible(true);
                    Login.this.frame.dispose();
                } else {
                    JOptionPane.showMessageDialog((Component)null, "Credenciales incorrectas");
                }

            }
        });
        this.txtPassword = new JPasswordField();
        this.txtPassword.setFont(new Font("Tahoma", 0, 15));
        this.txtPassword.setBounds(55, 259, 281, 42);
        panel_1.add(this.txtPassword);
        final String passPlaceholder = "Password";
        this.txtPassword.setEchoChar('\u0000');
        this.txtPassword.setText(passPlaceholder);
        this.txtPassword.setForeground(Color.GRAY);
        this.txtPassword.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (String.valueOf(Login.this.txtPassword.getPassword()).equals(passPlaceholder)) {
                    Login.this.txtPassword.setText("");
                    Login.this.txtPassword.setForeground(Color.BLACK);
                    Login.this.txtPassword.setEchoChar('•');
                }

            }

            public void focusLost(FocusEvent e) {
                if (String.valueOf(Login.this.txtPassword.getPassword()).isEmpty()) {
                    Login.this.txtPassword.setForeground(Color.GRAY);
                    Login.this.txtPassword.setText(passPlaceholder);
                    Login.this.txtPassword.setEchoChar('\u0000');
                }

            }
        });
        ImageIcon icono2 = new ImageIcon(this.getClass().getResource("/iconos/user4.png"));
        Image image = icono2.getImage().getScaledInstance(80, 80, 4);
        ImageIcon icono5 = new ImageIcon(image);
        JLabel lblNewLabel_4 = new JLabel("", icono5, 2);
        lblNewLabel_4.setBounds(147, 70, 117, 100);
        panel_1.add(lblNewLabel_4);
        JLabel lblNewLabel_5 = new JLabel("don't have an account yet?");
        lblNewLabel_5.setBounds(76, 399, 164, 14);
        panel_1.add(lblNewLabel_5);
        JLabel lblNewLabel_6 = new JLabel("create account");
        lblNewLabel_6.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Register registerWindow = new Register();
                registerWindow.frame.setVisible(true);
                Login.this.frame.dispose();
            }
        });
        lblNewLabel_6.setBounds(230, 399, 98, 14);
        panel_1.add(lblNewLabel_6);
    }
}