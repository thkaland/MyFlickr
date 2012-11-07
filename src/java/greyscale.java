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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.ServletException;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.InputSource;
import java.net.URLEncoder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 *
 * @author godgiven
 */
@WebServlet(name = "greyscale", urlPatterns = {"/greyscale"})
public class greyscale extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String photoname = request.getParameter("photoname");

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("user");

        try {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document my_doc = db.parse(new File(this.getServletContext().getRealPath("/") + "/photos/" + photoname + ".xml"));
            NodeList challenges = my_doc.getElementsByTagName("image");

            Node _ch = challenges.item(0);

            String choice = "";

            if (_ch.getNodeType() == Node.ELEMENT_NODE) {

                Element ch = (Element) _ch;

                String transform = ch.getElementsByTagName("greyscale").item(0).getAttributes().item(0).getNodeValue();

                if (transform.equals("no")) {

                    ch.getElementsByTagName("greyscale").item(0).getAttributes().item(0).setNodeValue("yes");
                    choice = ch.getElementsByTagName("greyscale").item(0).getAttributes().item(0).getNodeValue();

                    WriteXMLFile(photoname, choice);

                } else {

                    ch.getElementsByTagName("greyscale").item(0).getAttributes().item(0).setNodeValue("no");
                    choice = ch.getElementsByTagName("greyscale").item(0).getAttributes().item(0).getNodeValue();

                    WriteXMLFile(photoname, choice);

                }
                personalmainpage.create_mainpage(username, out, this.getServletContext().getRealPath("/"));

            }

        } catch (Exception e) {
            e.printStackTrace();
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

    /*public void init(ServletConfig config) throws ServletException {

        super.init(config);
        con = null;


        try {

            Class.forName("com.mysql.jdbc.Driver");
            String connectionUrl = "jdbc:mysql://localhost/flickr?"
                    + "user=root&password=123456";
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
