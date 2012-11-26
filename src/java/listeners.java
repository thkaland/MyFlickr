/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;

import java.sql.*;

/**
 *
 * @author godgiven
 */
@WebServlet(name = "listeners", urlPatterns = {"/listeners"}, loadOnStartup = 1)
public class listeners extends HttpServlet implements loginInterface, logoutInterface, registerInterface {

    static List<String> usersLogined = null;
    static List<String> usersRegistered = null;

    public void userLogin(String username) {
        if (usersLogined == null) {
            usersLogined = new ArrayList<String>();
        }
        usersLogined.add(username);

    }

    public void userLogout(String username) {

        for (Iterator<String> itr = usersLogined.iterator(); itr.hasNext();) {
            String element = itr.next();
            if (element.equals(username)) {
                itr.remove();
            }
        }

    }

    public void userRegister(String username) {
        if (usersRegistered == null) {
            usersRegistered = new ArrayList<String>();
        }
        usersRegistered.add(username);

    }

    public void init(ServletConfig config) throws ServletException {

        super.init(config);
        login.addLoginInterface(this);
        logout.addLogoutInterface(this);
        register.addRegisterInterface(this);


        Statement stmt;
        ResultSet rs;
        Connection con = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");
            String connectionUrl = "jdbc:mysql://localhost/flickr?"
                    + "user=root&password=123456";
            con = DriverManager.getConnection(connectionUrl);

            if (con != null) {

                System.out.println("Successful connection to mysql");

            }
            stmt = con.createStatement();

            rs = stmt.executeQuery("SELECT * FROM users");
            while (rs.next()) {

                userRegister(rs.getObject(1).toString());

            }

            stmt.close();
            rs.close();
            con.close();

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (ClassNotFoundException cE) {
            System.out.println("Class Not Found Exception: " + cE.toString());
        }

    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Iterator i;

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("user");

        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Stats</title>");
            out.println("</head>");
            out.println("<body>");
            //out.println("<h1>Servlet listeners at " + request.getContextPath() + "</h1>");



            if (usersLogined != null) {
                out.println("<p>Users which are currently online (" + usersLogined.size() + ")</p>");
                for (i = usersLogined.iterator(); i.hasNext();) {
                    out.println("<p>" + (String) i.next() + "</p>");
                }
            } else {
                out.println("<p>Users which are currently online (0)</p>");
            }
            if (usersRegistered != null) {
                out.println("<p>Users which are currently registered (" + usersRegistered.size() + ")</p>");
                for (i = usersRegistered.iterator(); i.hasNext();) {
                    out.println("<p>" + (String) i.next() + "</p>");
                }
            } else {
                out.println("<p>Users which are currently registered (0)</p>");
            }
            out.println("</body>");
            out.println("</html>");

            Thread.sleep(500);

            personalmainpage.create_mainpage(username, out, this.getServletContext().getRealPath("/"));

        } catch (InterruptedException e) {
            System.out.println("Terminated!");

        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
