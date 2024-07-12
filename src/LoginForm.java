import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginForm extends JFrame {
    private JPanel panel;
    private JTextField textFieldCorreo;
    private JPasswordField passwordFieldCedula;
    private JButton loginButton;
    private JLabel labelCorreo;
    private JLabel labelCedula;

    public LoginForm() {
        // Configuración del JFrame
        setTitle("Inicio de Sesión");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        panel = new JPanel();
        panel.setLayout(null);

        // Etiquetas y campos de texto
        labelCorreo = new JLabel("Correo:");
        labelCorreo.setBounds(50, 30, 80, 25);
        panel.add(labelCorreo);

        textFieldCorreo = new JTextField(20);
        textFieldCorreo.setBounds(140, 30, 200, 25);
        panel.add(textFieldCorreo);

        labelCedula = new JLabel("Cédula:");
        labelCedula.setBounds(50, 70, 80, 25);
        panel.add(labelCedula);

        passwordFieldCedula = new JPasswordField(20);
        passwordFieldCedula.setBounds(140, 70, 200, 25);
        panel.add(passwordFieldCedula);

        // Botón de inicio de sesión
        loginButton = new JButton("Iniciar Sesión");
        loginButton.setBounds(140, 110, 150, 25);
        panel.add(loginButton);

        // Acción del botón de inicio de sesión
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String correo = textFieldCorreo.getText();
                String cedula = new String(passwordFieldCedula.getPassword());

                if (validateLogin(correo, cedula)) {
                    JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso");
                    new AdminMenu().setVisible(true);
                    dispose(); // Cerrar el formulario de inicio de sesión
                } else {
                    JOptionPane.showMessageDialog(null, "Correo o cédula incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Añadir el panel al JFrame
        add(panel);
    }

    private boolean validateLogin(String correo, String cedula) {
        boolean isValid = false;
        String url = "jdbc:mysql://localhost:3306/curso";
        String username = "root";
        String password = "123456";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            String query = "SELECT * FROM usuariosadministrador WHERE correo = ? AND cedula = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, correo);
            preparedStatement.setString(2, cedula);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                isValid = true;
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return isValid;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginForm().setVisible(true);
            }
        });
    }
}
