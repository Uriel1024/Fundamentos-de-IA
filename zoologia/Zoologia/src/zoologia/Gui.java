package zoologia;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import org.jpl7.Query;

public class Gui extends JFrame {
    private Query q1;
    private String[] clases;
    private String[] properties;
    private JList<String> listaClases;
    private JList<String> listaPropiedades;
    private final DefaultListModel<String> listModel1 = new DefaultListModel<>();
    private final DefaultListModel<String> listModel2 = new DefaultListModel<>();
    private final JTextArea displayArea = new JTextArea();
    private final JTextArea descripcionArea = new JTextArea();
    private final JLabel imagen = new JLabel();
    private final JTextArea areaResultados = new JTextArea();

    public Gui() {
        super("Guitarras");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        int width  = pantalla.width,
                height = pantalla.height;
        setBounds((width - 865) / 2, (height - 600) / 2, 865, 610);
        initComponents();
    }

    private void initComponents() {
        // Pestañas
        JTabbedPane tabPan = new JTabbedPane();
        tabPan.setBounds(0, 0, 855, 580);
        add(tabPan);

        //Consulta clases
        JPanel tab01 = new JPanel(null);
        tabPan.add("Consulta Clases", tab01);

        // Cargo Prolog
        cargaConocimiento("engine.pl");
        cargaConocimiento("knowledge.pl");

        // Listado de clases en scroll
        clases = consultaClases();
        for (String c : clases) listModel1.addElement(c.trim());
        listaClases = new JList<>(listModel1);
        listaClases.setFont(new Font("Consolas", Font.BOLD, 18));
        listaClases.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

        JScrollPane scrollClases = new JScrollPane(listaClases);
        scrollClases.setBounds(10, 30, 300, 200);
        scrollClases.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Área de propiedades
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Arial", Font.PLAIN, 16));
        displayArea.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        JScrollPane spProps = new JScrollPane(displayArea);
        spProps.setBounds(10, 265, 300, 150);

        // Área de descripción
        descripcionArea.setEditable(false);
        descripcionArea.setFont(new Font("Arial", Font.PLAIN, 16));
        descripcionArea.setLineWrap(true);
        descripcionArea.setWrapStyleWord(true);
        descripcionArea.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        JScrollPane spDesc = new JScrollPane(descripcionArea);
        spDesc.setBounds(10, 450, 300, 85);

        // Imagen
        imagen.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        imagen.setBounds(330, 30, 500, 500);

        JLabel et01 = new JLabel("Clases:");
        et01.setFont(new Font("Arial", Font.BOLD, 16));
        et01.setBounds(10, 10, 300, 20);

        JLabel et02 = new JLabel("Propiedades:");
        et02.setFont(new Font("Arial", Font.BOLD, 16));
        et02.setBounds(10, 245, 300, 20);

        JLabel et03 = new JLabel("Descripción:");
        et03.setFont(new Font("Arial", Font.BOLD, 16));
        et03.setBounds(10, 430, 300, 20);

        //Componentes
        tab01.add(et01);
        tab01.add(et02);
        tab01.add(et03);
        tab01.add(scrollClases);
        tab01.add(spProps);
        tab01.add(spDesc);
        tab01.add(imagen);

        listaClases.addListSelectionListener(this::gestionaClases);
        listaClases.setSelectedIndex(0);

        //Búsqueda por propiedades
        JPanel tab02 = new JPanel(null);
        tabPan.add("Búsqueda por propiedades", tab02);

        // Listado de todas las propiedades
        properties = consultaTodasPropiedades();
        for (String p : properties) listModel2.addElement(p.trim());
        listaPropiedades = new JList<>(listModel2);
        listaPropiedades.setFont(new Font("Consolas", Font.BOLD, 18));
        listaPropiedades.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        JScrollPane spTodas = new JScrollPane(listaPropiedades);
        spTodas.setBounds(10, 30, 400, 500);

        JLabel et04 = new JLabel("Todas las Propiedades");
        et04.setFont(new Font("Arial", Font.BOLD, 16));
        et04.setBounds(10, 10, 300, 20);

        // Área de resultados
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Arial", Font.PLAIN, 16));
        areaResultados.setLineWrap(true);
        areaResultados.setWrapStyleWord(true);
        areaResultados.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        JScrollPane spResultados = new JScrollPane(areaResultados);
        spResultados.setBounds(420, 30, 400, 500);

        tab02.add(et04);
        tab02.add(spTodas);
        tab02.add(spResultados);

        listaPropiedades.addListSelectionListener(this::gestionaPropiedades);
        listaPropiedades.setSelectedIndex(0);

        //Árbol
        JPanel tab03 = new JPanel(null);
        tabPan.add("Árbol taxonómico", tab03);
        JLabel arbol = new JLabel(new ImageIcon("imagenes/arbol.jpg"));
        JScrollPane spArbol = new JScrollPane(arbol);
        spArbol.setBounds(0, 0, 840, 550);
        tab03.add(spArbol);

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                goodBye();
            }
        });
    }


    private void gestionaClases(ListSelectionEvent ev) {
        String sel = listaClases.getSelectedValue();
        StringBuilder t = new StringBuilder();
        if (!"top".equals(sel.trim())) {
            for (String sup : consultaSuperclases(sel))
                if (!"top".equals(sup.trim()))
                    t.append("es(").append(sup.trim()).append(")\n");
        }
        for (String prop : consultaPropiedades(sel))
            t.append(prop.trim()).append("\n");
        displayArea.setText(t.toString());

        String desc = obtieneDescripcion(sel);
        desc = desc.substring(1, desc.length() - 1);
        descripcionArea.setText(desc);

        imagen.setIcon(new ImageIcon("imagenes/" + sel + ".jpg"));
    }

    private void gestionaPropiedades(ListSelectionEvent ev) {
        String propiedad = listaPropiedades.getSelectedValue();
        if (propiedad == null || propiedad.isBlank()) return;
        propiedad = propiedad.replace(" ", "");

        q1 = new Query("tiene_propiedad(" + propiedad + ", L)");
        StringBuilder res = new StringBuilder();
        if (q1.hasSolution()) {
            Map<String, ?> sol = q1.nextSolution();
            String lista = sol.get("L").toString();
            String[] clases = lista.substring(1, lista.length() - 1).split(",");
            res.append("Clases con ").append(propiedad).append(":\n\n");
            for (String c : clases) res.append("• ").append(c.trim()).append("\n");
        } else {
            res.append("No se encontraron clases con esa propiedad.");
        }
        areaResultados.setText(res.toString());
    }

    // Métodos de consulta Prolog
    private String[] consultaClases() {
        q1 = new Query("clases(L)");
        if (q1.hasSolution()) {
            String r = q1.nextSolution().get("L").toString();
            return r.substring(r.indexOf('[') + 1, r.indexOf(']')).split(",");
        }
        return new String[0];
    }

    private String[] consultaPropiedades(String obj) {
        q1 = new Query("propiedadesc(" + obj + ", L)");
        if (q1.hasSolution()) {
            String r = q1.nextSolution().get("L").toString();
            return r.substring(r.indexOf('[') + 1, r.indexOf(']')).split(",");
        }
        return new String[0];
    }

    private String[] consultaSuperclases(String clase) {
        q1 = new Query("superclases_de(" + clase + ", L)");
        if (q1.hasSolution()) {
            String r = q1.nextSolution().get("L").toString();
            return r.substring(r.indexOf('[') + 1, r.indexOf(']')).split(",");
        }
        return new String[0];
    }

    private String[] consultaTodasPropiedades() {
        q1 = new Query("todas_propiedades(L)");
        if (q1.hasSolution()) {
            String r = q1.nextSolution().get("L").toString();
            return r.substring(r.indexOf('[') + 1, r.indexOf(']')).split(",");
        }
        return new String[0];
    }

    private String obtieneDescripcion(String clase) {
        q1 = new Query("obtiene_descripcion(" + clase + ", L)");
        if (q1.hasSolution()) {
            return q1.nextSolution().get("L").toString();
        }
        return "";
    }

    private void cargaConocimiento(String archivo) {
        q1 = new Query("consult('prolog/" + archivo + "')");
        System.out.println("consult('" + archivo + "') " + (q1.hasSolution() ? "OK" : "FAIL"));
    }

    private void goodBye() {
        int resp = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres salir?", "Salir", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) System.exit(0);
    }
}
