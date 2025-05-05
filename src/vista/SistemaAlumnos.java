package vista;
import controlador.AlumnoDAO;
import modelo.Alumno;
import modelo.ResultSetTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

//el .getRow = devuelve el número de fila actual de un conjunto de resultados (es por eso del ResultSetTableModel)

public class SistemaAlumnos extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // altas
    private JTextField txtNumeroControlAlta, txtNombreAlta, txtPrimerApellidoAlta, txtSegundoApellidoAlta;
    private JComboBox<String> comboSemestreAlta, comboCarreraAlta;
    private DefaultTableModel tableModelAltas;
    private JTable tablaAltas;

    // bajas
    private JTextField txtNumeroControlBaja, txtNombreBaja, txtPrimerApellidoBaja, txtSegundoApellidoBaja;
    private JComboBox<String> comboSemestreBaja, comboCarreraBaja;
    private DefaultTableModel tableModelBajas;
    private JTable tablaBajas;

    // cambios
    private JTextField txtNumeroControlCambio, txtNombreCambio, txtPrimerApellidoCambio, txtSegundoApellidoCambio;
    private JComboBox<String> comboSemestreCambio, comboCarreraCambio;
    private DefaultTableModel tableModelCambios;
    private JTable tablaCambios;

    // consultas
    private JTextField txtNumeroControlConsulta, txtNombreConsulta, txtPrimerApellidoConsulta, txtSegundoApellidoConsulta;
    private JComboBox<String> comboSemestreConsulta, comboCarreraConsulta;
    private DefaultTableModel tableModelConsultas;
    private JTable tablaConsultas;
    private JPanel panelConsultas, navegacionPanelConsultas;
    private int indiceActual = 0;
    private List<Alumno> listaConsultaResultados;
    private JTextField txtIndice;
    private JButton btnPrimero, btnAnterior, btnSiguiente, btnUltimo;
    private JButton btnBuscar;
    private JSpinner spinnerSemestreConsultaCampo;

    private AlumnoDAO alumnoDAO;

    //Metodos propis
    public void actualizarTabla(JTable tabla){
        final String CONTROLADOR_JDBC = "com.mysql.cj.jdbc.Driver";
        final String URL = "jdbc:mysql://localhost:3306/bd_topicos_2025"; //ruta
        final String CONSULTA = "SELECT * FROM alumnos"; //consultas

        try {
            ResultSetTableModel modelo = new ResultSetTableModel(CONTROLADOR_JDBC, URL, CONSULTA);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }//try-catch

    }//actualizarTabla

    //constructor
    public SistemaAlumnos() {

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        alumnoDAO = new AlumnoDAO();

        setTitle("Sistema de Gestión de Alumnos (Altas, Bajas, Cambios y Consultas) ");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menuOpciones = new JMenu("ALUMNOS");
        menuBar.add(menuOpciones);

        // items menu
        JMenuItem itemMenuPrincipal = new JMenuItem("Menú Principal");
        JMenuItem itemAltas = new JMenuItem("Altas");
        JMenuItem itemBajas = new JMenuItem("Bajas");
        JMenuItem itemCambios = new JMenuItem("Cambios");
        JMenuItem itemConsultas = new JMenuItem("Consultas");
        JMenuItem itemSalir = new JMenuItem("Salir");

        // agregar los items al menu
        menuOpciones.add(itemMenuPrincipal);
        menuOpciones.add(itemAltas);
        menuOpciones.add(itemBajas);
        menuOpciones.add(itemCambios);
        menuOpciones.add(itemConsultas);
        menuOpciones.addSeparator();
        menuOpciones.add(itemSalir);

        setJMenuBar(menuBar); // agregar el menu a la ventana
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // creación los paneles para cada vista
        JPanel menuPanel = crearPanelMenu();
        JPanel altasPanel = crearPanelAltas();
        JPanel bajasPanel = crearPanelBajas();
        JPanel cambiosPanel = crearPanelCambios();
        panelConsultas = crearPanelConsultas();

        // agregar los paneles al CardLayout
        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(altasPanel, "Altas");
        mainPanel.add(bajasPanel, "Bajas");
        mainPanel.add(cambiosPanel, "Cambios");
        mainPanel.add(panelConsultas, "Consultas");

        // agregar el panel principal a la ventana
        getContentPane().add(mainPanel);

        // agregar los ActionListener
        itemMenuPrincipal.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        itemAltas.addActionListener(e -> {
            java.util.ArrayList<Alumno> alumnos = alumnoDAO.mostrarAlumnos(null);
            tableModelAltas.setRowCount(0); // Limpiar la tabla pa que no se dupliquen
            for (Alumno alumno : alumnos) {
                tableModelAltas.addRow(new Object[]{
                        alumno.getNumControl(),
                        alumno.getNombre(),
                        alumno.getPrimerAp(),
                        alumno.getSegundoAp(),
                        alumno.getSemestre(),
                        alumno.getCarrera()
                });
            }
            cardLayout.show(mainPanel, "Altas");
        });

        //itemAltas.addActionListener(e -> cardLayout.show(mainPanel, "Altas"));
        itemBajas.addActionListener(e -> cardLayout.show(mainPanel, "Bajas"));
        itemCambios.addActionListener(e -> cardLayout.show(mainPanel, "Cambios"));
        itemConsultas.addActionListener(e -> cardLayout.show(mainPanel, "Consultas"));
        itemSalir.addActionListener(e -> System.exit(0));
    }//constructor

    // menu principal
    private JPanel crearPanelMenu() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel lblBienvenida = new JLabel("Sistema de alumnos, ABCC ", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 24));
        panel.setBackground(Color.GRAY);
        panel.add(lblBienvenida, BorderLayout.CENTER);
        return panel;
    }//crearPanelMenu

    //==================================================================================================================================================================================================================================

    // altas
    private JPanel crearPanelAltas() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("ALTAS ALUMNOS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(Color.GREEN);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("NÚMERO DE CONTROL"), gbc);
        txtNumeroControlAlta = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtNumeroControlAlta, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("NOMBRE"), gbc);
        txtNombreAlta = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtNombreAlta, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("PRIMER APELLIDO"), gbc);
        txtPrimerApellidoAlta = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtPrimerApellidoAlta, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("SEGUNDO APELLIDO"), gbc);
        txtSegundoApellidoAlta = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtSegundoApellidoAlta, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("SEMESTRE"), gbc);
        String[] semestres = {"Elige Semestre...", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        comboSemestreAlta = new JComboBox<>(semestres);
        gbc.gridx = 1;
        formPanel.add(comboSemestreAlta, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("CARRERA"), gbc);
        String[] carreras = {"Elige Carrera...", "Ingeniería en Sistemas Computacionales", "Ingeniería Mecatronica", "Administración", "Contabilidad", "Ingenieria en Industrias Alimentarias"};
        comboCarreraAlta = new JComboBox<>(carreras);
        gbc.gridx = 1;
        formPanel.add(comboCarreraAlta, gbc);

        JButton btnAgregar = new JButton("AGREGAR");
        JButton btnBorrar = new JButton("BORRAR");
        JButton btnCancelar = new JButton("CANCELAR");

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnAgregar);
        panelBotones.add(btnBorrar);
        panelBotones.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        formPanel.add(panelBotones, gbc);

        // crear tablita para altas
        String[] columnas = {"Núm. Control", "Nombre", "Primer Ap.", "Segundo Ap.", "Semestre", "Carrera"};
        tableModelAltas = new DefaultTableModel(columnas, 0);
        tablaAltas = new JTable(tableModelAltas);
        JScrollPane scrollPane = new JScrollPane(tablaAltas);

        // agreagr al panel principal
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        //instanciar el DAO
        AlumnoDAO alumnoDAO = new AlumnoDAO();

        btnAgregar.addActionListener(e -> {
            String numControl = txtNumeroControlAlta.getText();
            String nombre = txtNombreAlta.getText();
            String primerAp = txtPrimerApellidoAlta.getText();
            String segundoAp = txtSegundoApellidoAlta.getText();
            String semestreStr = (String) comboSemestreAlta.getSelectedItem();
            String carrera = (String) comboCarreraAlta.getSelectedItem();

            //para que se completen los campos, como tipo obligatorio
            if (numControl.isEmpty() || nombre.isEmpty() || primerAp.isEmpty() || segundoAp.isEmpty() ||
                    semestreStr.equals("Elige Semestre...") || carrera.equals("Elige Carrera...")) {
                JOptionPane.showMessageDialog(null, "Por favor, completa todos los campos.");
                return;
            }//if

            try {
                byte semestre = Byte.parseByte(semestreStr); // convierte el String del combo a byte
                Alumno nuevoAlumno = new Alumno(numControl, nombre, primerAp, segundoAp, (byte) 0, semestre, carrera);

                if (alumnoDAO.agregarAlumno(nuevoAlumno)) {
                    JOptionPane.showMessageDialog(null, "Alumno agregado correctamente a la base de datos");
                    tableModelAltas.addRow(new Object[]{nuevoAlumno.getNumControl(), nuevoAlumno.getNombre(), nuevoAlumno.getPrimerAp(), nuevoAlumno.getSegundoAp(), nuevoAlumno.getSemestre(), nuevoAlumno.getCarrera()});
                    txtNumeroControlAlta.setText("");
                    txtNombreAlta.setText("");
                    txtPrimerApellidoAlta.setText("");
                    txtSegundoApellidoAlta.setText("");
                    comboSemestreAlta.setSelectedIndex(0);
                    comboCarreraAlta.setSelectedIndex(0);
                } else {
                    JOptionPane.showMessageDialog(null, "Error al agregar el alumno a la base de datos");
                }//if-else

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "El semestre debe ser un número");
            }//try-catch
        });

        //borrar lo quie hay en los campos
        btnBorrar.addActionListener(e -> {
            txtNumeroControlAlta.setText("");
            txtNombreAlta.setText("");
            txtPrimerApellidoAlta.setText("");
            txtSegundoApellidoAlta.setText("");
            comboSemestreAlta.setSelectedIndex(0);
            comboCarreraAlta.setSelectedIndex(0);
        });

        btnCancelar.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        return panel;
    }//crearPanelAltas

    //==================================================================================================================================================================================================================================

    // bajas
    private JPanel crearPanelBajas() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("BAJAS ALUMNOS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(Color.RED);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("NÚMERO DE CONTROL"), gbc);
        txtNumeroControlBaja = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtNumeroControlBaja, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("NOMBRE"), gbc);
        txtNombreBaja = new JTextField(20);
        txtNombreBaja.setEditable(false);
        gbc.gridx = 1;
        formPanel.add(txtNombreBaja, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("PRIMER APELLIDO"), gbc);
        txtPrimerApellidoBaja = new JTextField(20);
        txtPrimerApellidoBaja.setEditable(false);
        gbc.gridx = 1;
        formPanel.add(txtPrimerApellidoBaja, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("SEGUNDO APELLIDO"), gbc);
        txtSegundoApellidoBaja = new JTextField(20);
        txtSegundoApellidoBaja.setEditable(false);
        gbc.gridx = 1;
        formPanel.add(txtSegundoApellidoBaja, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("SEMESTRE"), gbc);
        String[] semestres = {"Elige Semestre...", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        comboSemestreBaja = new JComboBox<>(semestres);
        comboSemestreBaja.setEnabled(false);
        gbc.gridx = 1;
        formPanel.add(comboSemestreBaja, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("CARRERA"), gbc);
        String[] carreras = {"Elige Carrera...", "Ingeniería en Sistemas Computacionales", "Ingeniería Mecatronica", "Administración", "Contabilidad", "Ingenieria en Industrias Alimentarias"};
        comboCarreraBaja = new JComboBox<>(carreras);
        comboCarreraBaja.setEnabled(false);
        gbc.gridx = 1;
        formPanel.add(comboCarreraBaja, gbc);

        JButton btnBuscar = new JButton("BUSCAR");
        JButton btnBorrar = new JButton("BORRAR");
        JButton btnCancelar = new JButton("CANCELAR");

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnBuscar);
        panelBotones.add(btnBorrar);
        panelBotones.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        formPanel.add(panelBotones, gbc);

        // crear la tabla para Bajas
        String[] columnas = {"Núm. Control", "Nombre", "Primer Ap.", "Segundo Ap.", "Semestre", "Carrera"};
        tableModelBajas = new DefaultTableModel(columnas, 0);
        tablaBajas = new JTable(tableModelBajas);
        JScrollPane scrollPane = new JScrollPane(tablaBajas);

        // agregar el formulario y la tabla al panel principal
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> {
            String numControl = txtNumeroControlBaja.getText();
            if (numControl.isEmpty()) { //si o si agregar el numero sde control para buscar
                JOptionPane.showMessageDialog(null, "Por favor, ingresa el número de control");
                return;
            }//if

            // buscar en la tabla de Altas
            for (int i = 0; i < tableModelAltas.getRowCount(); i++) {
                if (tableModelAltas.getValueAt(i, 0).equals(numControl)) {
                    txtNombreBaja.setText((String) tableModelAltas.getValueAt(i, 1));
                    txtPrimerApellidoBaja.setText((String) tableModelAltas.getValueAt(i, 2));
                    txtSegundoApellidoBaja.setText((String) tableModelAltas.getValueAt(i, 3));
                    comboSemestreBaja.setSelectedItem(tableModelAltas.getValueAt(i, 4));
                    comboCarreraBaja.setSelectedItem(tableModelAltas.getValueAt(i, 5));
                    return;
                }//if-else
            }//for
            JOptionPane.showMessageDialog(null, "No se encontró un alumno con ese número de control");
        });

        AlumnoDAO alumnoDAO = new AlumnoDAO(); //se obtiene una instancia del dao

        btnBorrar.addActionListener(e -> {
            String numControl = txtNumeroControlBaja.getText();
            if (numControl.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, ingresa el número de control");
                return;
            }//if

            //mensaje de eliminadcion
            if (alumnoDAO.eliminarAlumno(numControl)) {
                JOptionPane.showMessageDialog(null, "El Alumno con número de control " + numControl + " ha sido eliminado correctamente de la base de datos.");

                // buscar y eliminar
                for (int i = 0; i < tableModelAltas.getRowCount(); i++) {
                    if (tableModelAltas.getValueAt(i, 0).equals(numControl)) {
                        Object[] rowData = new Object[6];
                        for (int j = 0; j < 6; j++) {
                            rowData[j] = tableModelAltas.getValueAt(i, j);
                        }//for
                        tableModelBajas.addRow(rowData); // agregar a la tabla de Bajas
                        tableModelAltas.removeRow(i); // eliminar de la tabla de Altas
                        break; // como ya los encontramos y eliminamos, ya podemos salir de ahi
                    }//if
                }//for

                //limpiar campos
                txtNumeroControlBaja.setText("");
                txtNombreBaja.setText("");
                txtPrimerApellidoBaja.setText("");
                txtSegundoApellidoBaja.setText("");
                comboSemestreBaja.setSelectedIndex(0);
                comboCarreraBaja.setSelectedIndex(0);

            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar el alumno con número de control " + numControl + " de la base de datos.");
            }//if-else
        });

        btnCancelar.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        return panel;
    }//crearPanelBajas

    //==================================================================================================================================================================================================================================

    // cambios
    private JPanel crearPanelCambios() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("CAMBIOS ALUMNOS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(Color.ORANGE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("NÚMERO DE CONTROL"), gbc);
        txtNumeroControlCambio = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtNumeroControlCambio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("NOMBRE"), gbc);
        txtNombreCambio = new JTextField(20);
        txtNombreCambio.setEditable(false);
        gbc.gridx = 1;
        formPanel.add(txtNombreCambio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("PRIMER APELLIDO"), gbc);
        txtPrimerApellidoCambio = new JTextField(20);
        txtPrimerApellidoCambio.setEditable(false);
        gbc.gridx = 1;
        formPanel.add(txtPrimerApellidoCambio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("SEGUNDO APELLIDO"), gbc);
        txtSegundoApellidoCambio = new JTextField(20);
        txtSegundoApellidoCambio.setEditable(false);
        gbc.gridx = 1;
        formPanel.add(txtSegundoApellidoCambio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("SEMESTRE"), gbc);
        String[] semestres = {"Elige Semestre...", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        comboSemestreCambio = new JComboBox<>(semestres);
        comboSemestreCambio.setEnabled(false);
        gbc.gridx = 1;
        formPanel.add(comboSemestreCambio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("CARRERA"), gbc);
        String[] carreras = {"Elige Carrera...", "Ingeniería en Sistemas Computacionales", "Ingeniería Mecatronica", "Administración", "Contabilidad", "Ingenieria en Industrias Alimentarias"};
        comboCarreraCambio = new JComboBox<>(carreras);
        comboCarreraCambio.setEnabled(false);
        gbc.gridx = 1;
        formPanel.add(comboCarreraCambio, gbc);

        JButton btnBuscar = new JButton("BUSCAR");
        JButton btnGuardar = new JButton("GUARDAR");
        JButton btnCancelar = new JButton("CANCELAR");

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnBuscar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        formPanel.add(panelBotones, gbc);

        // crear la tabla para Cambios
        String[] columnas = {"Núm. Control", "Nombre", "Primer Ap.", "Segundo Ap.", "Semestre", "Carrera"};
        tableModelCambios = new DefaultTableModel(columnas, 0);
        tablaCambios = new JTable(tableModelCambios);
        JScrollPane scrollPane = new JScrollPane(tablaCambios);

        // agregar el formulario y la tabla al panel principal
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> {
            String numControl = txtNumeroControlCambio.getText();
            if (numControl.isEmpty()) { //si o si agregar el numero de control
                JOptionPane.showMessageDialog(null, "Por favor, ingresa el número de control");
                return;
            }//if

            // buscar en la tabla de Altas
            for (int i = 0; i < tableModelAltas.getRowCount(); i++) {
                if (tableModelAltas.getValueAt(i, 0).equals(numControl)) {
                    txtNombreCambio.setText((String) tableModelAltas.getValueAt(i, 1));
                    txtPrimerApellidoCambio.setText((String) tableModelAltas.getValueAt(i, 2));
                    txtSegundoApellidoCambio.setText((String) tableModelAltas.getValueAt(i, 3));
                    comboSemestreCambio.setSelectedItem(tableModelAltas.getValueAt(i, 4));
                    comboCarreraCambio.setSelectedItem(tableModelAltas.getValueAt(i, 5));
                    txtNombreCambio.setEditable(true);
                    txtPrimerApellidoCambio.setEditable(true);
                    txtSegundoApellidoCambio.setEditable(true);
                    comboSemestreCambio.setEnabled(true);
                    comboCarreraCambio.setEnabled(true);
                    return;
                }//if
            }//for
            JOptionPane.showMessageDialog(null, "No se encontró un alumno con ese número de control");
        });

        //instanciar el DAO
        AlumnoDAO alumnoDAO = new AlumnoDAO();

        //guardar cambios
        btnGuardar.addActionListener(e -> {
            String numControl = txtNumeroControlCambio.getText();
            String nombre = txtNombreCambio.getText();
            String primerAp = txtPrimerApellidoCambio.getText();
            String segundoAp = txtSegundoApellidoCambio.getText();
            String semestreStr = (String) comboSemestreCambio.getSelectedItem();
            String carrera = (String) comboCarreraCambio.getSelectedItem();

            if (numControl.isEmpty() || nombre.isEmpty() || primerAp.isEmpty() || segundoAp.isEmpty() ||
                    semestreStr.equals("Elige Semestre...") || carrera.equals("Elige Carrera...")) {
                JOptionPane.showMessageDialog(null, "Por favor, completa todos los campos");
                return;
            }//if

            try {
                byte semestre = Byte.parseByte(semestreStr);
                Alumno alumnoActualizado = new Alumno(numControl, nombre, primerAp, segundoAp, (byte) 0, semestre, carrera); //la edad la puse siempte de 0

                //mensaje de confirmacion de cmabios
                if (alumnoDAO.editarAlumno(alumnoActualizado)) {
                    JOptionPane.showMessageDialog(null, "Los datos del alumno con número de control " + numControl + " han sido actualizados correctamente en la base de datos");

                    //se actualiza en la tabla de altas y se agrega a la tabla de cambios
                    for (int i = 0; i < tableModelAltas.getRowCount(); i++) {
                        if (tableModelAltas.getValueAt(i, 0).equals(numControl)) {
                            tableModelAltas.setValueAt(alumnoActualizado.getNombre(), i, 1);
                            tableModelAltas.setValueAt(alumnoActualizado.getPrimerAp(), i, 2);
                            tableModelAltas.setValueAt(alumnoActualizado.getSegundoAp(), i, 3);
                            tableModelAltas.setValueAt(alumnoActualizado.getSemestre(), i, 4);
                            tableModelAltas.setValueAt(alumnoActualizado.getCarrera(), i, 5);
                            tableModelCambios.addRow(new Object[]{alumnoActualizado.getNumControl(), alumnoActualizado.getNombre(), alumnoActualizado.getPrimerAp(), alumnoActualizado.getSegundoAp(), alumnoActualizado.getSemestre(), alumnoActualizado.getCarrera()});
                            break; // como ya lo encontramos y actualizamos, podemos salir del bucle
                        }//if
                    }//for

                    txtNumeroControlCambio.setText("");
                    txtNombreCambio.setText("");
                    txtPrimerApellidoCambio.setText("");
                    txtSegundoApellidoCambio.setText("");
                    comboSemestreCambio.setSelectedIndex(0);
                    comboCarreraCambio.setSelectedIndex(0);
                    txtNombreCambio.setEditable(false);
                    txtPrimerApellidoCambio.setEditable(false);
                    txtSegundoApellidoCambio.setEditable(false);
                    comboSemestreCambio.setEnabled(false);
                    comboCarreraCambio.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Error al actualizar los datos del alumno en la base de datos");
                }//if-else

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "El semestre debe ser un número");
            }//try-catch
        });

        btnCancelar.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        return panel;

    }//crearPanelCambios

    //==================================================================================================================================================================================================================================
    private JPanel crearPanelConsultas() {
        panelConsultas = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("CONSULTAS ALUMNOS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(Color.BLUE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        formPanel.add(lblTitulo, gbc);

        //para seleccionar como buscarlo
        gbc.gridwidth = 3;
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Selecciona criterio de búsqueda:"), gbc);

        ButtonGroup criteriaGroup = new ButtonGroup();
        JRadioButton rbTodos = new JRadioButton("TODOS", true); //selecciona todo en general
        JRadioButton rbNumControl = new JRadioButton("NÚMERO DE CONTROL");
        JRadioButton rbNombre = new JRadioButton("NOMBRE");
        JRadioButton rbApPaterno = new JRadioButton("APELLIDO PATERNO");
        JRadioButton rbApMaterno = new JRadioButton("APELLIDO MATERNO");
        JRadioButton rbSemestre = new JRadioButton("SEMESTRE");
        JRadioButton rbCarrera = new JRadioButton("CARRERA");

        criteriaGroup.add(rbTodos);
        criteriaGroup.add(rbNumControl);
        criteriaGroup.add(rbNombre);
        criteriaGroup.add(rbApPaterno);
        criteriaGroup.add(rbApMaterno);
        criteriaGroup.add(rbSemestre);
        criteriaGroup.add(rbCarrera);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(rbTodos, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(rbNumControl, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(rbNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(rbApPaterno, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(rbApMaterno, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(rbSemestre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        formPanel.add(rbCarrera, gbc);

        txtNumeroControlConsulta = new JTextField(15);
        txtNombreConsulta = new JTextField(15);
        txtPrimerApellidoConsulta = new JTextField(15);
        txtSegundoApellidoConsulta = new JTextField(15);
        spinnerSemestreConsultaCampo = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        String[] carrerasConsulta = {"Elige Carrera...", "Ingeniería en Sistemas Computacionales", "Ingeniería Mecatrónica", "Administración", "Contabilidad", "Ingeniería en Industrias Alimentarias"};
        comboCarreraConsulta = new JComboBox<>(carrerasConsulta);
        comboCarreraConsulta.setEnabled(false);

        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(txtNumeroControlConsulta, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(txtNombreConsulta, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        formPanel.add(txtPrimerApellidoConsulta, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        formPanel.add(txtSegundoApellidoConsulta, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        formPanel.add(spinnerSemestreConsultaCampo, gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        formPanel.add(comboCarreraConsulta, gbc);

        btnBuscar = new JButton("BUSCAR");
        JButton btnBorrar = new JButton("BORRAR");
        JButton btnCancelar = new JButton("CANCELAR");

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        formPanel.add(btnBuscar, gbc);

        gbc.gridx = 1;
        gbc.gridy = 9;
        formPanel.add(btnBorrar, gbc);

        gbc.gridx = 2;
        gbc.gridy = 9;
        formPanel.add(btnCancelar, gbc);

        // Crear la tabla para Consultas
        String[] columnas = {"NUMERO DE CON...", "NOMBRE", "AP. PATERNO", "AP. MATERNO", "SEMESTRE", "CARRERA"};
        tableModelConsultas = new DefaultTableModel(columnas, 0);
        tablaConsultas = new JTable(tableModelConsultas);
        JScrollPane scrollPane = new JScrollPane(tablaConsultas);

        // Panel para la navegación
        navegacionPanelConsultas = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPrimero = new JButton("|<");
        btnAnterior = new JButton("<");
        txtIndice = new JTextField("0", 3);
        txtIndice.setHorizontalAlignment(SwingConstants.CENTER);
        txtIndice.setEditable(false);
        btnSiguiente = new JButton(">");
        btnUltimo = new JButton(">|");
        listaConsultaResultados = new ArrayList<>();

        navegacionPanelConsultas.add(btnPrimero);
        navegacionPanelConsultas.add(btnAnterior);
        navegacionPanelConsultas.add(txtIndice);
        navegacionPanelConsultas.add(btnSiguiente);
        navegacionPanelConsultas.add(btnUltimo);
        navegacionPanelConsultas.setVisible(false);

        // Agregar a lo principal
        panelConsultas.add(formPanel, BorderLayout.NORTH);
        panelConsultas.add(scrollPane, BorderLayout.CENTER);
        panelConsultas.add(navegacionPanelConsultas, BorderLayout.SOUTH);

        // Agregar listeners
        ActionListener radioListener = e -> {
            txtNumeroControlConsulta.setEnabled(((JRadioButton) e.getSource()).getText().equals("NÚMERO DE CONTROL"));
            txtNombreConsulta.setEnabled(((JRadioButton) e.getSource()).getText().equals("NOMBRE"));
            txtPrimerApellidoConsulta.setEnabled(((JRadioButton) e.getSource()).getText().equals("APELLIDO PATERNO"));
            txtSegundoApellidoConsulta.setEnabled(((JRadioButton) e.getSource()).getText().equals("APELLIDO MATERNO"));
            spinnerSemestreConsultaCampo.setEnabled(((JRadioButton) e.getSource()).getText().equals("SEMESTRE"));
            comboCarreraConsulta.setEnabled(((JRadioButton) e.getSource()).getText().equals("CARRERA"));
            limpiarCamposConsulta();
        };

        for (Component c : formPanel.getComponents()) {
            if (c instanceof JRadioButton) {
                ((JRadioButton) c).addActionListener(radioListener);
            }//if
        }//for

        // que todos los botones de ruedita se deshabiliten a excepción del de "TODOS"
        txtNumeroControlConsulta.setEnabled(false);
        txtNombreConsulta.setEnabled(false);
        txtPrimerApellidoConsulta.setEnabled(false);
        txtSegundoApellidoConsulta.setEnabled(false);
        spinnerSemestreConsultaCampo.setEnabled(false);
        comboCarreraConsulta.setEnabled(false);

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listaConsultaResultados = buscarAlumnos();
                actualizarTablaConsultas();
                actualizarNavegacionConsultas();
                navegacionPanelConsultas.setVisible(listaConsultaResultados.size() > 0);
            }//ActionPerformed
        });

        btnBorrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCamposConsulta();
                tableModelConsultas.setRowCount(0);
                navegacionPanelConsultas.setVisible(false);
                listaConsultaResultados.clear();
                indiceActual = 0;
            }//ActionPerformed
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Menu");
            }//actionPerfomres
        });

        btnPrimero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listaConsultaResultados.size() > 0) {
                    indiceActual = 0;
                    actualizarNavegacionConsultas();
                    mostrarRegistroActual();
                }//if
            }//ActionPerformed
        });

        btnAnterior.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (indiceActual > 0) {
                    indiceActual--;
                    actualizarNavegacionConsultas();
                    mostrarRegistroActual();
                }//if
            }//actionPerdormed
        });

        btnSiguiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (indiceActual < listaConsultaResultados.size() - 1) {
                    indiceActual++;
                    actualizarNavegacionConsultas();
                    mostrarRegistroActual();
                }//if
            }//actionPerformed
        });

        btnUltimo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listaConsultaResultados.size() > 0) {
                    indiceActual = listaConsultaResultados.size() - 1;
                    actualizarNavegacionConsultas();
                    mostrarRegistroActual();
                }//if
            }//actionPerformed
        });

        return panelConsultas;
    }//crearPanelConsultas

    private void limpiarCamposConsulta() {
        txtNumeroControlConsulta.setText("");
        txtNombreConsulta.setText("");
        txtPrimerApellidoConsulta.setText("");
        txtSegundoApellidoConsulta.setText("");
        if (spinnerSemestreConsultaCampo != null) {
            spinnerSemestreConsultaCampo.setValue(1);
        }//of
        comboCarreraConsulta.setSelectedIndex(0);
    }//limpiarCamposConsulta

    //de acuerdo a lo selecionado, muetsra los datos
    private List<Alumno> buscarAlumnos() {
        List<Alumno> resultados = new ArrayList<>();
        String criterio = "";
        String valor = "";

        if (panelConsultas != null) {
            Component[] components = ((JPanel) panelConsultas.getComponent(0)).getComponents();
            for (Component component : components) {
                if (component instanceof JRadioButton) {
                    JRadioButton radioButton = (JRadioButton) component;
                    if (radioButton.isSelected()) {
                        criterio = radioButton.getText();
                        break;
                    }//if adentro1
                }//if afuera 1
            }//for

            switch (criterio) {
                case "TODOS":
                    resultados = alumnoDAO.mostrarAlumnos(null);
                    break;
                case "NÚMERO DE CONTROL":
                    valor = txtNumeroControlConsulta.getText().trim();
                    resultados = alumnoDAO.buscarAlumnos("numControl", valor);
                    break;
                case "NOMBRE":
                    valor = txtNombreConsulta.getText().trim();
                    resultados = alumnoDAO.buscarAlumnos("nombre", valor);
                    break;
                case "APELLIDO PATERNO":
                    valor = txtPrimerApellidoConsulta.getText().trim();
                    resultados = alumnoDAO.buscarAlumnos("primerAp", valor);
                    break;
                case "APELLIDO MATERNO":
                    valor = txtSegundoApellidoConsulta.getText().trim();
                    resultados = alumnoDAO.buscarAlumnos("segundoAp", valor);
                    break;
                case "SEMESTRE":
                    valor = String.valueOf(((Number) spinnerSemestreConsultaCampo.getValue()).byteValue());
                    resultados = alumnoDAO.buscarAlumnos("semestre", valor);
                    break;
                case "CARRERA":
                    valor = (String) comboCarreraConsulta.getSelectedItem();
                    if (valor != null && !valor.equals("Elige Carrera...")) {
                        resultados = alumnoDAO.buscarAlumnos("carrera", valor);
                    }//if
                    break;
                default:
                    resultados = new ArrayList<>();
            }//switcj
        }//if
        return resultados;
    }//bucarAlumnos

    private void actualizarTablaConsultas() {
        tableModelConsultas.setRowCount(0);
        if (listaConsultaResultados != null && !listaConsultaResultados.isEmpty()) {
            for (Alumno alumno : listaConsultaResultados) {
                tableModelConsultas.addRow(new Object[]{
                        alumno.getNumControl(),
                        alumno.getNombre(),
                        alumno.getPrimerAp(),
                        alumno.getSegundoAp(),
                        alumno.getSemestre(),
                        alumno.getCarrera()
                });
            }//for
        }//if
    }//actualizarTablaConsultas

    private void actualizarNavegacionConsultas() {
        if (listaConsultaResultados.size() > 0) { //que no este vacia
            txtIndice.setText((indiceActual + 1) + " de " + listaConsultaResultados.size());
            btnPrimero.setEnabled(indiceActual > 0);
            btnAnterior.setEnabled(indiceActual > 0);
            btnSiguiente.setEnabled(indiceActual < listaConsultaResultados.size() - 1);
            btnUltimo.setEnabled(indiceActual < listaConsultaResultados.size() - 1);
        } else {
            txtIndice.setText("0 de 0"); //si esta vacia que muestre 0 de 0 (obvi)
            btnPrimero.setEnabled(false);
            btnAnterior.setEnabled(false);
            btnSiguiente.setEnabled(false);
            btnUltimo.setEnabled(false);
        }//if-else
    }//actualizarNavegacionConsultas

    private void mostrarRegistroActual() {
        tableModelConsultas.setRowCount(0); // limpia la tablita
        if (listaConsultaResultados.size() > 0) {
            Alumno alumno = listaConsultaResultados.get(indiceActual);
            tableModelConsultas.addRow(new Object[]{
                    alumno.getNumControl(),
                    alumno.getNombre(),
                    alumno.getPrimerAp(),
                    alumno.getSegundoAp(),
                    alumno.getSemestre(),
                    alumno.getCarrera()
            });
        }//if

    }//mostrarRegistroActual

    //==================================================================================================================================================================================================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SistemaAlumnos().setVisible(true);
            }
        });

    }//main

}//SistemaAlumnos (public class)