//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package GUI.tareas;

import App.conexion;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import Modules.CategoriaItem;

public class NuevaTarea extends JFrame {
    private JTextField textField;

    public NuevaTarea() {
        this.initialize();
    }

    private void initialize() {
        this.setBounds(100, 100, 340, 607);
        this.setDefaultCloseOperation(2);
        this.getContentPane().setLayout((LayoutManager)null);
        JLabel lblNewLabel = new JLabel("Crear nueva tarea");
        lblNewLabel.setFont(new Font("Tahoma", 1, 18));
        lblNewLabel.setBounds(20, 10, 208, 28);
        this.getContentPane().add(lblNewLabel);
        JLabel lblNewLabel_1 = new JLabel("TITULO");
        lblNewLabel_1.setBounds(20, 60, 58, 13);
        this.getContentPane().add(lblNewLabel_1);
        this.textField = new JTextField();
        this.textField.setBounds(20, 76, 273, 21);
        this.getContentPane().add(this.textField);
        this.textField.setColumns(10);
        JLabel lblNewLabel_2 = new JLabel("DESCRIPCION");
        lblNewLabel_2.setBounds(20, 119, 96, 13);
        this.getContentPane().add(lblNewLabel_2);
        final JTextArea textArea = new JTextArea();
        textArea.setBounds(20, 142, 273, 145);
        this.getContentPane().add(textArea);
        final JRadioButton rdbtnPublico = new JRadioButton("Publico");
        rdbtnPublico.setBounds(20, 300, 74, 21);
        this.getContentPane().add(rdbtnPublico);
        JRadioButton rdbtnPrivado = new JRadioButton("Privado");
        rdbtnPrivado.setBounds(96, 300, 87, 21);
        this.getContentPane().add(rdbtnPrivado);
        ButtonGroup visibilidadGroup = new ButtonGroup();
        visibilidadGroup.add(rdbtnPublico);
        visibilidadGroup.add(rdbtnPrivado);
        final JComboBox<CategoriaItem> comboBox = new JComboBox();
        comboBox.setBounds(20, 357, 273, 27);
        this.getContentPane().add(comboBox);

        try {
            Connection conn = conexion.obtenerConexion();
            String sql = "SELECT id_categoria, categoria FROM categoria";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int id = rs.getInt("id_categoria");
                String nombre = rs.getString("categoria");
                comboBox.addItem(new CategoriaItem(id, nombre));
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar categorías: " + e.getMessage());
        }

        JLabel lblNewLabel_3 = new JLabel("CATEGORIA");
        lblNewLabel_3.setBounds(20, 339, 74, 13);
        this.getContentPane().add(lblNewLabel_3);
        JLabel lblNewLabel_4 = new JLabel("FECHA DE EXPIRACION");
        lblNewLabel_4.setBounds(20, 440, 143, 13);
        this.getContentPane().add(lblNewLabel_4);
        final JSpinner spinner = new JSpinner();
        spinner.setModel(new SpinnerDateModel(new Date(), (Comparable)null, (Comparable)null, 6));
        spinner.setBounds(20, 463, 273, 27);
        this.getContentPane().add(spinner);
        JButton btnCrear = new JButton("Crear Tarea");
        btnCrear.setBounds(182, 520, 111, 27);
        this.getContentPane().add(btnCrear);
        final JCheckBox chckbxNewCheckBox = new JCheckBox("Compartir");
        chckbxNewCheckBox.setBounds(20, 402, 116, 21);
        this.getContentPane().add(chckbxNewCheckBox);
        btnCrear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String titulo = NuevaTarea.this.textField.getText();
                String descripcion = textArea.getText();
                int visibilidad = rdbtnPublico.isSelected() ? 1 : 2;
                CategoriaItem categoriaSeleccionada = (CategoriaItem)comboBox.getSelectedItem();
                int idCategoria = categoriaSeleccionada.id;
                Date fechaExp = (Date)spinner.getValue();
                if (chckbxNewCheckBox.isSelected()) {
                    System.out.println("La tarea será compartida.");
                } else {
                    System.out.println("La tarea no será compartida.");
                }

                Connection conn = conexion.obtenerConexion();

                try {
                    String sql = "INSERT INTO tarea (titulo, descripcion, visibilidad, categoria, fecha_expiracion) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, titulo);
                    ps.setString(2, descripcion);
                    ps.setInt(3, visibilidad);
                    ps.setInt(4, idCategoria);
                    ps.setDate(5, new java.sql.Date(fechaExp.getTime()));
                    ps.executeUpdate();
                    System.out.println("Tarea creada correctamente.");
                    NuevaTarea.this.dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }
        });
    }
}