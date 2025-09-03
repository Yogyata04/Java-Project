import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ContributionServlet")
public class ContributionServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Retrieve form data
        String amount = request.getParameter("amount");
        String cardNumber = request.getParameter("cardNumber");
        String expiryDate = request.getParameter("expiryDate");
        String cvv = request.getParameter("cvv");

        // Encrypt data using Rail Fence technique
        String encryptedAmount = railFenceEncrypt(amount);
        String encryptedCardNumber = railFenceEncrypt(cardNumber);
        String encryptedExpiryDate = railFenceEncrypt(expiryDate);
        String encryptedCvv = railFenceEncrypt(cvv);

        
         // JDBC code to insert data into the table
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/sample", "app", "app");

            String insertQuery = "INSERT INTO account_detail(amt, cardNo, expDate,cvv) VALUES (?, ?, ?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, encryptedAmount);
            preparedStatement.setString(2, encryptedCardNumber);
            preparedStatement.setString(3, encryptedExpiryDate);
             preparedStatement.setString(4, encryptedCvv);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                out.println("Feedback Saved");
            }
        } catch (SQLException | ClassNotFoundException e) {
            out.println(e);
        }
        
        out.println("Contribution data stored securely!");

        out.close();
    }

    private String railFenceEncrypt(String input) {
    StringBuilder evenDigits = new StringBuilder();
    StringBuilder oddDigits = new StringBuilder();
    StringBuilder encrypted = new StringBuilder();

    for (int i = 0; i < input.length(); i += 2) {
        char currentChar = input.charAt(i);
        if (Character.isDigit(currentChar)) {
            evenDigits.append(currentChar);
        }
    }

    for (int i = 1; i < input.length(); i += 2) {
        char currentChar = input.charAt(i);
        if (Character.isDigit(currentChar)) {
            oddDigits.append(currentChar);
        }
    }

    encrypted.append(evenDigits).append(oddDigits);
    return encrypted.toString();
}

}
