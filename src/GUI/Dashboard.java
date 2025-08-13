package GUI;

import App.conexion;
import GUI.notas.notasFeedInicio;
import GUI.tareas.*;
import GUI.feed.Feed;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Modules.User;

public class Dashboard {

    JFrame frame;
    private JLabel tareasPendientes;
    private JLabel tareasCreadas;
    private JLabel tareasRealizadas;
    private JLabel tareasConjunto;
    private DefaultPieDataset pie;
    private ChartPanel chartPanel;
    private User idUsuario;

    public Dashboard() {
        this.idUsuario = User.getCurrentUser();
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 938, 557);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JToolBar toolBar = new JToolBar();
        toolBar.setBounds(0, 0, 924, 21);
        frame.getContentPane().add(toolBar);

        JButton btnDashboard = new JButton("Dashboard");
        toolBar.add(btnDashboard);

        JButton btnFeed = new JButton("Feed");
        toolBar.add(btnFeed);
        btnFeed.addActionListener(e -> {
            Feed mostrarFeed = new Feed();
            mostrarFeed.setVisible(true);
        });

        JButton btnTareas = new JButton("Tareas");
        toolBar.add(btnTareas);
        btnTareas.addActionListener(e -> {
            tareasFeedInicio ventanaFeed = new tareasFeedInicio();
            ventanaFeed.setVisible(true);
        });

        JButton btnNotas = new JButton("Notas");
        toolBar.add(btnNotas);
        btnNotas.addActionListener(e -> {
            notasFeedInicio ventananotas = new notasFeedInicio();
            ventananotas.setVisible(true);
        });

        JButton btnPerfil = new JButton("Perfil");
        toolBar.add(btnPerfil);
        btnPerfil.addActionListener(e -> {
            datosPersonales ventana = new datosPersonales();
            ventana.setVisible(true);
        });

