import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/loginServer1"})
public class loginServer1 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String enteredUsername = request.getParameter("username");
        String enteredPassword = request.getParameter("password");

        // Encrypt the entered username and password using Caesar cipher
        String encryptedUsername = encryptCaesarCipher(enteredUsername);
        String encryptedPassword = encryptCaesarCipher(enteredPassword);

        // Replace this with your actual authentication logic
        boolean isAuthenticated = authenticateUser(encryptedUsername, encryptedPassword);

        if (isAuthenticated) {
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUsername", enteredUsername);
            response.sendRedirect("Show_Todo.jsp");
        } else {
            // Handle authentication failure, e.g., display an error message or redirect to the login page
            response.sendRedirect("login1.html");
        }
    }

    protected boolean authenticateUser(String username, String password) {
        // Database connection parameters
        String jdbcUrl = "jdbc:derby://localhost:1527/sample";
        String dbUser = "app";
        String dbPassword = "app";

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
                // Define the SQL statement to check credentials
                String sql = "SELECT * FROM register_user WHERE username = ? AND password = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        // If a row is returned, the username and password are valid
                        if (resultSet.next()) {
                            return true;
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            // Handle exceptions appropriately
            e.printStackTrace();
        }
        return false;
    }

    // Caesar cipher encryption method
    private String encryptCaesarCipher(String input) {
        int shift = 3; // Caesar cipher shift value
        StringBuilder encryptedText = new StringBuilder();

        for (char character : input.toCharArray()) {
            if (Character.isLetter(character)) {
                char encryptedChar = (char) (((character - 'a' + shift) % 26) + 'a');
                encryptedText.append(encryptedChar);
            } else if (Character.isUpperCase(character)) {
                char encryptedChar = (char) (((character - 'A' + shift) % 26) + 'A');
                encryptedText.append(encryptedChar);
            } else {
                encryptedText.append(character);
            }
        }

        return encryptedText.toString();
    }
}
