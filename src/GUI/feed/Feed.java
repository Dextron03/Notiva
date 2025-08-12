package GUI.feed;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.LayoutManager;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;

import Modules.publicacionDatos;

public class Feed extends JFrame {
    private JTextField textField;
    private int inicio = 0;
    private final int limit = 5;
    private boolean estadoCarga = false;

    public Feed() {
        this.initialize();
    }

    public JPanel cargarPublicaciones(publicacionDatos pub) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(632, 205));
        panel.setLayout(null);

        JLabel lblUsuario = new JLabel("De: Usuario" + pub.idTarea);
        lblUsuario.setBounds(10, 10, 200, 13);
        panel.add(lblUsuario);

        JLabel lblFecha = new JLabel("Publicado el: " + pub.fechaCreacion);
        lblFecha.setBounds(500, 10, 200, 13);
        panel.add(lblFecha);

        JLabel lblTitulo = new JLabel(pub.titulo);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblTitulo.setBounds(10, 33, 400, 13);
        panel.add(lblTitulo);

        JTextArea txtDescripcion = new JTextArea(pub.descripcion);
        txtDescripcion.setFont(new Font("Tahoma", Font.PLAIN, 13));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setEditable(false);
        txtDescripcion.setBounds(10, 56, 612, 65);
        panel.add(txtDescripcion);

        JLabel lblCategoria = new JLabel("Categoria: " + pub.categoria);
        lblCategoria.setBounds(10, 121, 200, 13);
        panel.add(lblCategoria);

        JButton btnLike = new JButton("Likes 0");
        btnLike.setBounds(10, 150, 85, 21);
        panel.add(btnLike);

        JButton btnComentar = new JButton("Comentar");
        btnComentar.setBounds(105, 150, 85, 21);
        panel.add(btnComentar);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(200, 150, 85, 21);
        panel.add(btnGuardar);

        JLabel lblColaboradores = new JLabel("Colaboradores: 0");
        lblColaboradores.setBounds(500, 154, 150, 13);
        panel.add(lblColaboradores);

        return panel;
    }

    private void initialize() {
        this.setTitle("Feed");
        this.setBounds(100, 100, 940, 560);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(null);

        // Panel lateral
        JPanel panelLateral = new JPanel();
        panelLateral.setBounds(692, 21, 222, 489);
        panelLateral.setBackground(new Color(228, 228, 228));
        panelLateral.setLayout(null);
        this.getContentPane().add(panelLateral);

        this.textField = new JTextField();
        this.textField.setBounds(8, 11, 119, 20);
        panelLateral.add(this.textField);
        this.textField.setColumns(10);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(Color.WHITE);
        btnBuscar.setBounds(137, 9, 75, 23);
        panelLateral.add(btnBuscar);

        JPanel panelAmigos = new JPanel();
        panelAmigos.setBackground(Color.WHITE);
        panelAmigos.setPreferredSize(new Dimension(220, 600));
        JScrollPane scrollAmigos = new JScrollPane(panelAmigos);
        scrollAmigos.setBounds(0, 43, 222, 446);
        scrollAmigos.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panelLateral.add(scrollAmigos);
        panelAmigos.setLayout(null);

        JLabel lblAmigos = new JLabel("Amigos");
        lblAmigos.setBounds(78, 10, 68, 23);
        lblAmigos.setFont(new Font("Tahoma", Font.BOLD, 14));
        panelAmigos.add(lblAmigos);

        ImageIcon iconoM = new ImageIcon(this.getClass().getResource("/iconos/amigos.png"));
        Image imagen = iconoM.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon iconoA = new ImageIcon(imagen);
        JLabel lblIconoAmigos = new JLabel("", iconoA, JLabel.CENTER);
        lblIconoAmigos.setBounds(21, 4, 59, 50);
        panelAmigos.add(lblIconoAmigos);

        JPanel panel_2 = new JPanel();
        panel_2.setBounds(10, 64, 189, 33);
        panelAmigos.add(panel_2);
        panel_2.setLayout(null);

        JLabel lblNewLabel_6 = new JLabel("Usuario");
        lblNewLabel_6.setBounds(10, 10, 57, 13);
        panel_2.add(lblNewLabel_6);

        JLabel lblNewLabel_7 = new JLabel("Following");
        lblNewLabel_7.setBounds(88, 10, 68, 13);
        panel_2.add(lblNewLabel_7);

        // Panel feed principal
        JPanel panelFeed = new JPanel();
        panelFeed.setBackground(Color.WHITE);
        panelFeed.setLayout(new BorderLayout());

        JLabel lblMensaje = new JLabel("Comparte tus experiencias con tus amigos");
        lblMensaje.setFont(new Font("Tahoma", Font.ITALIC, 17));
        panelFeed.add(lblMensaje, BorderLayout.NORTH);

        // Contenedor de publicaciones con BoxLayout
        JPanel contenedorPublicacion = new JPanel();
        contenedorPublicacion.setLayout(new BoxLayout(contenedorPublicacion, BoxLayout.Y_AXIS));
        contenedorPublicacion.setBackground(Color.WHITE);

        // Scroll para las publicaciones
        JScrollPane scrollFeed = new JScrollPane(contenedorPublicacion);
        scrollFeed.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panelFeed.add(scrollFeed, BorderLayout.CENTER);

        panelFeed.setBounds(10, 31, 672, 479);
        this.getContentPane().add(panelFeed);

        // Cargar publicaciones
        cargarPublicaciones(contenedorPublicacion);
    }

    private void cargarPublicaciones(JPanel contenedor) {
        List<publicacionDatos> lista = publicacionDatos.obtenerPublicaciones();
        for (publicacionDatos pub : lista) {
            JPanel panelPub = cargarPublicaciones(pub);
            contenedor.add(panelPub);
        }
        contenedor.revalidate();
        contenedor.repaint();
    }
}