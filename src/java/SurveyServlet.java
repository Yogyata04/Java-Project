
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

@WebServlet("/SurveyServlet")
public class SurveyServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            // Retrieve form data
            String satisfaction = railFenceEncrypt(request.getParameter("satisfaction"));
            String improvements = railFenceEncrypt(request.getParameter("improvements"));
            String tasksCompleted = railFenceEncrypt(request.getParameter("tasks-completed"));
            String priorityTasks = railFenceEncrypt(request.getParameter("priority-tasks"));
            String timeManagement = railFenceEncrypt(request.getParameter("time-management"));

            // Load the JDBC driver
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            // Establish a connection
            Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/sample", "app", "app");
            // Insert data into the database
            String query = "INSERT INTO survey(satisfaction, improve, tasks_completed, priority, time_management) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, satisfaction);
                preparedStatement.setString(2, improvements);
                preparedStatement.setString(3, tasksCompleted);
                preparedStatement.setString(4, priorityTasks);
                preparedStatement.setString(5, timeManagement);

                // Execute the SQL statement to insert data
                int rowsAffected = preparedStatement.executeUpdate();

                // Close resources
                preparedStatement.close();
                connection.close();

                if (rowsAffected > 0) {
                    response.sendRedirect("index.html");
                } else {
                    response.getWriter().println("Failed to add user survey.");
                }
            } catch (SQLException ex) {
                Logger.getLogger(SurveyServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(SurveyServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        out.close();
    }

    // RailFence Encryption Technique
    private String railFenceEncrypt(String input) {
        String evenChars = "", oddChars = "", encrypted = "";
        int i, j = 0, k = 0;

        for (i = 0; i < input.length(); i += 2) {
            evenChars += String.valueOf(input.charAt(i));
        }

        for (i = 1; i < input.length(); i += 2) {
            oddChars += String.valueOf(input.charAt(i));
        }

        encrypted = evenChars + oddChars;

        return encrypted;
    }

}
