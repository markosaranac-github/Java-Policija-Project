<%@page import="org.hibernate.Session"%>
<%@page import="db.HibernateUtil"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Izmena inspektora | Policijska stanica</title>
        <link rel="stylesheet" href="css/bootstrap.flatly.min.css">
        <link rel="stylesheet" href="css/typicons/typicons.min.css">
    </head>
    <body>
        <%@include file="include/header.jsp" %>
        
        <div class="container">
            
            <%
                Session hibSession = HibernateUtil.getSessionFactory().openSession();
                Inspektor i = (Inspektor) hibSession.get(Inspektor.class, Integer.parseInt(request.getParameter("id")));
            %>
            
            <h4>Informacije o inspektoru:</h4>
            
            <form action="InspektorServlet" method="get" style="margin-bottom: 100px">
                <input type="hidden" name="action" value="edit">
                <input type="hidden" name="id" value="<%=i.getInsId()%>">
                <label for="ime">Ime inspektora:</label>
                <input type="text" class="form-control" id="ime" name="ime" value="<%=i.getInsIme()%>"><br/>
                <label for="prezime">Prezime inspektora:</label>
                <input type="text" class="form-control" id="prezime" name="prezime" value="<%=i.getInsPrezime()%>"><br/>
                <label for="datum">Datum rođenja:</label>
                <input type="date" class="form-control" id="datum" name="datum" value="<%=i.getInsDtrodj().toString()%>"><br/>
                <label for="telefon">Telefon:</label>
                <input type="text" class="form-control" id="telefon" name="telefon" value="<%=i.getInsTelefon()%>"><br/>
                <label for="adresa">Adresa:</label>
                <input type="text" class="form-control" id="adresa" name="adresa" value="<%=i.getInsAdresa()%>"><br/>
                <label for="korisnicko_ime">Korisničko ime:</label>
                <input type="text" class="form-control" id="korisnicko_ime" name="korisnicko_ime" value="<%=i.getInsKorisnickoIme()%>"><br/>
                <label for="sifra">Resetovanje šifre:</label>
                <a href="InspektorServlet?action=reset&id=<%=i.getInsId()%>" class="btn btn-primary" style="margin-left: 20px;" id="sifra"><div class="typcn icon-default typcn-arrow-sync"> Resetuj šifru</div></a><br/><br/>
                <button class="form-control btn btn-primary"><div class="typcn icon-default typcn-tick"> Sačuvaj izmene</div></button>
            </form>
            
        </div>
        
        <%@include file="include/footer.jsp" %>
    </body>
</html>
