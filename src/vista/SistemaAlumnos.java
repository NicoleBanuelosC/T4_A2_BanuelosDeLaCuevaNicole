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
        JPanel consultasPanel = crearPanelConsultas();

        // agregar los paneles al CardLayout
        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(altasPanel, "Altas");
        mainPanel.add(bajasPanel, "Bajas");
        mainPanel.add(cambiosPanel, "Cambios");
        mainPanel.add(consultasPanel, "Consultas");

        // agregar el panel principal a la ventana
        add(mainPanel);

        // agregar los ActionListener
        itemMenuPrincipal.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        itemAltas.addActionListener(e -> {
            AlumnoDAO alumnoDAO = new AlumnoDAO();
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

    // consultas
    private JPanel crearPanelConsultas() {
        JPanel panel = new JPanel(new BorderLayout());
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

        JTextField txtNumControlConsulta = new JTextField(15);
        JTextField txtNombreConsulta = new JTextField(15);
        JTextField txtApPaternoConsulta = new JTextField(15);
        JTextField txtApMaternoConsulta = new JTextField(15);
        JSpinner spinnerSemestreConsulta = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        String[] carrerasConsulta = {"Elige Carrera...", "Ingeniería en Sistemas Computacionales", "Ingeniería Mecatrónica", "Administración", "Contabilidad", "Ingeniería en Industrias Alimentarias"};
        JComboBox<String> comboCarreraConsulta = new JComboBox<>(carrerasConsulta);
        comboCarreraConsulta.setEnabled(false);

        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(txtNumControlConsulta, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(txtNombreConsulta, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        formPanel.add(txtApPaternoConsulta, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        formPanel.add(txtApMaternoConsulta, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        formPanel.add(spinnerSemestreConsulta, gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        formPanel.add(comboCarreraConsulta, gbc);

        JButton btnBuscar = new JButton("BUSCAR");
        JButton btnBorrar = new JButton("BORRAR");
        JButton btnCancelar = new JButton("CANCELAR");

        gbc.gridx = 2;
        gbc.gridy = 4;
        formPanel.add(btnBuscar, gbc);

        gbc.gridx = 2;
        gbc.gridy = 5;
        formPanel.add(btnBorrar, gbc);

        gbc.gridx = 2;
        gbc.gridy = 6;
        formPanel.add(btnCancelar, gbc);

        // Crear la tabla para Consultas
        String[] columnas = {"NUMERO DE CON...", "NOMBRE", "AP. PATERNO", "AP. MATERNO", "SEMESTRE", "CARRERA"};
        tableModelConsultas = new DefaultTableModel(columnas, 0);
        tablaConsultas = new JTable(tableModelConsultas);
        JScrollPane scrollPane = new JScrollPane(tablaConsultas);

        // Agregar a lo principal
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        ActionListener radioListener = e -> {
            txtNumControlConsulta.setEnabled(rbNumControl.isSelected());
            txtNombreConsulta.setEnabled(rbNombre.isSelected());
            txtApPaternoConsulta.setEnabled(rbApPaterno.isSelected());
            txtApMaternoConsulta.setEnabled(rbApMaterno.isSelected());
            spinnerSemestreConsulta.setEnabled(rbSemestre.isSelected());
            comboCarreraConsulta.setEnabled(rbCarrera.isSelected());
        };

        rbTodos.addActionListener(radioListener);
        rbNumControl.addActionListener(radioListener);
        rbNombre.addActionListener(radioListener);
        rbApPaterno.addActionListener(radioListener);
        rbApMaterno.addActionListener(radioListener);
        rbSemestre.addActionListener(radioListener);
        rbCarrera.addActionListener(radioListener);

        // que todos los botones de ruedita se deshabiliten a excepción del de "TODOS"
        txtNumControlConsulta.setEnabled(false);
        txtNombreConsulta.setEnabled(false);
        txtApPaternoConsulta.setEnabled(false);
        txtApMaternoConsulta.setEnabled(false);
        spinnerSemestreConsulta.setEnabled(false);
        comboCarreraConsulta.setEnabled(false);

        // Botón buscar
        btnBuscar.addActionListener(e -> {
            tableModelConsultas.setRowCount(0);

            if (rbTodos.isSelected()) {
                // mostrar todos los registros de la tabla de Altas
                for (int i = 0; i < tableModelAltas.getRowCount(); i++) {
                    tableModelConsultas.addRow(new Object[]{
                            tableModelAltas.getValueAt(i, 0),
                            tableModelAltas.getValueAt(i, 1),
                            tableModelAltas.getValueAt(i, 2),
                            tableModelAltas.getValueAt(i, 3),
                            tableModelAltas.getValueAt(i, 4),
                            tableModelAltas.getValueAt(i, 5)
                    });
                } // for
            } // if

            else if (rbNumControl.isSelected()) {
                String numControl = txtNumControlConsulta.getText().trim();
                if (numControl.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresa un número de control"); //que el numero de control sea lo que si o si deba agregar
                    return;
                } // if

                for (int i = 0; i < tableModelAltas.getRowCount(); i++) {
                    if (tableModelAltas.getValueAt(i, 0).toString().equalsIgnoreCase(numControl)) {
                        tableModelConsultas.addRow(new Object[]{
                                tableModelAltas.getValueAt(i, 0),
                                tableModelAltas.getValueAt(i, 1),
                                tableModelAltas.getValueAt(i, 2),
                                tableModelAltas.getValueAt(i, 3),
                                tableModelAltas.getValueAt(i, 4),
                                tableModelAltas.getValueAt(i, 5)
                        });
                    } // if
                } // for
            } // if

            else if (rbNombre.isSelected()) {
                String nombre = txtNombreConsulta.getText().trim();
                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresa un nombre");
                    return;
                } // if

                for (int i = 0; i < tableModelAltas.getRowCount(); i++) {
                    if (tableModelAltas.getValueAt(i, 1).toString().equalsIgnoreCase(nombre)) {
                        tableModelConsultas.addRow(new Object[]{
                                tableModelAltas.getValueAt(i, 0),
                                tableModelAltas.getValueAt(i, 1),
                                tableModelAltas.getValueAt(i, 2),
                                tableModelAltas.getValueAt(i, 3),
                                tableModelAltas.getValueAt(i, 4),
                                tableModelAltas.getValueAt(i, 5)
                        });
                    } // if
                } // for
            } // if

            else if (rbApPaterno.isSelected()) {
                String apPaterno = txtApPaternoConsulta.getText().trim();
                if (apPaterno.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresa un primer apellido");
                    return;
                } // if

                for (int i = 0; i < tableModelAltas.getRowCount(); i++) {
                    if (tableModelAltas.getValueAt(i, 2).toString().equalsIgnoreCase(apPaterno)) {
                        tableModelConsultas.addRow(new Object[]{
                                tableModelAltas.getValueAt(i, 0),
                                tableModelAltas.getValueAt(i, 1),
                                tableModelAltas.getValueAt(i, 2),
                                tableModelAltas.getValueAt(i, 3),
                                tableModelAltas.getValueAt(i, 4),
                                tableModelAltas.getValueAt(i, 5)
                        });
                    } // if
                } // for
            } // if

            else if (rbApMaterno.isSelected()) {
                String apMaterno = txtApMaternoConsulta.getText().trim();
                if (apMaterno.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresa un segundo apellido");
                    return;
                } // if

                for (int i = 0; i < tableModelAltas.getRowCount(); i++) {
                    if (tableModelAltas.getValueAt(i, 3).toString().equalsIgnoreCase(apMaterno)) {
                        tableModelConsultas.addRow(new Object[]{
                                tableModelAltas.getValueAt(i, 0),
                                tableModelAltas.getValueAt(i, 1),
                                tableModelAltas.getValueAt(i, 2),
                                tableModelAltas.getValueAt(i, 3),
                                tableModelAltas.getValueAt(i, 4),
                                tableModelAltas.getValueAt(i, 5)
                        });
                    } // if
                } // for
            } // if

            else if (rbSemestre.isSelected()) {
                String semestre = spinnerSemestreConsulta.getValue().toString();
                for (int i = 0; i < tableModelAltas.getRowCount(); i++) {
                    if (tableModelAltas.getValueAt(i, 4).toString().equals(semestre)) {
                        tableModelConsultas.addRow(new Object[]{
                                tableModelAltas.getValueAt(i, 0),
                                tableModelAltas.getValueAt(i, 1),
                                tableModelAltas.getValueAt(i, 2),
                                tableModelAltas.getValueAt(i, 3),
                                tableModelAltas.getValueAt(i, 4),
                                tableModelAltas.getValueAt(i, 5)
                        });
                    } // if
                } // for
            } // if

            else if (rbCarrera.isSelected()) {
                String carrera = (String) comboCarreraConsulta.getSelectedItem();
                if (carrera == null || carrera.equals("Elige Carrera...")) { //por si pone esta opcion no la acepte, porque teoricamente no es valida
                    JOptionPane.showMessageDialog(null, "Por favor, selecciona una carrera válida");
                    return;
                } // if

                boolean hayRegistros = false;
                for (int i = 0; i < tableModelAltas.getRowCount(); i++) {
                    if (tableModelAltas.getValueAt(i, 5).toString().equalsIgnoreCase(carrera)) {
                        tableModelConsultas.addRow(new Object[]{
                                tableModelAltas.getValueAt(i, 0),
                                tableModelAltas.getValueAt(i, 1),
                                tableModelAltas.getValueAt(i, 2),
                                tableModelAltas.getValueAt(i, 3),
                                tableModelAltas.getValueAt(i, 4),
                                tableModelAltas.getValueAt(i, 5)
                        });
                        hayRegistros = true;
                    } // if
                } // for

                //verificar registros en la carrera
                if (!hayRegistros) {
                    JOptionPane.showMessageDialog(null, "No se encontraron alumnos en la carrera: " + carrera);
                } // if
            } // if

            if (tableModelConsultas.getRowCount() == 0 && !rbCarrera.isSelected()) {
                JOptionPane.showMessageDialog(null, "No se encontraron resultados que coincidan con eso");
            } // if
        });

        // Botón borrar
        btnBorrar.addActionListener(e -> {
            txtNumControlConsulta.setText("");
            txtNombreConsulta.setText("");
            txtApPaternoConsulta.setText("");
            txtApMaternoConsulta.setText("");
            spinnerSemestreConsulta.setValue(1);
            comboCarreraConsulta.setSelectedIndex(0);
            tableModelConsultas.setRowCount(0);
        });

        // Cancelar -> mandar al menú principal
        btnCancelar.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        return panel;
    } // crearPanelConsultas
    
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