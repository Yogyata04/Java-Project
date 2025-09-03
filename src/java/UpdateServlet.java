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

@WebServlet(urlPatterns = {"/UpdateServlet"})
public class UpdateServlet extends HttpServlet {
    
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            // Get the updated todo details from the request parameters
            int id = Integer.parseInt(request.getParameter("id"));
            String updatedTitle = encrypt(request.getParameter("title"));
            String updatedTargetDate = request.getParameter("target_date");
            String updatedDescription = encrypt(request.getParameter("description"));

            // Update the todo item in the database
            Connection connection = null;
            PreparedStatement preparedStatement = null;

            try {
                Class.forName("org.apache.derby.jdbc.ClientDriver");
                connection = DriverManager.getConnection("jdbc:derby://localhost:1527/sample", "app", "app");

                String updateQuery = "UPDATE todo_list SET title=?, target_date=?, description=? WHERE id=?";
                preparedStatement = connection.prepareStatement(updateQuery);
                preparedStatement.setString(1, updatedTitle);
                preparedStatement.setString(2, updatedTargetDate);
                preparedStatement.setString(3, updatedDescription);
                preparedStatement.setInt(4, id);

                int rowsUpdated = preparedStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    //out.println("Todo item updated successfully.");
                     response.sendRedirect("Show_Todo.jsp");
                } else {
                    out.println("No rows were updated.");
                }
                           
            } catch (Exception e) {
                out.println(e);
            } finally {
                try {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    out.println(e);
                }
            }
        }
        
    }

    /*protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }*/
}
