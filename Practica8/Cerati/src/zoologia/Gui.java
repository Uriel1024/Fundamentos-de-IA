/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

/**
 *
 * @author 
 */
public class Gui extends JFrame 
{
    private Query q1;
    private String[] clases;
    private String[] properties;
    private JList listaClases;
    private JList listaPropiedades;
    private final DefaultListModel listModel1 = new DefaultListModel();
    private final DefaultListModel listModel2 = new DefaultListModel();
    private final JTextArea displayArea = new JTextArea();
    private final JTextArea descripcionArea = new JTextArea();
    private final JLabel imagen = new JLabel();
    
    
    public Gui()
    {
        super("Zoológico");  
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        int width = pantalla.width;
        int height = pantalla.height;
        this.setBounds((width-865)/2,(height-600)/2,865,610);
        initComponents();
    }
    
    private void initComponents()
    {
        JTabbedPane tabPan = new JTabbedPane();
        tabPan.setBounds(0,0,855,580);
        this.add(tabPan);
        JPanel tab01 = new JPanel();
        JPanel tab02 = new JPanel();
        JPanel tab03 = new JPanel();
        
        tabPan.add("Consulta Clases",tab01);
        tabPan.add("Búsca por propiedades", tab02);
        tabPan.add("Árbol taxonómico", tab03);
        
        cargaConocimiento("engine.pl");
        cargaConocimiento("knowledge.pl");
        
        // Panel de Consulta Clases

        clases = consultaClases();
        for(int i=0;i<clases.length;i++)
            listModel1.addElement((clases[i]).trim());
        listaClases = new JList(listModel1);

        
        //Panel para consulta de clases
        consultaClasesPanel(tab01);

        listaClases.addListSelectionListener(   evt -> gestionaClases(evt));
        listaClases.setSelectedIndex(0);


        // Panel de Búsqueda por propiedades
        buscarPorPropiedades(tab02);

        // Panel del árbol taxonómico

        arbolTaxonomico(tab03);

        // Handle the X-Button 
        class MyWindowAdapter extends WindowAdapter
        {
            @Override
            public void windowClosing(WindowEvent eventObject)
            {
		goodBye();
            }
        }
        addWindowListener(new MyWindowAdapter());  
        
    }

    private void consultaClasesPanel(JPanel tab01){

        JLabel et01 = new JLabel("Clases:");
        JLabel et02 = new JLabel("Propiedades:");
        JLabel et03 = new JLabel("Descripción");
        JScrollPane spClases = new JScrollPane(listaClases);
        JScrollPane spProps  = new JScrollPane(displayArea);
        JScrollPane spDesc   = new JScrollPane(descripcionArea);


        tab01.setLayout(null);
        displayArea.setEditable(false);
        descripcionArea.setEditable(false);
        et01.setBounds(10,10,300,20);
        et02.setBounds(10,245,300,20);
        et03.setBounds(10,430,300,20);
        et01.setFont(new Font("Arial",Font.BOLD,16));
        et02.setFont(new Font("Arial",Font.BOLD,16));
        et03.setFont(new Font("Arial",Font.BOLD,16));
        spClases.setBounds(10,30,300,200);
        listaClases.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        listaClases.setFont(new Font("Consolas",Font.BOLD,18));
        spProps.setBounds(10,265,300,150);
        displayArea.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        displayArea.setFont(new Font("Arial",Font.PLAIN,16));
        spDesc.setBounds(10,450,300,85);
        descripcionArea.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        descripcionArea.setFont(new Font("Arial",Font.PLAIN,16));
        descripcionArea.setLineWrap(true);
        descripcionArea.setWrapStyleWord(true);
        imagen.setBounds(330,30,500,500);
        imagen.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        tab01.add(et01);
        tab01.add(et02);
        tab01.add(et03);
        tab01.add(spClases);
        tab01.add(spProps);
        tab01.add(spDesc);
        tab01.add(imagen);
    }

    private void buscarPorPropiedades(JPanel tab02){
        properties = consultaTodasPropiedades();
        for(int i=0;i<properties.length;i++)
            listModel2.addElement((properties[i]).trim());
        listaPropiedades = new JList(listModel2);

        JScrollPane spTodas = new JScrollPane(listaPropiedades);

        JLabel et04 = new JLabel("Todas las Propiedades");

        tab02.setLayout(null);
        et04.setBounds(10,10,300,20);
        et04.setFont(new Font("Arial",Font.BOLD,16));
        spTodas.setBounds(10,30,400,500);
        listaPropiedades.setFont(new Font("Consolas",Font.BOLD,18));
        listaPropiedades.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

        tab02.add(et04);
        tab02.add(spTodas);

        listaPropiedades.addListSelectionListener(evt -> gestionaPropiedades(evt));
        listaPropiedades.setSelectedIndex(0);


    }

