import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DeleteTodoServlet")
public class DeleteTodoServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the ID of the todo item to delete from the request parameter
        int todoId = Integer.parseInt(request.getParameter("id"));

        // Perform the deletion operation
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/sample", "app", "app");

            // Define the SQL query to delete the todo item by ID
            String deleteQuery = "DELETE FROM todo_list WHERE id=?";
            preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, todoId);

            // Execute the delete operation
            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {
                // Deletion was successful
                response.sendRedirect("Show_Todo.jsp"); // Redirect to the todo list page
            } else {
                // Handle the case where the todo item with the specified ID was not found
                response.sendRedirect("error.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions here
        } finally {
            // Close resources (connection, preparedStatement)
            // Handle exceptions if needed
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
