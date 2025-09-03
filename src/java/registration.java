
import java.io.IOException;
import java.sql.Connection;
import java.sql.*;
import java.sql.DriverManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Archana
 */
@WebServlet(urlPatterns = {"/registration"})
public class registration extends HttpServlet {

    private static String encrypt(String input, int shift) {
        StringBuilder encryptedText = new StringBuilder();

        for (char character : input.toCharArray()) {
            if (Character.isLetter(character)) {
                char shiftedChar = (char) (((character - 'a' + shift) % 26) + 'a');
                encryptedText.append(shiftedChar);
            } else if (Character.isDigit(character)) {
                char shiftedDigit = (char) (((character - '0' + shift) % 10) + '0');
                encryptedText.append(shiftedDigit);
            } else {
                encryptedText.append(character);
            }
        }

        return encryptedText.toString();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String phone = request.getParameter("Phone");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPass;
        confirmPass = request.getParameter("confirm_password");

        // Database connection parameters
        String jdbcUrl = "jdbc:derby://localhost:1527/sample"; // Change your_database
        String dbUser = "app";
        String dbPassword = "app";

        if (password.equals(confirmPass)) {

            try {
                // Load the Derby JDBC driver
                Class.forName("org.apache.derby.jdbc.ClientDriver");

                // Establish a database connection
                Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);

                // Define the SQL statement
                String sql = "INSERT INTO register_user(name,phone,email,username,password)VALUES(?,?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                // Set the parameters
                preparedStatement.setString(1, encrypt(name, 3)); // Encrypt name with Caesar cipher (shift=3)
                preparedStatement.setString(2, encrypt(phone, 3)); // Encrypt phone with Caesar cipher (shift=3)
                preparedStatement.setString(3, encrypt(email, 3)); // Encrypt email with Caesar cipher (shift=3)
                preparedStatement.setString(4, encrypt(username, 3)); // Encrypt username with Caesar cipher (shift=3)
                preparedStatement.setString(5, encrypt(password, 3)); // Encrypt password with Caesar cipher (shift=3)

                // Execute the SQL statement to insert data
                int rowsAffected = preparedStatement.executeUpdate();

                // Close resources
                preparedStatement.close();
                connection.close();

                if (rowsAffected > 0) {
                    response.sendRedirect("Login1.html");
                } else {
                    response.getWriter().println("Failed to add user.");
                }

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                response.getWriter().println("Database error: " + e.getMessage());
            }
        }
    }
}
