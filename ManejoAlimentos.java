package alimentos;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ManejoAlimentos extends JFrame implements ActionListener, ChangeListener, ItemListener {

    private final Color[] COLORES = {Color.GREEN, Color.YELLOW, Color.ORANGE, Color.WHITE, Color.MAGENTA};
    private final String[] TIPO_ALIMENTO = {"Verduras", "Frutas", "Cereales", "Leguminosas", "Alim. de orig. animal"};
    private final int[] KCAL_TIPO_ALIM = {25, 60, 70, 120, 40}; // Kilocalorías por ración por tipo de alimento
    private JSlider[] racTipoAlim; // Fija el número de raciones por tipo de alimento
    private JLabel[] kCalTipoAlim; // Guarda las kilocalorías por tipo de alimento
    private JPanel pKcalTipoAlim[]; // Paneles para guardar las barras a desplegar
    private JLabel kilocalorias; // Para mostrar el valor de las kilocalorías
    private JButton verificar;
    private Alimentos[] alimentos;
    // Atributos para panelAlimentos()
    private final String[] TIEMPOSCOMIDA = {"Desayuno", "Comida", "Cena"};
    private JCheckBox eleccionTiempo[];
    private DefaultListModel <Alimentos> modelosListas[];
    private JList tiempos[];
    private JComboBox alimento;
    private JButton aceptar; // Para agregar un alimento
    private JLabel tipoAli;
    private int numAlimTie[];
    private JLabel numAlimTiem[];

    public ManejoAlimentos()  {
        super("Práctica 2");
        Container panel = getContentPane();
        JTabbedPane panelPrincipal = new JTabbedPane();
        panelPrincipal.addTab("Conociendo los alimentos", panelConociendo());
        panelPrincipal.addTab("Elección de alimentos", panelAlimentos());
        panelPrincipal.addTab("Clasificación de los elementos", grafico());
        panel.add(panelPrincipal);

        alimentos = new Alimentos[22];
        cargarDatos();

        this.setVisible(true);
        this.setSize(650, 355);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public JPanel panelConociendo() { // Panel de conociendo los alimentos
        JPanel conocer = new JPanel();
        conocer.setLayout(new BorderLayout());
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BorderLayout());
        JPanel panelCentro = new JPanel();
        JPanel panelSur = new JPanel();
        JPanel panelTitulo = new JPanel();
        panelTitulo.setLayout(new BorderLayout());
        JPanel panelEncabezados = new JPanel(new GridLayout(2,1));
        JLabel enc1 = new JLabel("CONOCIMIENTO DE LAS KILOCALORÍAS APORTADAS POR RACIONES POR TIPO DE ALIMENTO", SwingConstants.CENTER);
        JLabel enc2 = new JLabel("Elige el número de las raciones de cada tipo de alimento que consideres debes consumir", SwingConstants.CENTER);
        panelEncabezados.add(enc1);
        panelEncabezados.add(enc2);
        panelSuperior.add(panelEncabezados);

        pKcalTipoAlim =	new	JPanel[TIPO_ALIMENTO.length];//Atributo
        JPanel pGpoTipoAlim	= new JPanel(); // sirve para agrupar a	los	JSlider	y las JLabel (barras)
        pGpoTipoAlim.setLayout(new GridLayout(1,5)); // Te toca especificar el tipo de distribución(administrador)
        racTipoAlim	= new JSlider[TIPO_ALIMENTO.length];
        kCalTipoAlim = new JLabel[TIPO_ALIMENTO.length];
        verificar = new JButton("Verifica proporción"); // Debes declararlo como atributo
        kilocalorias = new JLabel("Total de kilocalorias: 0");
        verificar.addActionListener(this);
        panelSur.add(kilocalorias);
        panelSur.add(verificar);
        //panelSur.add(reAlimentacion);

        for (int ta=0; ta<TIPO_ALIMENTO.length; ta++) {
            JPanel pDatos = new JPanel();
            pDatos.setLayout(new BorderLayout());
            JLabel tit = new JLabel(TIPO_ALIMENTO[ta]);
            tit.setHorizontalAlignment(SwingConstants.CENTER);
            pKcalTipoAlim[ta] = new JPanel();
            racTipoAlim[ta] = new JSlider();
            racTipoAlim[ta].setOrientation(SwingConstants.VERTICAL);
            racTipoAlim[ta].setPaintLabels(true);
            racTipoAlim[ta].setPaintTicks(true);
            racTipoAlim[ta].setMinimum(0);
            racTipoAlim[ta].setMaximum(10);
            racTipoAlim[ta].setValue(0);
            racTipoAlim[ta].setMajorTickSpacing(2);
            racTipoAlim[ta].setMinorTickSpacing(1);
            racTipoAlim[ta].addChangeListener(this);
            kCalTipoAlim[ta] = new JLabel();
            kCalTipoAlim[ta].setBackground(COLORES[ta]);
            kCalTipoAlim[ta].setOpaque(true);
            kCalTipoAlim[ta].setVerticalAlignment(racTipoAlim[ta].getHeight());
            kCalTipoAlim[ta].setHorizontalAlignment(SwingConstants.HORIZONTAL);
            pKcalTipoAlim[ta].setLayout(new BorderLayout());
            pKcalTipoAlim[ta].setPreferredSize(new Dimension(60, 0));
            pKcalTipoAlim[ta].add(kCalTipoAlim[ta]);
            pDatos.add(tit, BorderLayout.NORTH);
            pDatos.add(racTipoAlim[ta], BorderLayout.WEST);
            pDatos.add(pKcalTipoAlim[ta], BorderLayout.EAST);
            pGpoTipoAlim.add(pDatos);
        }
        panelCentro.add(pGpoTipoAlim);

        conocer.add(panelSuperior, BorderLayout.NORTH);
        conocer.add(panelCentro, BorderLayout.CENTER);
        conocer.add(panelSur, BorderLayout.SOUTH);

        return conocer;
    }

    public JPanel panelAlimentos() {
        cargarDatos();
        JPanel pAlimentos = new JPanel();
        pAlimentos.setLayout(new BorderLayout());
        JPanel panelCentro = new JPanel(); // Para la selección de alimentos y cuadros de verificación
        JPanel panelCheck = new JPanel(); // Para cuadros de verificación de tiempos de comidas
        panelCheck.setLayout(new GridLayout(1, 0));
        panelCentro.setLayout(new BorderLayout());
        JPanel panelSeleccion = new JPanel(); // Para la selección de los alimentos
        JLabel titulo = new JLabel("ELECCIÓN DE ALIMENTOS Y ASIGNACIÓN A UN TIEMPO PARA SU CONSUMO");
        titulo.setHorizontalAlignment(JLabel.CENTER);
        JLabel tAlimento = new JLabel("Elige el alimento");
        //aceptar = new JButton("Aceptar");
        tipoAli = new JLabel(" ");
        alimento = new JComboBox(alimentos); // Creación del cuadro combinado
        alimento.addItemListener(this);
        tipoAli.setText(TIPO_ALIMENTO[0]); // Se muestra el primero

        JPanel panelTiempos = new JPanel();
        panelTiempos.add(new JPanel());
        panelSeleccion.add(tAlimento);
        panelSeleccion.add(alimento);
        panelSeleccion.add(tipoAli);
        panelCentro.add(panelSeleccion, BorderLayout.NORTH);
        panelCentro.add(creaPanelTiempos(), BorderLayout.CENTER);
        pAlimentos.add(titulo, BorderLayout.NORTH);
        pAlimentos.add(panelCentro, BorderLayout.CENTER);
        panelCentro.add(creaListas(), BorderLayout.SOUTH);

        return pAlimentos;
    }

    public JPanel grafico() {
        JPanel graph = new JPanel();
        return graph;
    }

    private void cargarDatos() {
        alimentos = new Alimentos[22];
        alimentos[0] = new Alimentos("Zanahoria", 0);
        alimentos[1] = new Alimentos("Lechuga", 0);
        alimentos[2] = new Alimentos("Espinaca", 0);
        alimentos[3] = new Alimentos("Acelgas", 0);
        alimentos[4] = new Alimentos("Tomate", 0);
        alimentos[5] = new Alimentos("Plátano", 1);
        alimentos[6] = new Alimentos("Naranja", 1);
        alimentos[7] = new Alimentos("Fresa", 1);
        alimentos[8] = new Alimentos("Manzana", 1);
        alimentos[9] = new Alimentos("Durazno", 1);
        alimentos[10] = new Alimentos("Trigo", 2);
        alimentos[11] = new Alimentos("Arroz", 2);
        alimentos[12] = new Alimentos("Avena", 2);
        alimentos[13] = new Alimentos("Maíz", 2);
        alimentos[14] = new Alimentos("Garbanzos", 3);
        alimentos[15] = new Alimentos("Habas", 3);
        alimentos[16] = new Alimentos("Lentejas", 3);
        alimentos[17] = new Alimentos("Soja", 3);
        alimentos[18] = new Alimentos("Carne", 4);
        alimentos[19] = new Alimentos("Pescados y mariscos", 4);
        alimentos[20] = new Alimentos("Huevos", 4);
        alimentos[21] = new Alimentos("Queso", 4);
    }

    public JPanel creaPanelTiempos() {
        JPanel tiemposAlimento = new JPanel();
        tiemposAlimento.setLayout(new BorderLayout());
        JLabel indicacion = new JLabel("Selecciona el tiempo cuando desees consumirlo");
        indicacion.setHorizontalAlignment(JLabel.CENTER);
        tiemposAlimento.add(indicacion, BorderLayout.NORTH);
        JPanel eleccion = new JPanel();
        aceptar = new JButton("Agregar");
        aceptar.addActionListener(this);
        eleccionTiempo = new JCheckBox[TIEMPOSCOMIDA.length+1];
        numAlimTie = new int[TIEMPOSCOMIDA.length+1];
        eleccion.setLayout(new GridLayout(1, 3));
        for (int t=0; t<eleccionTiempo.length-1; t++) {
            eleccionTiempo[t] = new JCheckBox(TIEMPOSCOMIDA[t]);
            eleccion.add(eleccionTiempo[t]);
            numAlimTie[t]=0;
        }
        eleccionTiempo[eleccionTiempo.length-1] = new JCheckBox("En todos");
        eleccion.add(eleccionTiempo[eleccionTiempo.length-1]);
        tiemposAlimento.add(eleccion, BorderLayout.NORTH);
        tiemposAlimento.add(aceptar, BorderLayout.EAST);

        return tiemposAlimento;
    }

    public JPanel creaListas() {
        JPanel pListas = new JPanel();
        JPanel listas = new JPanel();
        JPanel numAlim = new JPanel();
        tiempos = new JList[TIEMPOSCOMIDA.length];
        numAlimTiem = new JLabel[TIEMPOSCOMIDA.length];
        modelosListas = new DefaultListModel[TIEMPOSCOMIDA.length];
        GridLayout dList = new GridLayout(0, TIEMPOSCOMIDA.length+1, 5, 0);
        pListas.setLayout(new BorderLayout());
        listas.setLayout(dList);
        numAlim.setLayout(dList);
        for (int t=0; t<tiempos.length; t++) {
            modelosListas[t] = new DefaultListModel();
            tiempos[t] = new JList(modelosListas[t]);
            listas.add(new JScrollPane(tiempos[t]));
        }
        for (int t=0; t<tiempos.length; t++) {
            numAlimTiem[t] = new JLabel("Num.Alim.: 0");
            numAlim.add(numAlimTiem[t]);
        }

        pListas.add(listas, BorderLayout.NORTH);
        pListas.add(numAlim, BorderLayout.SOUTH);

        return pListas;
    }

    public class Alimentos {
        private String alimento; // Nombre del alimento
        private int tipo; // Identificación del tipo de alimento

        public Alimentos (String alimento, int tipo) {
            this.alimento = alimento;
            this.tipo = tipo;
        }

        public int tipo() {
            return tipo;
        }

        public String Alimento() {
            return  alimento;
        }

        @Override
        public String toString() {
            return alimento;
        }

        public boolean equals(Alimentos ali) {
            return alimento.equals(ali.alimento) && tipo == ali.tipo;
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Object prod = ae.getSource(); // Productor del evento
        long[] numTipoAli = new long[TIPO_ALIMENTO.length-1];
        if (prod==verificar) {
            String mensaje = "Las verduras sin restricción de cantidad\n" +
                             "Las frutas sin restricción de cantidad con variedad de colores\n" +
                             "Los cereales deben ser suficientes\n" +
                             "Las leguminosas deben ser suficientes combinadas con cereales\n" +
                             "Productos animales deben ser consumidos en poca cantidad.";
            JOptionPane.showMessageDialog(this, mensaje);
        }

        if (prod==aceptar) {
            Alimentos aliSel = (Alimentos) alimento.getSelectedItem();
            for (int t=0; t<eleccionTiempo.length-1; t++) {
                if (eleccionTiempo[t].isSelected()) {
                    modelosListas[t].addElement(aliSel);
                    numAlimTiem[t].setText("Num.Alim.:"+(++numAlimTie[t]));
                    numTipoAli[aliSel.tipo()]++;
                }
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        int totalKc = 0;
        for (int ta=0; ta<COLORES.length; ta++) {
            int base = (racTipoAlim[ta].getHeight()-racTipoAlim[ta].getValue()* KCAL_TIPO_ALIM[ta])*racTipoAlim[ta].getHeight()/racTipoAlim[ta].getHeight();
            kCalTipoAlim[ta].setBounds(0, base, 60, racTipoAlim[ta].getHeight()*racTipoAlim[ta].getValue()* KCAL_TIPO_ALIM[ta]/racTipoAlim[ta].getHeight());
            kCalTipoAlim[ta].setText(""+racTipoAlim[ta].getValue()*KCAL_TIPO_ALIM[ta]+" - KC");
            totalKc += racTipoAlim[ta].getValue()*KCAL_TIPO_ALIM[ta];
        }
        kilocalorias.setText("Total KC: "+totalKc+" ");
        this.repaint();
    }

    @Override
    public void itemStateChanged(ItemEvent ie) {
        JComboBox aliSel = (JComboBox)ie.getSource();
        int t=((Alimentos)aliSel.getSelectedItem()).tipo();
        tipoAli.setText(""+TIPO_ALIMENTO[t]);
        //Actualiza elección del tiempo de comida
        boolean seleccion = true;
        for (int i=0; i< eleccionTiempo.length-1; i++) {
            //seleccion = true && eleccionTiempo[t].isSelected();
            eleccionTiempo[eleccionTiempo.length-1].setSelected(seleccion);
        }
    }

    public static void main(String[] args) {
        new ManejoAlimentos();
    }
}
