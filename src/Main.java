import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.*;
class StringInputDialog {
    public static void main(String[] args) {

        JPanel panel = new JPanel(new GridLayout(4, 2));
        JLabel label0 = new JLabel("Name: ");
        JTextField NameTextField = new JTextField(30);
        JLabel label1 = new JLabel("Specialist: ");
        JTextField specialistTextField = new JTextField(30);
        JLabel label2 = new JLabel("Location Preferred: ");
        JTextField locationTextField = new JTextField(20);

        panel.add(label0);
        panel.add(NameTextField);
        panel.add(label1);
        panel.add(specialistTextField);
        panel.add(label2);
        panel.add(locationTextField);

// Show the input dialog
        int result = JOptionPane.showConfirmDialog(
                null, panel, "MedConnect - Finding Finest Doctors Around You: ", JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            String searchSpecialist = specialistTextField.getText();
            String searchLocation = locationTextField.getText();
            String name = NameTextField.getText();
            // Call the database retrieval function
            displayDoctorInformation(searchLocation, searchSpecialist, name);
        }
    }

    private static void displayDoctorInformation(String searchLocation, String searchSpecialist, String name) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/doctors"; // Change to your database name
            String username = "root";
            String password = "Ashwath@123";
            connection = DriverManager.getConnection(url, username, password);

            String query = "SELECT doctor_name, specialty, location FROM doctors WHERE location = ? AND specialty = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, searchLocation);
            preparedStatement.setString(2, searchSpecialist);

            resultSet = preparedStatement.executeQuery();

            StringBuilder doctorInfo = new StringBuilder();
            doctorInfo.append("Hello, " + name + "!\nYour best options are: \n");
            while (resultSet.next()) {
                String doctorName = resultSet.getString("doctor_name");
                String specialty = resultSet.getString("specialty");
                String location = resultSet.getString("location");

                doctorInfo.append("Doctor Name: ").append(doctorName).append("\n");
                doctorInfo.append("Specialty: ").append(specialty).append("\n");
                doctorInfo.append("Location: ").append(location).append("\n\n");
            }

            if (doctorInfo.length() > 0) {
                JOptionPane.showMessageDialog(null, doctorInfo.toString(), "Doctor Information", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No doctors found matching the criteria.", "Doctor Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}