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

@WebServlet("/AttendanceServlet")
public class AttendanceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/student_db";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "password";
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String studentName = request.getParameter("studentName");
        String studentId = request.getParameter("studentId");
        String attendanceDate = request.getParameter("attendanceDate");
        String status = request.getParameter("status");
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO attendance (student_id, student_name, attendance_date, status) VALUES (?, ?, ?, ?)");
            stmt.setString(1, studentId);
            stmt.setString(2, studentName);
            stmt.setString(3, attendanceDate);
            stmt.setString(4, status);
            
            int rowsInserted = stmt.executeUpdate();
            
            out.println("<html><body>");
            if (rowsInserted > 0) {
                out.println("<h2>Attendance Recorded Successfully</h2>");
            } else {
                out.println("<h2>Failed to Record Attendance</h2>");
            }
            out.println("<br><a href='attendance.jsp'>Go Back</a>");
            out.println("</body></html>");
            
            conn.close();
        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM attendance");
            ResultSet rs = stmt.executeQuery();
            
            out.println("<html><body>");
            out.println("<h2>Attendance Records</h2>");
            out.println("<table border='1'><tr><th>Student ID</th><th>Name</th><th>Date</th><th>Status</th></tr>");
            
            while (rs.next()) {
                out.println("<tr><td>" + rs.getString("student_id") + "</td><td>" + rs.getString("student_name") + "</td><td>" + rs.getString("attendance_date") + "</td><td>" + rs.getString("status") + "</td></tr>");
            }
            out.println("</table>");
            out.println("<br><a href='attendance.jsp'>Go Back</a>");
            out.println("</body></html>");
            
            conn.close();
        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
    }
}