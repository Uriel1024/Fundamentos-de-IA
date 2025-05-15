package nutricion;

import org.jpl7.Query;
import org.jpl7.Term;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GUIFrame extends JFrame {
    private final JTextField ageField, heightField, weightField;
    private final JRadioButton maleRadio, femaleRadio;
    private final JTextArea resultadosArea;
    private final JLabel jMenu;
    private final JButton firstButton, prevButton, nextButton, lastButton;
    private final JLabel[] desayuno, almuerzo, comida, merienda, cena;

    private List<MenuDieta> menus = new ArrayList<>();
    private int currentIndex = 0;

    public GUIFrame() {
        setTitle("Planificador de Nutrición Dra. Miku");
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Imagen de Miku
        JLabel imag = new JLabel();
        ImageIcon icono = new ImageIcon("img/miku.png");
        Image imagen = icono.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        imag.setIcon(new ImageIcon(imagen));
        imag.setBounds(780, 10, 200, 200);
        add(imag);

        // Datos usuario
        add(new JLabel("Sexo:") {{ setBounds(10, 10, 50, 25); }});
        maleRadio = new JRadioButton("Masculino"); maleRadio.setBounds(70, 10, 100, 25); add(maleRadio);
        femaleRadio = new JRadioButton("Femenino"); femaleRadio.setBounds(180, 10, 100, 25); add(femaleRadio);
        new ButtonGroup() {{ add(maleRadio); add(femaleRadio); }};

        add(new JLabel("Edad (años):") {{ setBounds(10, 50, 100, 25); }});
        ageField = new JTextField(); ageField.setBounds(120, 50, 60, 25); add(ageField);

        add(new JLabel("Estatura (m):") {{ setBounds(200, 50, 100, 25); }});
        heightField = new JTextField(); heightField.setBounds(310, 50, 60, 25); add(heightField);

        add(new JLabel("Peso (kg):") {{ setBounds(390, 50, 80, 25); }});
        weightField = new JTextField(); weightField.setBounds(480, 50, 60, 25); add(weightField);

        JButton calcularButton = new JButton("Calcular TMB y Buscar Menús");
        calcularButton.setBounds(560, 50, 200, 25); add(calcularButton);

        // Resultados
        jMenu = new JLabel(); jMenu.setBounds(10, 90, 300, 25);
        jMenu.setFont(new Font("Consolas", Font.BOLD, 14));
        add(jMenu);

        resultadosArea = new JTextArea(); resultadosArea.setEditable(false);
        resultadosArea.setLineWrap(true); resultadosArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(resultadosArea);
        scroll.setBounds(10, 120, 760, 80);
        add(scroll);

        // Botones de navegación justo debajo de resultados
        firstButton = new JButton("|<");
        prevButton  = new JButton("<");
        nextButton  = new JButton(">");
        lastButton  = new JButton(">|");
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        navPanel.setBounds(500, 210, 760, 30);
        navPanel.add(firstButton);
        navPanel.add(prevButton);
        navPanel.add(nextButton);
        navPanel.add(lastButton);
        add(navPanel);

        // Etiquetas y paneles de platillos
        String[] labels = {"Desayuno", "Almuerzo", "Comida", "Merienda", "Cena"};
        int y = 250;
        JPanel pDesayuno = panelConLayout(10, y, 760, 80, 1, 4);
        add(label(labels[0], 10, y - 25)); add(pDesayuno);

        JPanel pAlmuerzo = panelConLayout(10, y + 100, 760, 80, 1, 2);
        add(label(labels[1], 10, y + 75)); add(pAlmuerzo);

        JPanel pComida = panelConLayout(10, y + 200, 760, 80, 1, 3);
        add(label(labels[2], 10, y + 175)); add(pComida);

        JPanel pMerienda = panelConLayout(10, y + 300, 760, 80, 1, 2);
        add(label(labels[3], 10, y + 275)); add(pMerienda);

        JPanel pCena = panelConLayout(10, y + 400, 760, 80, 1, 2);
        add(label(labels[4], 10, y + 375)); add(pCena);

        // Arrays de JLabels
        desayuno = new JLabel[4]; for (int i = 0; i < 4; i++) pDesayuno.add(desayuno[i] = new JLabel());
        almuerzo = new JLabel[2]; for (int i = 0; i < 2; i++) pAlmuerzo.add(almuerzo[i] = new JLabel());
        comida   = new JLabel[3]; for (int i = 0; i < 3; i++) pComida.add(comida[i] = new JLabel());
        merienda = new JLabel[2]; for (int i = 0; i < 2; i++) pMerienda.add(merienda[i] = new JLabel());
        cena     = new JLabel[2]; for (int i = 0; i < 2; i++) pCena.add(cena[i]     = new JLabel());

        // Acciones
        calcularButton.addActionListener(e -> onBuscar());
        firstButton.addActionListener(e -> mostrar(0));
        prevButton.addActionListener(e -> mostrar(currentIndex - 1));
        nextButton.addActionListener(e -> mostrar(currentIndex + 1));
        lastButton.addActionListener(e -> mostrar(menus.size() - 1));
    }

    private JPanel panelConLayout(int x,int y,int w,int h,int rows,int cols) {
        JPanel p = new JPanel(new GridLayout(rows, cols)); p.setBounds(x,y,w,h); return p;
    }
    private JLabel label(String text,int x,int y){ JLabel l=new JLabel(text); l.setBounds(x,y,100,25); return l; }

    private void onBuscar() {
        resultadosArea.setText(""); menus.clear(); jMenu.setText("");
        try {
            boolean isMale = maleRadio.isSelected(), isFemale = femaleRadio.isSelected();
            if(!isMale && !isFemale) throw new IllegalArgumentException("Seleccione sexo");
            int age = Integer.parseInt(ageField.getText());
            double height = Double.parseDouble(heightField.getText())*100;
            double weight = Double.parseDouble(weightField.getText());
            double tmb = isMale
                    ? 88.36+13.4*weight+4.8*height-5.7*age
                    : 447.6+9.2*weight+3.1*height-4.3*age;
            int gasto = (int)Math.round(tmb);

            resultadosArea.append("TMB estimado: "+gasto+" kcal\n\n");
            menus = obtenerMenusDesdeProlog(gasto);
            if(menus.isEmpty()) { resultadosArea.append("No hay menús disponibles.\n"); return; }
            mostrar(0);
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: "+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar(int idx) {
        if(idx<0||idx>=menus.size()) return;
        currentIndex=idx;
        MenuDieta m = menus.get(idx);
        jMenu.setText("Menú "+(idx+1)+" de "+menus.size());
        resultadosArea.setText(m.toString());
        actualizar(desayuno, m.getDesayuno());
        actualizar(almuerzo, m.getAlmuerzo());
        actualizar(comida,   m.getComida());
        actualizar(merienda, m.getMerienda());
        actualizar(cena,     m.getCena());
    }

    private void actualizar(JLabel[] labels, List<String> items) {
        for(int i=0;i<labels.length;i++) {
            if(i<items.size()){
                ImageIcon ic=new ImageIcon("img/"+items.get(i)+".jpg");
                Image im=ic.getImage().getScaledInstance(80,80,Image.SCALE_SMOOTH);
                labels[i].setIcon(new ImageIcon(im));
            } else labels[i].setIcon(null);
        }
    }

    private List<MenuDieta> obtenerMenusDesdeProlog(int gasto) {
        List<MenuDieta> res = new ArrayList<>();
        try {
            new Query("consult('knowledge.pl')").hasSolution();
            Query q = new Query("dietas("+gasto+",Menus)");
            if(q.hasSolution()){
                Term list = q.oneSolution().get("Menus");
                for(Term menu: list.listToTermArray()){
                    List<String> d = term2list(menu.arg(1));
                    List<String> a = term2list(menu.arg(2));
                    List<String> c = term2list(menu.arg(3));
                    List<String> me= term2list(menu.arg(4));
                    List<String> ce= term2list(menu.arg(5));
                    Term kt = menu.arg(6);
                    int kcal = kt.isInteger()?kt.intValue():kt.arg(1).intValue();
                    res.add(new MenuDieta(d,a,c,me,ce,kcal));
                }
            }
        } catch(Exception e){ e.printStackTrace(); }
        return res;
    }

    private List<String> term2list(Term t){ List<String> L=new ArrayList<>(); for(Term x:t.listToTermArray()) L.add(x.name()); return L; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUIFrame().setVisible(true));
    }
}
