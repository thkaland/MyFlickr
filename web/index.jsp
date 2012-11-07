<%-- 
    Document   : index
    Created on : Nov 7, 2012, 6:45:31 PM
    Author     : godgiven
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>MyFlickr</title>
        <link href="index.css" rel="stylesheet" type="text/css" />

    </head>

    <body>

        <h1>My Flickr</h1>

        <form action="login" method="post">

            <h3>Username</h3><input type="text" name="username"> <br>
            <h3>Password</h3><input type="password" name="password"> <br> 

            <input type="submit" value="Log In"> <br>

        </form>
        
        <a href="register.html">If not registered press here</a> <br>
     
        <ul class="gallery">

            <li>

                <img src="photos/1.jpg"/>
                <img src="photos/2.jpg"/>
                <img src="photos/3.jpg"/>
                <img src="photos/4.jpg"/>
                <img src="photos/5.jpg"/>
                <img src="photos/6.jpg"/>
                <img src="photos/7.jpg"/>
                <img src="photos/8.jpg"/>
                <img src="photos/9.jpg"/>
                <img src="photos/10.jpg"/>
                <img src="photos/11.jpg"/>
                <img src="photos/12.jpg"/>

            </li>

        </ul>

    </body>

</html>