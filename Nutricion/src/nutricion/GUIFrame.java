        package nutricion;

        import java.awt.Font;
        import java.awt.GridLayout;
        import java.awt.Image;
        import javax.swing.*;
        import java.util.*;
        import org.jpl7.*;

        public class GUIFrame extends JFrame
        {
            private final JLabel[] desayuno;
            private final JLabel[] comida;
            private final JLabel[] merienda;
            private final JTextField gastoField;
            private final JTextArea resultadosArea;
            private final JLabel jMenu = new JLabel();
            private final JButton firstButton;
            private final JButton prevButton;
            private final JButton nextButton;
            private final JButton lastButton;
            private List<MenuDieta> menus = new ArrayList<>();
            private int currentIndex = 0;

            public GUIFrame() {
                setTitle("Planificador de Nutrición Dra. Miku");
                setSize(800, 670);
                setLocationRelativeTo(null);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                this.setLayout(null);

                JLabel imag = new JLabel();

                ImageIcon icono = new ImageIcon("img/miku.png");
                Image imagen = icono.getImage();
                int w =  imagen.getWidth(null);
                int h =  imagen.getHeight(null);
                imagen = icono.getImage().getScaledInstance((int) (w/1.2), (int)(h/1.2), Image.SCALE_SMOOTH);
                ImageIcon iconoEscalado = new ImageIcon(imagen);
                imag.setIcon(iconoEscalado);

                imag.setBounds(450,1,imagen.getWidth(null),imagen.getHeight(null));
                this.add(imag);

                JLabel label = new JLabel("Gasto metabólico (kcal): ");
                gastoField = new JTextField(10);
                JButton buscarButton = new JButton("Buscar Menús");

                label.setBounds(10,10,150,30);
                gastoField.setBounds(150,10,100,30);
                buscarButton.setBounds(250,10,150,30);

                this.add(label);
                this.add(gastoField);
                this.add(buscarButton);

                resultadosArea = new JTextArea();
                resultadosArea.setEditable(false);
                resultadosArea.setLineWrap(true);
                resultadosArea.setWrapStyleWord(true);
                JScrollPane panelResultado = new JScrollPane(resultadosArea);
                JPanel breakfastPanel = new JPanel();
                JPanel lunchPanel = new JPanel();
                JPanel snackPanel = new JPanel();

                breakfastPanel.setLayout(new GridLayout(1,4));
                lunchPanel.setLayout(new GridLayout(1,3));
                snackPanel.setLayout(new GridLayout(1,2));

                JLabel et1 = new JLabel("Desayuno");
                JLabel et2 = new JLabel("Comida");
                JLabel et3 = new JLabel("Merienda");

                desayuno = new JLabel[4];
                comida = new JLabel[3];
                merienda = new JLabel[2];

                for (int i=0;i<desayuno.length;i++)
                {
                        desayuno[i] = new JLabel();
                        desayuno[i].setIcon(null);
                        breakfastPanel.add(desayuno[i]);
                }

                for (int i=0;i<comida.length;i++)
                {
                        comida[i] = new JLabel();
                        comida[i].setIcon(null);
                        lunchPanel.add(comida[i]);
                }

                for (int i=0;i<merienda.length;i++)
                {
                        merienda[i] = new JLabel();
                        merienda[i].setIcon(null);
                        snackPanel.add(merienda[i]);
                }

                firstButton = new JButton("First");
                prevButton = new JButton("Prev");
                nextButton = new JButton("Next");
                lastButton = new JButton("Last");

                jMenu.setBounds(10,50,200,20);
                jMenu.setFont(new Font("Consolas", Font.BOLD, 14));
                et1.setFont(new Font("Consolas", Font.BOLD, 14));
                et2.setFont(new Font("Consolas", Font.BOLD, 14));
                et3.setFont(new Font("Consolas", Font.BOLD, 14));

                panelResultado.setBounds(10,80,500,100);
                et1.setBounds(10,190,100,20);
                et2.setBounds(10,320,100,20);
                et3.setBounds(10,450,100,20);
                breakfastPanel.setBounds(10,210,400,100);
                lunchPanel.setBounds(10,340,300,100);
                snackPanel.setBounds(10,470,200,100);

                firstButton.setBounds(10,580,100,30);
                prevButton.setBounds(110,580,100,30);
                nextButton.setBounds(210,580,100,30);
                lastButton.setBounds(310,580,100,30);

                this.add(jMenu);
                this.add(panelResultado);
                this.add(et1);
                this.add(et2);
                this.add(et3);
                this.add(breakfastPanel);
                this.add(lunchPanel);
                this.add(snackPanel);
                this.add(firstButton);
                this.add(prevButton);
                this.add(nextButton);
                this.add(lastButton);

                firstButton.addActionListener(e -> mostrarPrimero());
                prevButton.addActionListener(e -> mostrarAnterior());
                nextButton.addActionListener(e -> mostrarSiguiente());
                lastButton.addActionListener(e -> mostrarUltimo());

                buscarButton.addActionListener(e -> buscarMenus());
            }

            private void buscarMenus() {
                resultadosArea.setText("");
                String gastoTexto = gastoField.getText();
                try {
                    int gasto = java.lang.Integer.parseInt(gastoTexto);
                    menus = obtenerMenusDesdeProlog(gasto);
                    currentIndex = 0;
                    if (menus.isEmpty()) {
                        resultadosArea.append("No se encontraron menús que cumplan con el criterio.\n");
                    } else {
                        mostrarMenuActual();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Por favor ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            private void mostrarMenuActual() {
                if (!menus.isEmpty() && currentIndex >= 0 && currentIndex < menus.size())
                {
                    resultadosArea.setText(menus.get(currentIndex).toString());
                    jMenu.setText("Menu: " + (currentIndex+1)+ " de " + menus.size() );
                    MenuDieta m = menus.get(currentIndex);
                    List<String> mDesayuno = m.getDesayuno();
                    List<String> mComida = m.getComida();
                    List<String> mMerienda = m.getMerienda();

                    for (JLabel food1 : desayuno) {
                        food1.setIcon(null);
                    }

                    for (JLabel food2 : comida) {
                        food2.setIcon(null);
                    }

                    for (JLabel food3 : merienda) {
                        food3.setIcon(null);
                    }

                    int i=0;

                    for (String alimento: mDesayuno)
                    {
                        ImageIcon icon = new ImageIcon("img/"+alimento+".jpg");
                        Image imagen = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                        desayuno[i].setIcon(new ImageIcon(imagen));
                        i++;
                    }

                    i=0;
                    for (String alimento: mComida)
                    {
                        ImageIcon icon = new ImageIcon("img/"+alimento+".jpg");
                        Image imagen = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                        comida[i].setIcon(new ImageIcon(imagen));
                        i++;
                    }

                    i=0;
                    for (String alimento: mMerienda)
                    {
                        ImageIcon icon = new ImageIcon("img/"+alimento+".jpg");
                        Image imagen = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                        merienda[i].setIcon(new ImageIcon(imagen));
                        i++;
                    }
                }
            }

            private void mostrarPrimero() {
                currentIndex = 0;
                mostrarMenuActual();
            }

            private void mostrarAnterior() {
                if (currentIndex > 0) {
                    currentIndex--;
                    mostrarMenuActual();
                }
            }

            private void mostrarSiguiente() {
                if (currentIndex < menus.size() - 1) {
                    currentIndex++;
                    mostrarMenuActual();
                }
            }

            private void mostrarUltimo() {
                currentIndex = menus.size() - 1;
                mostrarMenuActual();
            }

            private List<MenuDieta> obtenerMenusDesdeProlog(int gasto) {
                List<MenuDieta> resultados = new ArrayList<>();
                try {
                    Query cargar = new Query("consult('knowledge.pl')");
                    cargar.hasSolution();

                    String consulta = "dietas(" + gasto + ", Menus)";
                    Query q = new Query(consulta);

                    if (q.hasSolution()) {
                        Map<String, Term> sol = q.oneSolution();
                        Term menusTerm = sol.get("Menus");

                        if (menusTerm.isList()) {
                            Term[] menusArray = menusTerm.listToTermArray();
                            for (Term menu : menusArray) {
                                Term[] partes = menu.arg(1).listToTermArray();
                                List<String> mDesayuno = convertirTermLista(partes);
                                partes = menu.arg(2).listToTermArray();
                                List<String> mComida = convertirTermLista(partes);
                                partes = menu.arg(3).listToTermArray();
                                List<String> mMerienda = convertirTermLista(partes);
                                int kcal = menu.arg(4).intValue();

                                resultados.add(new MenuDieta(mDesayuno, mComida, mMerienda, kcal));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return resultados;
            }

            private List<String> convertirTermLista(Term[] terminos) {
                List<String> lista = new ArrayList<>();
                for (Term t : terminos) {
                    lista.add(t.name());
                }
                return lista;
            }
        }