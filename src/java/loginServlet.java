
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/loginServlet"})
public class loginServlet extends HttpServlet {

    // Ceaser Cipher Encryption
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

        String enteredUsername = request.getParameter("username");
        String enteredPassword = request.getParameter("password");

        // Database connection parameters
        String jdbcUrl = "jdbc:derby://localhost:1527/sample"; // Change your_database
        String dbUser = "app";
        String dbPassword = "app";

        try {

            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);

            // Retrieve the stored encrypted password for the entered username
            String query = "SELECT password FROM register_user WHERE username=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, encrypt(enteredUsername, 3)); // Encrypt username for comparison
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // Retrieve the encrypted password from the database
                    String storedPassword = resultSet.getString("password");

                    // Compare entered password with stored password
                    if (storedPassword.equals(encrypt(enteredPassword, 3))) {
                        // Authentication successful, redirect to a success page
                        HttpSession session = request.getSession();
                        session.setAttribute("loggedInUsername", enteredUsername);
                        response.sendRedirect("Show_Todo.jsp");
                    } else {
                        // Authentication failed, redirect to a failure page
                        response.sendRedirect("Login1.html");
                    }
                } else {
                    // Username not found, redirect to a failure page
                    response.sendRedirect("Login1.html");
                }
            }

            // Close the connection
            connection.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Database error: " + e.getMessage());
        }
    }
}
