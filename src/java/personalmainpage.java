
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author godgiven
 */
public class personalmainpage {

    public static void create_mainpage(String username, PrintWriter out, String path) {
        
        
        out.println("<html>");
        out.println("<head>");

        out.println("<link href=\"personalmainpage.css\" rel=\"stylesheet\" type=\"text/css\" />");

        out.println("</head>");
        out.println("<body>");

        out.println("<p>User: " + username + "</p> " + "<a href=\"index.jsp\">Log Out</a>");

        out.println("<form action=\"upload\" method=\"post\" enctype=\"multipart/form-data\">\n" + "\n"
                + "<h1>Upload a photo:</h1> <input name=\"uploadedfile\" type=\"file\"/><br/>\n" + "\n"
                + "<input type=\"submit\" value=\"Upload File\" />\n" + "\n" + "</form>\n" + "\n");
        
        Connection con = null;
        
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            String connectionUrl = "jdbc:mysql://localhost/flickr?"
                    + "user=root&password=123456";
            con = DriverManager.getConnection(connectionUrl);
            
            if (con != null) {
                
                System.out.println("Successful connection to mysql");
                
            }
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from photos where username='" + username + "'");

            while (rs.next()) {
                
                out.println("<div class=\"photos\">");
                
                try {

                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();

                    Document my_doc = db.parse(new File(path + "/photos/" + rs.getString(2) + ".xml"));
                    NodeList challenges = my_doc.getElementsByTagName("image");

                    Node _ch = challenges.item(0);


                    if (_ch.getNodeType() == Node.ELEMENT_NODE) {

                        Element ch = (Element) _ch;

                        String transform = ch.getElementsByTagName("greyscale").item(0).getAttributes().item(0).getNodeValue();

                        if (transform.equals("no")) {

                            out.println("<img src=\"photos/" + rs.getString(2) + "\"> <br>");

                        } else {

                            out.println("<img class=\"greyscale\" src=\"photos/" + rs.getString(2) + "\"> <br>");

                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }





                out.println("<form action=\"greyscale\" method=\"post\" >");
                out.println("<input type=\"submit\" value=\"Greyscale\" />\n");
                out.println("<input type=\"hidden\" name=\"photoname\" value=\"" + rs.getString(2) + "\">");
                out.println("</form>");

                out.println("</div>");


            }


        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (ClassNotFoundException cE) {
            System.out.println("Class Not Found Exception: " + cE.toString());
        }

        out.println("</body>");
        out.println("</html>");

    }
}
