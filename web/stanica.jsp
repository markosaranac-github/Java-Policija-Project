<%@page import="entities.PolicijskaStanica"%>
<%@page import="db.HibernateUtil"%>
<%@page import="org.hibernate.Session"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Informacije | Policijska stanica</title>
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
            
            <h4>Informacije o policijskoj stanici:</h4>
            <form action="StanicaServlet" method="get">
                <input type="hidden" name="action" value="edit">
                <label for="adresa">Adresa stanice:</label>
                <input class="form-control" type="text" id="adresa" name="adresa" value="<%=stanica.getPolAdresa()%>"><br/>
                <label for="telefon">Telefon stanice:</label>
                <input class="form-control" type="text" id="telefon" name="telefon" value="<%=stanica.getPolTelefon()%>"><br/>
                <label for="nacelnik">Nacelnik stanice:</label>
                <input class="form-control" type="text" id="nacelnik" name="nacelnik" value="<%=((Nacelnik)stanica.getNacelnik()).getNacIme() + " " + ((Nacelnik)stanica.getNacelnik()).getNacPrezime()%>" disabled><br/>
                <%if (session.getAttribute("user") != null) {
                    if (session.getAttribute("user").getClass().equals(Nacelnik.class)) {%>
                    <button class="form-control btn btn-primary"><div class="typcn icon-default typcn-tick"> Sačuvaj izmene</div></button>
                <%  }
                  }%>
            </form>
            
        </div>
                
        <%@include file="include/footer.jsp" %>
    </body>
</html>
