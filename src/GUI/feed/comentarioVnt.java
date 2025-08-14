package GUI.feed;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import Modules.comentario;
import Modules.User;

public class comentarioVnt extends JFrame {
    private int idTarea;
    private JPanel panelListaComentarios;
    private JTextArea txtNuevoComentario;
    private User idUsuario;

    public comentarioVnt(int idTarea) {
        this.idTarea = idTarea;
        this.idUsuario = User.getCurrentUser();
        setTitle("Comentarios de la publicación");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initialize();
        cargarComentarios();
    }
    
    
    private void enviarComentario() {
        String texto = txtNuevoComentario.getText().trim();
        if (!texto.isEmpty()) {
            comentario.agregarComentario(idTarea, texto, idUsuario.getCurrentUserId());
            txtNuevoComentario.setText("");
            cargarComentarios();
        } else {
            JOptionPane.showMessageDialog(this, "Escribe un comentario antes de enviar.");
        }
    }


    private void initialize() {
        setLayout(new BorderLayout());

        panelListaComentarios = new JPanel();
        panelListaComentarios.setLayout(new BoxLayout(panelListaComentarios, BoxLayout.Y_AXIS));
        panelListaComentarios.setBackground(Color.WHITE);

        JScrollPane scrollComentarios = new JScrollPane(panelListaComentarios);
        scrollComentarios.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        txtNuevoComentario = new JTextArea(3, 30);
        txtNuevoComentario.setLineWrap(true);
        txtNuevoComentario.setWrapStyleWord(true);

        JButton btnEnviar = new JButton("Enviar");
        btnEnviar.addActionListener(e -> enviarComentario());

        JPanel panelNuevoComentario = new JPanel(new BorderLayout());
        panelNuevoComentario.setBorder(BorderFactory.createTitledBorder("Nuevo comentario"));
        panelNuevoComentario.add(new JScrollPane(txtNuevoComentario), BorderLayout.CENTER);
        panelNuevoComentario.add(btnEnviar, BorderLayout.EAST);

        add(scrollComentarios, BorderLayout.CENTER);
        add(panelNuevoComentario, BorderLayout.SOUTH);
    }
    

    private void cargarComentarios() {
        panelListaComentarios.removeAll();

        List<comentario.ComentarioDatos> lista = comentario.obtenerComentarios(idTarea);

        if (lista.isEmpty()) {
            JLabel lblSinComentarios = new JLabel("No hay comentarios aún.");
            lblSinComentarios.setForeground(Color.GRAY);
            lblSinComentarios.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelListaComentarios.add(lblSinComentarios);
        } else {
            for (comentario.ComentarioDatos c : lista) {
                JPanel panelComentario = new JPanel();
                panelComentario.setLayout(new BorderLayout());
                panelComentario.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5, 5, 5, 5),
                    BorderFactory.createLineBorder(new Color(200,200,200), 1)
                ));
                panelComentario.setBackground(new Color(245, 245, 245));

                JLabel lblUsuarioFecha = new JLabel(c.usuario + " - " + c.fecha.toString());
                lblUsuarioFecha.setFont(new Font("Tahoma", Font.BOLD, 12));

                JLabel lblComentario = new JLabel("<html>" + c.comentario + "</html>");
                lblComentario.setFont(new Font("Tahoma", Font.PLAIN, 12));
                lblComentario.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

                panelComentario.add(lblUsuarioFecha, BorderLayout.NORTH);
                panelComentario.add(lblComentario, BorderLayout.CENTER);

                panelListaComentarios.add(panelComentario);
                panelListaComentarios.add(Box.createRigidArea(new Dimension(0,5))); // separador tipo cascada
            }
        }

        panelListaComentarios.revalidate();
        panelListaComentarios.repaint();
    }

}