        JButton btnCerrarSesion = new JButton("Cerrar sesión");
        toolBar.add(btnCerrarSesion);
        btnCerrarSesion.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(frame, "¿Estás seguro?", "Cerrar sesión",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (opcion == JOptionPane.YES_OPTION) {
                Login cerrarSesion = new Login();
                cerrarSesion.frame.setVisible(true);
                frame.dispose();
            }
        });

        // Panel Tareas Pendientes
        JPanel panelPendientes = new JPanel();
        panelPendientes.setBackground(Color.WHITE);
        panelPendientes.setBounds(27, 98, 201, 133);
        panelPendientes.setLayout(null);
        frame.getContentPane().add(panelPendientes);

        JLabel lblPendientes = new JLabel("Tareas pendientes");
        lblPendientes.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblPendientes.setBounds(10, 20, 150, 21);
        panelPendientes.add(lblPendientes);

        tareasPendientes = new JLabel("0");
        tareasPendientes.setFont(new Font("Tahoma", Font.BOLD, 36));
        tareasPendientes.setBounds(20, 43, 100, 31);
        panelPendientes.add(tareasPendientes);

        pie = new DefaultPieDataset();
        obtenerTareasPendientes();

        // Panel Tareas Creadas
        JPanel panelCreadas = new JPanel();
        panelCreadas.setBackground(Color.WHITE);
        panelCreadas.setBounds(250, 98, 201, 133);
        panelCreadas.setLayout(null);
        frame.getContentPane().add(panelCreadas);

        JLabel lblCreadas = new JLabel("Tareas Creadas");
        lblCreadas.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblCreadas.setBounds(10, 23, 150, 13);
        panelCreadas.add(lblCreadas);

        tareasCreadas = new JLabel("0");
        tareasCreadas.setFont(new Font("Tahoma", Font.BOLD, 36));
        tareasCreadas.setBounds(10, 38, 100, 36);
        panelCreadas.add(tareasCreadas);

        obtenerTareasCreadas();

        // Panel Tareas Realizadas
        JPanel panelRealizadas = new JPanel();
        panelRealizadas.setBackground(Color.WHITE);
        panelRealizadas.setBounds(475, 98, 201, 133);
        panelRealizadas.setLayout(null);
        frame.getContentPane().add(panelRealizadas);

        JLabel lblRealizadas = new JLabel("Tareas Realizadas");
        lblRealizadas.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblRealizadas.setBounds(10, 20, 150, 13);
        panelRealizadas.add(lblRealizadas);

        tareasRealizadas = new JLabel("0");
        tareasRealizadas.setFont(new Font("Tahoma", Font.BOLD, 36));
        tareasRealizadas.setBounds(10, 44, 100, 36);
        panelRealizadas.add(tareasRealizadas);

        obtenerTareasRealizadas();

        // Panel Tareas en Conjunto
        JPanel panelConjunto = new JPanel();
        panelConjunto.setBackground(Color.WHITE);
        panelConjunto.setBounds(698, 98, 201, 133);
        panelConjunto.setLayout(null);
        frame.getContentPane().add(panelConjunto);

        JLabel lblConjunto = new JLabel("Tareas en conjunto");
        lblConjunto.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblConjunto.setBounds(10, 21, 150, 13);
        panelConjunto.add(lblConjunto);

        tareasConjunto = new JLabel("0");
        tareasConjunto.setFont(new Font("Tahoma", Font.BOLD, 36));
        tareasConjunto.setBounds(10, 45, 100, 36);
        panelConjunto.add(tareasConjunto);

        obtenerTareasConjunto();

        JLabel lblTitulo = new JLabel("Dashboard");
        lblTitulo.setFont(new Font("Tahoma", Font.PLAIN, 19));
        lblTitulo.setBounds(27, 64, 128, 21);
        frame.getContentPane().add(lblTitulo);

        actualizarGrafico();

        JFreeChart chart = ChartFactory.createPieChart("Resumen de tareas", pie, true, true, false);
        chartPanel = new ChartPanel(chart);
        chartPanel.setBounds(10, 252, 474, 268);
        frame.getContentPane().add(chartPanel);
    }

    private void obtenerTareasPendientes() {
        String query = "SELECT COUNT(*) AS TOTAL FROM tareausuario tus " +
                "LEFT JOIN tarea ta ON tus.id_tarea = ta.id_tarea " +
                "LEFT JOIN estadoTarea esT ON esT.id_estado = tus.estadoTarea " +
                "WHERE esT.estado = 'pendiente' AND tus.id_usuario = ?";
        try (Connection con = conexion.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, idUsuario.getCurrentUserId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int total = rs.getInt("TOTAL");
                tareasPendientes.setText(String.valueOf(total));
                pie.setValue("Tareas pendientes", total);
            }
        } catch (SQLException e) {
            mostrarError("Pendiente");
        }
    }

    private void obtenerTareasCreadas() {
        String query = "SELECT COUNT(*) AS TOTAL FROM tareausuario WHERE id_usuario = ?";
        try (Connection con = conexion.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, idUsuario.getCurrentUserId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int total = rs.getInt("TOTAL");
                tareasCreadas.setText(String.valueOf(total));
                pie.setValue("Tareas creadas", total);
            }
        } catch (SQLException e) {
            mostrarError("Creadas");
        }
    }

    private void obtenerTareasRealizadas() {
        String query = "SELECT COUNT(*) AS TOTAL FROM tareausuario tus " +
                "LEFT JOIN tarea ta ON tus.id_tarea = ta.id_tarea " +
                "LEFT JOIN estadoTarea esT ON esT.id_estado = tus.estadoTarea " +
                "WHERE esT.estado = 'realizadas' AND tus.id_usuario = ?";
        try (Connection con = conexion.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, idUsuario.getCurrentUserId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int total = rs.getInt("TOTAL");
                tareasRealizadas.setText(String.valueOf(total));
                pie.setValue("Tareas realizadas", total);
            }
        } catch (SQLException e) {
            mostrarError("Realizadas");
        }
    }

    private void obtenerTareasConjunto() {
        String query = "SELECT COUNT(*) AS TOTAL FROM tarea_compartida tc " +
                "LEFT JOIN tareausuario tu ON tu.id_tarea = tc.id_tarea " +
                "LEFT JOIN estadoTarea est ON est.id_estado = tu.estadoTarea " +
                "WHERE est.estado = 'compartida' AND tu.id_usuario = ?";
        try (Connection con = conexion.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, idUsuario.getCurrentUserId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int total = rs.getInt("TOTAL");
                tareasConjunto.setText(String.valueOf(total));
                pie.setValue("Tareas conjunto", total);
            }
        } catch (SQLException e) {
            mostrarError("Conjunto");
        }
    }

    private void mostrarError(String tipo) {
        JOptionPane.showMessageDialog(frame,
                tipo + " no se pudo conectar a la base de datos",
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void actualizarGrafico() {
        int pendientes = obtenerValorNumerico(tareasPendientes);
        int creadas = obtenerValorNumerico(tareasCreadas);
        int realizadas = obtenerValorNumerico(tareasRealizadas);
        int conjunto = obtenerValorNumerico(tareasConjunto);

        pie.setValue("Tareas pendientes", pendientes);
        pie.setValue("Tareas creadas", creadas);
        pie.setValue("Tareas realizadas", realizadas);
        pie.setValue("Tareas conjunto", conjunto);
    }

    private int obtenerValorNumerico(JLabel label) {
        try {
            return Integer.parseInt(label.getText().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
