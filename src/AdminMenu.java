import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class AdminMenu extends JFrame {
    private JPanel panel;
    private JButton addButton;
    private JButton viewAllButton;
    private JButton searchButton;
    private JTextField searchField;
    private JTable studentsTable;
    private JScrollPane scrollPane;

    public AdminMenu() {
        setTitle("Menú de Administrador");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        panel = new JPanel();
        panel.setLayout(null);

        addButton = new JButton("Ingresar Estudiante");
        addButton.setBounds(50, 30, 200, 25);
        panel.add(addButton);

        viewAllButton = new JButton("Ver Todos los Registros");
        viewAllButton.setBounds(50, 70, 200, 25);
        panel.add(viewAllButton);

        searchField = new JTextField(20);
        searchField.setBounds(50, 110, 200, 25);
        panel.add(searchField);

        searchButton = new JButton("Buscar por Código de Matrícula");
        searchButton.setBounds(260, 110, 250, 25);
        panel.add(searchButton);

        studentsTable = new JTable();
        scrollPane = new JScrollPane(studentsTable);
        scrollPane.setBounds(50, 150, 500, 200);
        panel.add(scrollPane);

        // Button Actions
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddStudentForm().setVisible(true);
            }
        });

        viewAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAllStudents();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigo = searchField.getText();
                if (!codigo.isEmpty()) {
                    loadStudentByCodigo(codigo);
                } else {
                    JOptionPane.showMessageDialog(null, "Ingrese un código de matrícula", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(panel);
    }

    private void loadAllStudents() {
        String url = "jdbc:mysql://localhost:3306/curso";
        String username = "root";
        String password = "123456";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            String query = "SELECT * FROM estudiantes";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            studentsTable.setModel(buildTableModel(resultSet));

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadStudentByCodigo(String codigo) {
        String url = "jdbc:mysql://localhost:3306/curso";
        String username = "root";
        String password = "123456";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            String query = "SELECT * FROM estudiantes WHERE codigo_matricula = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, codigo);
            ResultSet resultSet = preparedStatement.executeQuery();
            studentsTable.setModel(buildTableModel(resultSet));

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        Vector<String> columnNames = new Vector<>();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        Vector<Vector<Object>> data = new Vector<>();
        while (resultSet.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(resultSet.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminMenu().setVisible(true);
            }
        });
    }
}

class AddStudentForm extends JFrame {
    private JPanel panel;
    private JTextField textFieldNombre;
    private JTextField textFieldCodigo;
    private JButton addButton;
    private JLabel labelNombre;
    private JLabel labelCodigo;

    public AddStudentForm() {
        setTitle("Agregar Estudiante");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        panel = new JPanel();
        panel.setLayout(null);

        labelNombre = new JLabel("Nombre:");
        labelNombre.setBounds(50, 30, 80, 25);
        panel.add(labelNombre);

        textFieldNombre = new JTextField(20);
        textFieldNombre.setBounds(140, 30, 200, 25);
        panel.add(textFieldNombre);

        labelCodigo = new JLabel("Código:");
        labelCodigo.setBounds(50, 70, 80, 25);
        panel.add(labelCodigo);

        textFieldCodigo = new JTextField(20);
        textFieldCodigo.setBounds(140, 70, 200, 25);
        panel.add(textFieldCodigo);

        addButton = new JButton("Agregar Estudiante");
        addButton.setBounds(140, 110, 150, 25);
        panel.add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = textFieldNombre.getText();
                String codigo = textFieldCodigo.getText();

                if (!nombre.isEmpty() && !codigo.isEmpty()) {
                    addStudent(nombre, codigo);
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(panel);
    }

    private void addStudent(String nombre, String codigo) {
        String url = "jdbc:mysql://localhost:3306/curso";
        String username = "root";
        String password = "123456";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            String query = "INSERT INTO estudiantes (nombre_apellido, codigo_matricula) VALUES (?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, codigo);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Estudiante agregado exitosamente");
            }

            preparedStatement.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
