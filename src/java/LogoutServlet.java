
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/LogoutServlet"})
public class LogoutServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            // Get the user's session
            HttpSession session = request.getSession(false);

            if (session != null) {
                // Invalidate the session (log out the entered user)
                session.invalidate();
            }

            // Redirect to the index.html page for confirmation
            response.sendRedirect("index.html");
        } catch (Exception e) {
            out.println(e);
        }
    }
}
