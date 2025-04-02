import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/EmployeeServlet")
public class EmployeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "password";
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            
            String empId = request.getParameter("empId");
            
            if (empId == null || empId.isEmpty()) {
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employees");
                ResultSet rs = stmt.executeQuery();
                
                out.println("<html><body>");
                out.println("<h2>Employee List</h2>");
                out.println("<table border='1'><tr><th>ID</th><th>Name</th><th>Department</th></tr>");
                
                while (rs.next()) {
                    out.println("<tr><td>" + rs.getInt("id") + "</td><td>" + rs.getString("name") + "</td><td>" + rs.getString("department") + "</td></tr>");
                }
                out.println("</table>");
            } else {
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employees WHERE id = ?");
                stmt.setInt(1, Integer.parseInt(empId));
                ResultSet rs = stmt.executeQuery();
                
                out.println("<html><body>");
                if (rs.next()) {
                    out.println("<h2>Employee Details</h2>");
                    out.println("<p>ID: " + rs.getInt("id") + "</p>");
                    out.println("<p>Name: " + rs.getString("name") + "</p>");
                    out.println("<p>Department: " + rs.getString("department") + "</p>");
                } else {
                    out.println("<h2>No Employee Found</h2>");
                }
            }
            out.println("<br><a href='employee_search.html'>Search Again</a>");
            out.println("</body></html>");
            
            conn.close();
        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
    }
}
