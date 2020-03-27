<%@page import="entities.PolicijskaStanica"%>
<%@page import="org.hibernate.Session"%>
<%@page import="db.HibernateUtil"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Početna | Policijska stanica</title>
        <link rel="stylesheet" href="css/bootstrap.flatly.min.css">
        <link rel="stylesheet" href="css/typicons/typicons.min.css">
    </head>
    <body>
        <%@include file="include/header.jsp" %>
        
        <%
            Session hibSession = HibernateUtil.getSessionFactory().openSession();
            
            PolicijskaStanica stanica = (PolicijskaStanica)hibSession.get(PolicijskaStanica.class, 1);
            
            hibSession.close();
        %>
        
        <div class="container">

            <h3>
                Policijska stanica, <%=stanica.getPolAdresa()%>
            </h3>

            <hr/>

            <img src="img/image1.jpg" style="width: 100%;">

            <br/><br/>

        </div>
        
        <%@include file="include/footer.jsp" %>
    </body>
</html>
