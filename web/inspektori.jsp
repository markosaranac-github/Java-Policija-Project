<%@page import="java.util.ArrayList"%>
<%@page import="org.hibernate.Session"%>
<%@page import="db.HibernateUtil"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Inspektori | Policijska stanica</title>
        <link rel="stylesheet" href="css/bootstrap.flatly.min.css">
        <link rel="stylesheet" href="css/typicons/typicons.min.css">
    </head>
    <body>
        <%@include file="include/header.jsp" %>
        
        <div class="container">
            
            <%
                if (session.getAttribute("user") != null) {
                    if (session.getAttribute("user").getClass().equals(Nacelnik.class)) {
            %>
                <h4>Dodaj novog inspektora:</h4>
                <form action="InspektorServlet" method="get">
                    <input type="hidden" name="action" value="add">
                    <table>
                        <tr>
                            <td><input type="text" name="ime" class="form-control" placeholder="Ime"></td>
                            <td><input type="text" name="prezime" class="form-control" placeholder="Prezime"></td>
                            <td><input type="date" name="datum" class="form-control" placeholder="Datum rodjenja"></td>
                            <td><input type="text" name="telefon" class="form-control" placeholder="Telefon"></td>
                            <td><input type="text" name="adresa" class="form-control" placeholder="Adresa"></td>
                            <td><input type="text" name="korisnicko_ime" class="form-control" placeholder="Korisničko ime"></td>
                            <td><input type="text" name="sifra" class="form-control" placeholder="Šifra"></td>
                        </tr>
                        <tr >
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td style="text-align: right" colspan="2"><button class="btn btn-primary w-100" style="margin-top: 10px; height: 40px"><div class="typcn icon-default typcn-plus"> Dodaj inspektora</div></button></td>
                        </tr>
                    </table>
                </form>
                <br/>
            <%      }
                }
            %>
            
            <%
                Session hibSession = HibernateUtil.getSessionFactory().openSession();
                
                ArrayList<Inspektor> inspektori = (ArrayList<Inspektor>) hibSession.createCriteria(Inspektor.class).list();
                
                if (inspektori.size() > 0) {
            %>
                <div>
                    <h4>Lista svih inspektora:</h4>
                    <table class="table table-hover table-bordered table-striped">
                        <tr>
                            <th>Ime i prezime</th>
                            <th>Datum rođenja</th>
                            <th>Telefon</th>
                            <th>Adresa</th>
                            <th>Korisničko ime</th>
                            <%
                                if (session.getAttribute("user") != null) {
                                    if (session.getAttribute("user").getClass().equals(Nacelnik.class)) {
                            %>
                            <th></th>
                            <%      
                                    }
                                }
                            %>
                        </tr>
                        <% for (Inspektor i : inspektori) {%>
                        <tr>
                            <td><%=i.getInsIme() + " " + i.getInsPrezime()%></td>
                            <td><%=i.getInsDtrodj().toString()%></td>
                            <td><%=i.getInsTelefon()%></td>
                            <td><%=i.getInsAdresa()%></td>
                            <td><%=i.getInsKorisnickoIme()%></td>
                            <%
                                if (session.getAttribute("user") != null) {
                                    if (session.getAttribute("user").getClass().equals(Nacelnik.class)) {
                            %>
                            <td>
                                <a href="inspektor.jsp?id=<%=i.getInsId()%>" class="btn btn-outline-primary w-100">
                                    <div class="typcn icon-default typcn-pencil"> Izmeni</div>
                                </a>
                            </td>
                            <%      
                                    }
                                }
                            %>
                        </tr>
                        <% } %>
                    </table>
                </div>
            <%}%>
            
        </div>
        
        <%@include file="include/footer.jsp" %>
    </body>
</html>