    private void arbolTaxonomico(JPanel tab03){
        tab03.setLayout(null);
        JLabel arbol = new JLabel();
        arbol.setIcon(new ImageIcon("imagenes/arbol.jpg"));
        JScrollPane spArbol = new JScrollPane(arbol);
        spArbol.setBounds(0,0,840,550);
        tab03.add(spArbol);


    }
    
    private void gestionaClases(ListSelectionEvent ev)
    {
        String selectedValue = (String) listaClases.getSelectedValue();
        String t = "";
   
        if(!selectedValue.trim().equals("top"))
        {
            String[] superclases = consultaSuperclases(selectedValue);
            for(int i=0;i<superclases.length;i++)
                if(!superclases[i].trim().equals("top"))
                    t+="es("+superclases[i].trim()+")\n";
        }
        
        String[] propiedades = consultaPropiedades(selectedValue);
        for(int i=0;i<propiedades.length;i++) 
            t+=propiedades[i].trim()+"\n";
  
        displayArea.setText(t);
        
        String str = obtieneDescripcion(selectedValue);
        str = str.substring(1,str.length()-1);      
        descripcionArea.setText(str);
        
        imagen.setIcon(new ImageIcon("imagenes/"+selectedValue + ".jpg"));
    }
    
    private void gestionaPropiedades(ListSelectionEvent ev)
    {
        // Debe agregar su código, para gestionat el evento 
        // Haga una consulta a SW-Prolog con la regla:
        // tiene_propiedad(P,L), 
        // donde P es la propiedad y L es la lista de clases que la tienen
        
        String selectedValue = (String) listaPropiedades.getSelectedValue();
        // System.out.println(selectedValue);
        
    }
            
    private String[] consultaClases()
    {
        String consulta = "clases(L)";
        String[] resultado = null;
        q1 = new Query(consulta);
        if(q1.hasSolution())
        {
           String respuesta = "";
           Map soluciones = q1.nextSolution();
           respuesta+=soluciones.get("L");
           resultado = respuesta.substring(respuesta.indexOf("[")+1, respuesta.indexOf("]")).split(",");
        }
        return resultado;
    }
   
    private String[] consultaPropiedades(String obj)
    {
        String consulta = "propiedadesc("+ obj+ ", L)";
        //System.out.println(consulta);
        String[] resultado = null;
        q1 = new Query(consulta);
        if(q1.hasSolution())
        {
           String respuesta = "";
           Map soluciones = q1.nextSolution();
           respuesta+=soluciones.get("L");
           resultado = respuesta.substring(respuesta.indexOf("[")+1, respuesta.indexOf("]")).split(",");
        }
        return resultado;        
    }
    
    private String[] consultaSuperclases(String clase)
    {
        String consulta = "superclases_de("+ clase + ", L)";
        //System.out.println(consulta);
        String[] resultado = null;
        q1 = new Query(consulta);
        if(q1.hasSolution())
        {
           String respuesta = "";
           Map soluciones = q1.nextSolution();
           respuesta+=soluciones.get("L");
           resultado = respuesta.substring(respuesta.indexOf("[")+1, respuesta.indexOf("]")).split(",");
        }
        return resultado;      
    }
   
    private String[] consultaTodasPropiedades()
    {
        String consulta = "todas_propiedades(L)";
        String[] resultado = null;
        q1 = new Query(consulta);
        if(q1.hasSolution())
        {
           String respuesta = "";
           Map soluciones = q1.nextSolution();
           respuesta+=soluciones.get("L");
           resultado = respuesta.substring(respuesta.indexOf("[")+1, respuesta.indexOf("]")).split(",");
        }
        return resultado;        
    }

    private String obtieneDescripcion(String clase)
    {
        String consulta = "obtiene_descripcion(" + clase + ",L)";
        String resultado = "";
        q1 = new Query(consulta);
        if(q1.hasSolution())
        {
           Map soluciones = q1.nextSolution();
           resultado+=soluciones.get("L");
        }
        return resultado;        
    }    
    
    private void cargaConocimiento(String archivo)
    {
        String consulta = "consult('" + "prolog/" + archivo + "')";
        q1 = new Query(consulta); // Se general el Query con el String de consulta
        System.out.println( consulta + " " + (q1.hasSolution() ? "succeeded" : "failed") ); // se realiza la consulta               
    }
    
    private void goodBye()
    {
        int respuesta = JOptionPane.showConfirmDialog(rootPane, "Are you sure?","Exit",JOptionPane.YES_NO_OPTION);
        if(respuesta==JOptionPane.YES_OPTION) System.exit(0);
    }
}
