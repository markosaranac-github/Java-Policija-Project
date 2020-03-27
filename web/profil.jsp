<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Moji podaci | Policijska stanica</title>
        <link rel="stylesheet" href="css/bootstrap.flatly.min.css">
        <link rel="stylesheet" href="css/typicons/typicons.min.css">
    </head>
    <body>
        <%@include file="include/header.jsp" %>
        
        <div class="container">
            
            <h4>Moji podaci:</h4>
            <br/>
            
            <%
                if (session.getAttribute("user").getClass().equals(Nacelnik.class))
                {
                    Nacelnik user = (Nacelnik) session.getAttribute("user");
            %>
            <div class="row" style="margin-bottom: 100px">
                <div class="col-md-4" style="text-align: center;">
                    <img src="img/user.png" style="width: 80%; margin-top: 50px"><br/><br/>
                    <h4><i>Načelnik</i></h4>
                </div>
                <div class="col-md-8">
                    <form action="UserServlet" method="get">
                        <input type="hidden" name="action" value="edit">
                        <label for="ime">Ime:</label>
                        <input type="text" class="form-control" id="ime" name="ime" value="<%=user.getNacIme()%>"><br/>
                        <label for="prezime">Prezime:</label>
                        <input type="text" class="form-control" id="prezime" name="prezime" value="<%=user.getNacPrezime()%>"><br/>
                        
                        <label for="datum">Datum rođenja:</label>
                        <% if (user.getNacDtrodj() != null) { %>
                        <input type="date" class="form-control" id="datum" name="datum" value="<%=new SimpleDateFormat("yyyy-MM-dd").format(user.getNacDtrodj())%>"><br/>
                        <% } else { %>
                        <input type="date" class="form-control" id="datum" name="datum" value=""><br/>
                        <% } %>
                        
                        <label for="telefon">Telefon:</label>
                        <% if (user.getNacTelefon() != null) { %>
                        <input type="text" class="form-control" id="telefon" name="telefon" value="<%=user.getNacTelefon()%>"><br/>
                        <% } else { %>
                        <input type="text" class="form-control" id="telefon" name="telefon" value=""><br/>
                        <% } %>
                        
                        <label for="adresa">Adresa:</label>
                        <% if (user.getNacAdresa() != null) { %>
                        <input type="text" class="form-control" id="adresa" name="adresa" value="<%=user.getNacAdresa()%>"><br/>
                        <% } else { %>
                        <input type="text" class="form-control" id="adresa" name="adresa" value=""><br/>
                        <% } %>
                        
                        <label for="korisnicko_ime">Korisničko ime:</label>
                        <input type="text" class="form-control" id="korisnicko_ime" name="korisnicko_ime" value="<%=user.getNacKorisnickoIme()%>"><br/>

                        <button class="form-control btn btn-primary"><div class="typcn icon-default typcn-tick"> Sačuvaj izmene</div></button>
                    </form>
                    <br/>
                    <hr/>
                    <h4>Promena šifre:</h4>
                    <form action="UserServlet" method="get">
                        <input type="hidden" name="action" value="changepassword">
                               
                        <label for="sifra">Nova šifra:</label>
                        <input type="password" class="form-control" id="sifra" name="sifra" value=""><br/>
                        
                        <button class="form-control btn btn-primary"><div class="typcn icon-default typcn-arrow-sync"> Promeni šifru</div></button>
                    </form>
                </div>
            </div>
            <%
                } else {
                    Inspektor user = (Inspektor) session.getAttribute("user");
            %>
            <div class="row" style="margin-bottom: 100px">
                <div class="col-md-4" style="text-align: center;">
                    <img src="img/user.png" style="width: 80%; margin-top: 50px"><br/><br/>
                    <h4><i>Inspektor</i></h4>
                </div>
                <div class="col-md-8">
                    <form action="UserServlet" method="get">
                        <input type="hidden" name="action" value="edit">

                        <label for="ime">Ime:</label>
                        <input type="text" class="form-control" id="ime" name="ime" value="<%=user.getInsIme()%>"><br/>
                        <label for="prezime">Prezime:</label>
                        <input type="text" class="form-control" id="prezime" name="prezime" value="<%=user.getInsPrezime()%>"><br/>
                        
                        <label for="datum">Datum rođenja:</label>
                        <% if (user.getInsDtrodj() != null) { %>
                        <input type="date" class="form-control" id="datum" name="datum" value="<%=new SimpleDateFormat("yyyy-MM-dd").format(user.getInsDtrodj())%>"><br/>
                        <% } else { %>
                        <input type="date" class="form-control" id="datum" name="datum" value=""><br/>
                        <% } %>
                        
                        <label for="telefon">Telefon:</label>
                        <% if (user.getInsTelefon() != null) { %>
                        <input type="text" class="form-control" id="telefon" name="telefon" value="<%=user.getInsTelefon()%>"><br/>
                        <% } else { %>
                        <input type="text" class="form-control" id="telefon" name="telefon" value=""><br/>
                        <% } %>
                        
                        <label for="adresa">Adresa:</label>
                        <% if (user.getInsAdresa() != null) { %>
                        <input type="text" class="form-control" id="adresa" name="adresa" value="<%=user.getInsAdresa()%>"><br/>
                        <% } else { %>
                        <input type="text" class="form-control" id="adresa" name="adresa" value=""><br/>
                        <% } %>
                        
                        <label for="korisnicko_ime">Korisničko ime:</label>
                        <input type="text" class="form-control" id="korisnicko_ime" name="korisnicko_ime" value="<%=user.getInsKorisnickoIme()%>"><br/>
                        
                        <button class="form-control btn btn-primary"><div class="typcn icon-default typcn-tick"> Sačuvaj izmene</div></button>
                    </form>
                    <br/>
                    <hr/>
                    <h4>Promena šifre:</h4>
                    <form action="UserServlet" method="get">
                        <input type="hidden" name="action" value="changepassword">
                               
                        <label for="sifra">Nova šifra:</label>
                        <input type="password" class="form-control" id="sifra" name="sifra" value=""><br/>
                        
                        <button class="form-control btn btn-primary"><div class="typcn icon-default typcn-arrow-sync"> Promeni šifru</div></button>
                    </form>
                </div>
            </div>
            <%
                }
            %>
            
        </div>
        
        <%@include file="include/footer.jsp" %>
    </body>
</html>
