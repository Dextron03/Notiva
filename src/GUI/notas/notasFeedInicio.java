package GUI.notas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import Modules.User;

public class notasFeedInicio extends JFrame {
    private JPanel panelNotas;
    private JScrollPane scrollPane;
    private int posicionY = 0;
    private User idUsuario;

    private JPanel notaSeleccionada = null;
    private Map<JPanel, NotaData> notaPanelDataMap = new HashMap<>();

    public notasFeedInicio() {
        getContentPane().setBackground(Color.WHITE);
        initialize();
        this.idUsuario = User.getCurrentUser();
        cargarNotasDesdeBD();
    }

    private void initialize() {
        setTitle("Notas");
        setBounds(100, 100, 938, 557);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel lblNotas = new JLabel("Bloc de notas");
        lblNotas.setBounds(35, 21, 172, 25);
        lblNotas.setFont(new Font("Tahoma", Font.BOLD, 20));
        getContentPane().add(lblNotas);

        JToolBar toolBar = new JToolBar();
        toolBar.setToolTipText("");
        toolBar.setBounds(0, 0, 1051, 21);
        getContentPane().add(toolBar);

        JButton btnNewButton = new JButton("Dashboard");
        btnNewButton.setEnabled(false);
        toolBar.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("Tareas");
        btnNewButton_1.setEnabled(false);
        toolBar.add(btnNewButton_1);

        JButton btnNewButton_4 = new JButton("GUI/feed");
        btnNewButton_4.setEnabled(false);
        toolBar.add(btnNewButton_4);

        JButton btnNewButton_3 = new JButton("Perfil");
        btnNewButton_3.setEnabled(false);
        toolBar.add(btnNewButton_3);

        JButton btnNewButton_2 = new JButton("Notas");
        btnNewButton_2.setEnabled(true);
        toolBar.add(btnNewButton_2);

        JButton btnNewButton_5 = new JButton("Cerrar sesión");
        btnNewButton_5.setEnabled(false);
        toolBar.add(btnNewButton_5);

        JPanel topPanel = new JPanel(null);
        topPanel.setBounds(0, 0, 924, 100);
        add(topPanel);

        JButton btnAgregarNota = new JButton("Agregar nota");
        btnAgregarNota.setBounds(25, 60, 120, 25);
        topPanel.add(btnAgregarNota);
        btnAgregarNota.addActionListener(e -> {
            NuevaNota nueva = new NuevaNota(this);
            nueva.setVisible(true);
        });

        JButton btnEliminarNota = new JButton("Eliminar");
        btnEliminarNota.setBounds(160, 60, 100, 25);
        topPanel.add(btnEliminarNota);
        btnEliminarNota.addActionListener(e -> eliminarNotaSeleccionada());

        JButton btnEditarNota = new JButton("Editar");
        btnEditarNota.setBounds(280, 60, 100, 25);
        topPanel.add(btnEditarNota);
        btnEditarNota.addActionListener(e -> {
            if (notaSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "Selecciona primero una nota.");
                return;
            }
            NotaData data = notaPanelDataMap.get(notaSeleccionada);
            if (data != null) {
                verNotas vista = new verNotas(this,data.titulo, data.contenido);
                vista.setVisible(true);
            }
        });

        panelNotas = new JPanel();
        panelNotas.setBackground(Color.WHITE);
        panelNotas.setLayout(null);
        panelNotas.setPreferredSize(new Dimension(910, 1100));

        scrollPane = new JScrollPane(panelNotas);
        scrollPane.setBounds(0, 100, 924, 450);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane);
    }

    public static class NotaData {
        String titulo, contenido, fecha;

        NotaData(String t, String c, String f) {
            this.titulo = t;
            this.contenido = c;
            this.fecha = f;
        }
    }
    // Se obtiene el contenido desde la base de datos  mediante el título
    public void agregarNotaAlFeed(String titulo, String fecha) {
        String contenido = "";
        try (Connection cx = App.conexion.obtenerConexion()) {
            PreparedStatement ps = cx.prepareStatement(
                    "SELECT contenido FROM notas WHERE titulo = ?");
            ps.setString(1, titulo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) contenido = rs.getString("contenido");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JPanel panelNota = new JPanel(null);
        panelNota.setBounds(25, posicionY, 860, 50);
        panelNota.setBackground(new Color(230, 230, 250));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setBounds(10, 10, 400, 15);
        panelNota.add(lblTitulo);

        JLabel lblFecha = new JLabel(fecha);
        lblFecha.setBounds(420, 10, 300, 15);
        panelNota.add(lblFecha);

        notaPanelDataMap.put(panelNota, new NotaData(titulo, contenido, fecha));

        panelNota.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (notaSeleccionada != null) {
                    notaSeleccionada.setBackground(new Color(230, 230, 250));
                }
                notaSeleccionada = panelNota;
                panelNota.setBackground(new Color(173, 216, 230));
            }
        });

        panelNotas.add(panelNota);
        panelNotas.revalidate();
        panelNotas.repaint();
        posicionY += 60;
    }

    public void cargarNotasDesdeBD() {
        try (Connection cx = App.conexion.obtenerConexion()) {
            PreparedStatement ps = cx.prepareStatement(
                    "SELECT titulo, fecha_creacion FROM notas WHERE propietario = ? AND propietario != null ORDER BY fecha_creacion DESC");
            
           ps.setInt(1, idUsuario.getCurrentUserId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String titulo = rs.getString("titulo");
                String fecha = rs.getString("fecha_creacion");
                agregarNotaAlFeed(titulo, fecha);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void eliminarNotaSeleccionada() {
        if (notaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Selecciona primero una nota.");
            return;
        }

        NotaData data = notaPanelDataMap.get(notaSeleccionada);
        if (data == null) return;

        int ok = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar nota “" + data.titulo + "”?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );
        if (ok == JOptionPane.YES_OPTION) {
            try (Connection cx = App.conexion.obtenerConexion()) {
                PreparedStatement ps = cx.prepareStatement(
                        "DELETE FROM notas WHERE titulo = ?");
                ps.setString(1, data.titulo);
                ps.executeUpdate();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            panelNotas.remove(notaSeleccionada);
            notaPanelDataMap.remove(notaSeleccionada);
            panelNotas.revalidate();
            panelNotas.repaint();
            notaSeleccionada = null;
        }
    }
    public void refrescarNotas() {
        panelNotas.removeAll();
        notaPanelDataMap.clear();
        posicionY = 0;
        cargarNotasDesdeBD();
        panelNotas.revalidate();
        panelNotas.repaint();
    }
    public void actualizarFeed() {
        refrescarNotas();
    }
}