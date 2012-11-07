/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.sql.*;
import java.util.*;
import java.text.*;
import java.util.regex.*;
import javax.servlet.ServletConfig;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author godgiven
 */
@WebServlet(name = "upload", urlPatterns = {"/upload"})
public class upload extends HttpServlet {

    //static Connection con;

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
        /*response.setContentType("text/html;charset=UTF-8");
         PrintWriter out = response.getWriter();
         try {
         /* TODO output your page here. You may use following sample code. *//*
         out.println("<html>");
         out.println("<head>");
         out.println("<title>Servlet NewServlet</title>");            
         out.println("</head>");
         out.println("<body>");
         out.println("<h1>Servlet NewServlet at " + request.getContextPath() + "</h1>");
         out.println("</body>");
         out.println("</html>");
         } finally {            
         out.close();
         }
         */

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        processRequest(request, response);

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("user");

        String finalimage = "";
        String choice = "";

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        System.out.println("request: " + request);

        if (!isMultipart) {

            System.out.println("File Not Uploaded");

        } else {

            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload uploadimage = new ServletFileUpload(factory);
            List items = null;

            try {

                items = uploadimage.parseRequest(request);
                System.out.println("items: " + items);

            } catch (FileUploadException e) {
                e.printStackTrace();
            }

            Iterator itr = items.iterator();

            while (itr.hasNext()) {

                FileItem item = (FileItem) itr.next();

                if (item.isFormField()) {

                    String name = item.getFieldName();
                    System.out.println("name: " + name);
                    String value = item.getString();
                    System.out.println("value: " + value);

                } else {

                    try {

                        String itemName = item.getName();
                        Random generator = new Random();
                        int r = Math.abs(generator.nextInt());

                        String reg = "[.*]";
                        String replacingtext = "";
                        System.out.println("Text before replacing is:-" + itemName);
                        Pattern pattern = Pattern.compile(reg);
                        Matcher matcher = pattern.matcher(itemName);
                        StringBuffer buffer = new StringBuffer();

                        while (matcher.find()) {

                            matcher.appendReplacement(buffer, replacingtext);

                        }

                        int IndexOf = itemName.indexOf(".");

                        if (IndexOf == -1) {

                            personalmainpage.create_mainpage(username, out, this.getServletContext().getRealPath("/"));
                            return;

                        }

                        String domainName = itemName.substring(IndexOf);
                        System.out.println("domainName: " + domainName);

                        finalimage = buffer.toString() + "_" + r + domainName;
                        System.out.println("Final Image=" + finalimage);


                        File savedFile = new File(this.getServletContext().getRealPath("") + "/photos/" + finalimage);

                        item.write(savedFile);

                        String strQuery = null;
                        
                        Connection con = null;

                        try {
                            
                            Class.forName("com.mysql.jdbc.Driver");
                            String connectionUrl = "jdbc:mysql://localhost/flickr?" + "user=root&password=123456";
                            con = DriverManager.getConnection(connectionUrl);

                            if (con != null) {

                                System.out.println("Successful connection to mysql");

                            }
                            
                            System.out.println("itemName: " + itemName);

                            Statement st = con.createStatement();
                            strQuery = "INSERT INTO photos VALUES('" + username + "', '" + finalimage + "')";
                            int rs = st.executeUpdate(strQuery);

                            System.out.println("Image inserted successfully");
                            
                            con.close();
                            st.close();
                            
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        

                        /*finally {
                         personalmainpage.create_mainpage(username,con, out);
                         }*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
            try {

                choice = "no";
                WriteXMLFile(finalimage, choice);
                personalmainpage.create_mainpage(username, out, this.getServletContext().getRealPath("/"));

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    /*public void init(ServletConfig config) throws ServletException {

        super.init(config);
        con = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");
            String connectionUrl = "jdbc:mysql://localhost/flickr?" + "user=root&password=123456";
            con = DriverManager.getConnection(connectionUrl);

            if (con != null) {

                System.out.println("Successful connection to mysql");

            }

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (ClassNotFoundException cE) {
            System.out.println("Class Not Found Exception: " + cE.toString());
        }
    }*/

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public void WriteXMLFile(String finalimage, String choice) {

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("image");
            doc.appendChild(rootElement);


            Element greyscale = doc.createElement("greyscale");

            Attr attr = doc.createAttribute("enabled");
            attr.setValue(choice);
            greyscale.setAttributeNode(attr);

            rootElement.appendChild(greyscale);


            /* 
             // shorten way
             // staff.setAttribute("id", "1");
 
             // firstname elements
             Element firstname = doc.createElement("firstname");
             firstname.appendChild(doc.createTextNode("yong"));
             staff.appendChild(firstname);
 
             // lastname elements
             Element lastname = doc.createElement("lastname");
             lastname.appendChild(doc.createTextNode("mook kim"));
             staff.appendChild(lastname);
 
             // nickname elements
             Element nickname = doc.createElement("nickname");
             nickname.appendChild(doc.createTextNode("mkyong"));
             staff.appendChild(nickname);
 
             // salary elements
             Element salary = doc.createElement("salary");
             salary.appendChild(doc.createTextNode("100000"));
             staff.appendChild(salary);
             */
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(this.getServletContext().getRealPath("/") + "/photos/" + finalimage + ".xml"));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

            System.out.println("File saved!");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }

    }
}
