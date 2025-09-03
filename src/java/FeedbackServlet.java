import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/FeedbackServlet"})
public class FeedbackServlet extends HttpServlet {

  // Polyalphabetic Cipher Encryption Key
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final String KEY = "keyword"; // Example key, you can use your own

    private String encrypt(String input) {
        StringBuilder result = new StringBuilder();
        int inputLength = input.length();
        int keyLength = KEY.length();

        for (int i = 0; i < inputLength; i++) {
            char currentChar = input.charAt(i);

            if (Character.isLetter(currentChar)) {
                char keyChar = KEY.charAt(i % keyLength);
                int shift = ALPHABET.indexOf(Character.toLowerCase(keyChar)); // Calculate shift based on key character
                char encryptedChar = encryptChar(currentChar, shift);
                result.append(encryptedChar);
            } else {
                result.append(currentChar);
            }
        }
        return result.toString();
    }

    private char encryptChar(char c, int shift) {
        int index = ALPHABET.indexOf(Character.toLowerCase(c));
        if (index == -1) {
            // Character is not in the alphabet, return it unchanged
            return c;
        }

        char encryptedChar = ALPHABET.charAt((index + shift) % ALPHABET.length());
        return Character.isUpperCase(c) ? Character.toUpperCase(encryptedChar) : encryptedChar;
    }
    
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Retrieve form data
        String name = encrypt(request.getParameter("name"));
        String email = encrypt(request.getParameter("email"));
        String message = encrypt(request.getParameter("message"));

        // JDBC code to insert data into the table
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/sample", "app", "app");

            String insertQuery = "INSERT INTO feedback_user (name, email, message) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, message);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                out.println("Feedback Saved");
            }
        } catch (SQLException | ClassNotFoundException e) {
            out.println(e); 
        }

        // Redirect back to the feedback.html page or any other page
        response.sendRedirect("index.html");
    }
}
