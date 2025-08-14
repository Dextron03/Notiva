package GUI.feed;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import Modules.User;
import Modules.publicacionDatos;
import Modules.likes;

public class Feed extends JFrame {
    private JTextField textField;
    private JPanel contenedorPublicacion; 
    private User id_Usuario;

    public Feed() {
        this.initialize();
        this.id_Usuario = User.getCurrentUser();
    }

    public JPanel cargarPublicaciones(publicacionDatos pub) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setMaximumSize(new Dimension(650, 250));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel lblUsuario = new JLabel("De: Usuario " + pub.idTarea);
        lblUsuario.setFont(new Font("Tahoma", Font.BOLD, 13));
        JLabel lblFecha = new JLabel("Publicado el: " + pub.fechaCreacion);
        lblFecha.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblFecha.setForeground(Color.GRAY);
        header.add(lblUsuario, BorderLayout.WEST);
        header.add(lblFecha, BorderLayout.EAST);

        JLabel lblTitulo = new JLabel(pub.titulo);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JTextArea txtDescripcion = new JTextArea(pub.descripcion);
        txtDescripcion.setFont(new Font("Tahoma", Font.PLAIN, 13));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setEditable(false);
        txtDescripcion.setOpaque(false);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        footer.setOpaque(false);

        // Botón de Like inicializado con la cantidad real
        int likesIniciales = likes.obtenerLikes(pub.idTarea);
        JButton btnLike = new JButton("👍 Likes " + likesIniciales);
        btnLike.putClientProperty("id_tarea", pub.idTarea);

        btnLike.addActionListener(e -> {
            int idTarea = (int) btnLike.getClientProperty("id_tarea");
            int idUsuario = id_Usuario.getCurrentUserId();

            if (!likes.usuarioYaDioLike(idTarea, idUsuario)) {
                likes.registrarLike(idTarea, idUsuario);
                btnLike.setText("👍 Likes " + likes.obtenerLikes(idTarea));
            } else {
                System.out.println("El usuario ya dio like a esta publicación.");
            }
        });

        JButton btnComentar = new JButton("💬 Comentar");
        btnComentar.putClientProperty("id_tarea", pub.idTarea);

        btnComentar.addActionListener(e -> {
            int idTarea = (int) btnComentar.getClientProperty("id_tarea");
            new GUI.feed.comentarioVnt(idTarea).setVisible(true); 
            //GUI.comentarios.VentanaComentarios(idTarea).setVisible(true);
        });
        
        
        
        
        
        JButton btnGuardar = new JButton("💾 ver perfil");

        footer.add(btnLike);
        footer.add(btnComentar);
        footer.add(btnGuardar);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        panelInferior.add(txtDescripcion, BorderLayout.CENTER);
        panelInferior.add(footer, BorderLayout.SOUTH);

        panel.add(header, BorderLayout.NORTH);
        panel.add(lblTitulo, BorderLayout.CENTER);
        panel.add(panelInferior, BorderLayout.SOUTH);

        return panel;
    }

    private void initialize() {
        this.setTitle("Feed");
        this.setBounds(100, 100, 940, 560);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.getContentPane().setLayout(null);

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

        JPanel panelFeed = new JPanel();
        panelFeed.setBackground(Color.WHITE);
        panelFeed.setLayout(new BorderLayout());

        JLabel lblMensaje = new JLabel("Comparte tus experiencias con tus amigos");
        lblMensaje.setFont(new Font("Tahoma", Font.ITALIC, 17));
        panelFeed.add(lblMensaje, BorderLayout.NORTH);

        contenedorPublicacion = new JPanel();
        contenedorPublicacion.setLayout(new BoxLayout(contenedorPublicacion, BoxLayout.Y_AXIS));
        contenedorPublicacion.setBackground(Color.WHITE);

        JScrollPane scrollFeed = new JScrollPane(contenedorPublicacion);
        scrollFeed.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panelFeed.add(scrollFeed, BorderLayout.CENTER);

        panelFeed.setBounds(10, 31, 672, 479);
        this.getContentPane().add(panelFeed);

        actualizarFeed();
    }

    public void actualizarFeed() {
        contenedorPublicacion.removeAll();
        List<publicacionDatos> lista = publicacionDatos.obtenerPublicaciones();
        for (int i = lista.size() - 1; i >= 0; i--) {
            JPanel panelPub = cargarPublicaciones(lista.get(i));
            contenedorPublicacion.add(panelPub);
        }
        contenedorPublicacion.revalidate();
        contenedorPublicacion.repaint();

        JScrollPane scroll = (JScrollPane) contenedorPublicacion.getParent().getParent();
        scroll.getVerticalScrollBar().setValue(0);
    }

    public void agregarNuevaPublicacion(publicacionDatos pub) {
        JPanel panelPub = cargarPublicaciones(pub);
        contenedorPublicacion.add(panelPub, 0);
        contenedorPublicacion.revalidate();
        contenedorPublicacion.repaint();

        JScrollPane scroll = (JScrollPane) contenedorPublicacion.getParent().getParent();
        scroll.getVerticalScrollBar().setValue(0);
    }
}
