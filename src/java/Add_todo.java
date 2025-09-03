
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/Add_todo"})
public class Add_todo extends HttpServlet {
    
    // Simple Monoalphabetic Cipher
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";          
    private static final String ENCRYPTION_KEY = "zyxwvutsrqponmlkjihgfedcba";    
    
    private String encrypt(String input) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toLowerCase().toCharArray()) {
            if (Character.isLetter(c)) {
                int index = ALPHABET.indexOf(c);
                result.append(index != -1 ? ENCRYPTION_KEY.charAt(index) : c);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        // Retrieve form data
        String title = encrypt(request.getParameter("title"));
        LocalDate targetDate = LocalDate.parse(request.getParameter("targetdate"));
        String description = encrypt(request.getParameter("description"));
        String username = encrypt((String) request.getSession().getAttribute("loggedInUsername"));

        // JDBC code to insert data into the table
        try {

            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/sample", "app", "app");
            String insertQuery = "INSERT INTO todo_list (username,title, target_date, description) VALUES (?,?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, title);
            preparedStatement.setDate(3, java.sql.Date.valueOf(targetDate));
            preparedStatement.setString(4, description);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                out.println("Data Saved");
            }
        } catch (SQLException e) {
            out.println(e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Add_todo.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        // Redirect back to the todo.html page
        response.sendRedirect("Show_Todo.jsp");
    }
}
