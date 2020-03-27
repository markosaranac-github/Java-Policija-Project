<%@page import="org.hibernate.transform.Transformers"%>
<%@page import="org.hibernate.criterion.Projections"%>
<%@page import="entities.Svedok"%>
<%@page import="entities.Osumnjiceni"%>
<%@page import="entities.Osteceni"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.ArrayList"%>
<%@page import="entities.KrivicnoDelo"%>
<%@page import="org.hibernate.Session"%>
<%@page import="db.HibernateUtil"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Krivično delo | Policijska stanica</title>
        <link rel="stylesheet" href="css/bootstrap.flatly.min.css">
        <link rel="stylesheet" href="css/typicons/typicons.min.css">
    </head>
    <body>
        <%@include file="include/header.jsp" %>
        
        <div class="container">
        
            <%
                Session hibSession = HibernateUtil.getSessionFactory().openSession();
                KrivicnoDelo kd = (KrivicnoDelo) hibSession.get(KrivicnoDelo.class, Integer.parseInt(request.getParameter("id")));
            
                ArrayList<Inspektor> svi_inspektori = (ArrayList<Inspektor>) hibSession.createCriteria(Inspektor.class).list();
                ArrayList<Osumnjiceni> svi_osumnjiceni = (ArrayList<Osumnjiceni>) hibSession.createCriteria(Osumnjiceni.class).list();
                ArrayList<Osteceni> svi_osteceni = (ArrayList<Osteceni>) hibSession.createCriteria(Osteceni.class).list();
                ArrayList<Svedok> svi_svedoci = (ArrayList<Svedok>) hibSession.createCriteria(Svedok.class).list();
                
                ArrayList<KrivicnoDelo> svi_clanovi = (ArrayList<KrivicnoDelo>) hibSession.createCriteria(KrivicnoDelo.class).setProjection(
                                                        Projections.distinct(Projections.projectionList()
                                                        .add(Projections.property("kriClan"), "kriClan")))
                                                    .setResultTransformer(Transformers.aliasToBean(KrivicnoDelo.class)).list(); 
            %>
            
            <h4>Informacije o krivičnom delu:</h4>
            
            <form action="DeloServlet" method="get" id="forma">
                <input type="hidden" name="action" value="edit">
                <input type="hidden" name="id" value="<%=kd.getKriId()%>">
                <label for="clan" style="width: 100%;">Prekršaj po članu:</label>
                <input type="text" class="form-control" id="clan" name="clan" style="width: 49%; float: left;" value="<%=kd.getKriClan()%>">
                <select id="clanselect" class="form-control" onchange="clanIzabran()" style="width: 50%; float: left; margin-left: 1%">
                    <%
                        if (svi_clanovi.size() > 0){
                            for (KrivicnoDelo delo : svi_clanovi) {
                    %>
                            <option value="<%=delo.getKriClan()%>"><%=delo.getKriClan()%></option>
                    <%
                            }
                        }
                    %>
                </select>
                <br/><div style="height: 60px;"></div>
                
                <label for="pocetak">Datum početka:</label>
                <% if (kd.getKriPocetak() != null) { %>
                <input type="date" class="form-control" id="pocetak" name="pocetak" value="<%=kd.getKriPocetak().toString()%>"><br/>
                <% } else { %>
                <input type="date" class="form-control" id="pocetak" name="pocetak" value=""><br/>
                <% } %>
                
                <label for="kraj">Datum završetka:</label>
                <% if (kd.getKriKraj() != null) { %>
                <input type="date" class="form-control" id="kraj" name="kraj" value="<%=kd.getKriKraj().toString()%>"><br/>
                <% } else { %>
                <input type="date" class="form-control" id="kraj" name="kraj" value=""><br/>
                <% } %>
                
                <label for="opis">Opis prekršaja:</label><br/>
                <textarea name="opis" id="opis" form="forma" maxlength="500" class="form-control"><%=kd.getKriOpis()%></textarea><br/>
                <button class="form-control btn btn-primary"><div class="typcn icon-default typcn-tick"> Sačuvaj izmene</div></button>
            </form>
                
            <hr/>
            
            <h4/>Inspektori:</h4>
        
            <%
               Set inspektori = kd.getInspektori();
               
               if (inspektori.size() > 0){
            %>
                <table class="table table-hover table-bordered table-striped">
                    <tr>
                        <th>Ime i prezime</th>
                        <th>Datum rođenja</th>
                        <th>Telefon</th>
                        <th>Adresa</th>
                        <th>Korisničko ime</th>
                    </tr>
                    <% for (Iterator iter = inspektori.iterator(); iter.hasNext();) {
                        Inspektor i = (Inspektor) iter.next();
                    %>
                        <tr>
                            <td><%=i.getInsIme() + " " + i.getInsPrezime()%></td>
                            <td><%=i.getInsDtrodj().toString()%></td>
                            <td><%=i.getInsTelefon()%></td>
                            <td><%=i.getInsAdresa()%></td>
                            <td><%=i.getInsKorisnickoIme()%></td>
                        </tr>
                    <% } %>
                </table>
            <% } else { %>
            <h5>Nema inspektora zaduženih za ovo delo!</h5>
            <% } %>
            
            <form action="DeloServlet" style="width: 100%; margin-top: 20px;">
                <input type="hidden" name="action" value="addInspektor">
                <input type="hidden" name="delo" value="<%=request.getParameter("id")%>">
                <select id="inspektorselect" name="inspektorselect" class="form-control">
                    <option value=""></option>
                    <%
                        if (svi_inspektori.size() > 0){
                            for (Inspektor ins : svi_inspektori) {
                    %>
                            <option value="<%=ins.getInsId()%>"><%=ins.getInsIme() + " " + ins.getInsPrezime()%></option>
                    <%
                            }
                        }
                    %>
                </select><br/>
                <button class="form-control btn btn-primary"><div class="typcn icon-default typcn-plus"> Dodaj inspektora</div></button>
            </form>
            <hr/>
            
            <h4/>Oštećeni:</h4>
    
            <%
               Set osteceni = kd.getOsteceni();
               
               if (osteceni.size() > 0){
            %>
                <table class="table table-hover table-bordered table-striped">
                    <tr>
                        <th>Ime i prezime</th>
                        <th>Datum rođenja</th>
                        <th>Telefon</th>
                        <th>Adresa</th>
                        <th></th>
                    </tr>
                    <% for (Iterator iter = osteceni.iterator(); iter.hasNext();) {
                        Osteceni o = (Osteceni) iter.next();
                    %>
                        <tr>
                            <td><%=o.getOstIme() + " " + o.getOstPrezime()%></td>
                            <td><%=o.getOstDtrodj().toString()%></td>
                            <td><%=o.getOstTelefon()%></td>
                            <td><%=o.getOstAdresa()%></td>
                            <td><a href="DeloServlet?action=removeOsteceni&id=<%=o.getOstId()%>&delo=<%=kd.getKriId()%>">
                                <div class="typcn icon-default typcn-times" style="text-align: center; font-size: 1.2em; color: red"></div>
                            </a></td>
                        </tr>
                    <% } %>
                </table>
            <% } else { %>
            <h5>Nema oštećenih prijavljenih na ovo delo!</h5>
            <% } %>
            
            <form action="DeloServlet" method="get" style="margin-top: 20px;">
                <input type="hidden" name="action" value="addOsteceni">
                <input type="hidden" name="delo" value="<%=request.getParameter("id")%>">
                <div class="row">
                    <div class="col-md-6">
                        <label for="osteceni">Novi oštećeni:</label>
                        <input class="form-control" name="osteceni" id="osteceni" type="text"  placeholder="Ime, Prezime, Datum rodj. (yyyy-mm-dd), Adresa, Telefon">
                    </div>
                    <div class="col-md-6">
                        <label for="osteceniselect">Postojeći oštećeni:</label>
                        <select id="osteceniselect" name="osteceniselect" class="form-control">
                            <option value=""></option>
                            <%
                                if (svi_osteceni.size() > 0){
                                    for (Osteceni ost : svi_osteceni) {
                            %>
                                    <option value="<%=ost.getOstId()%>"><%=ost.getOstIme() + " " + ost.getOstPrezime()%></option>
                            <%
                                    }
                                }
                            %>
                        </select><br/>
                    </div>
                </div>
                <button style="margin-top: 20px" class="form-control btn btn-primary"><div class="typcn icon-default typcn-plus"> Dodaj oštećenog</div></button>
            </form>
            <hr/>
            
            <h4/>Osumnjičeni:</h4>
        
            <%
               Set osumnjiceni = kd.getOsumnjiceni();
               
               if (osumnjiceni.size() > 0){
            %>
                <table class="table table-hover table-bordered table-striped">
                    <tr>
                        <th>Ime i prezime</th>
                        <th>Datum rođenja</th>
                        <th>Telefon</th>
                        <th>Adresa</th>
                        <th>Visina / Težina</th>
                        <th>Opis</th>
                        <th></th>
                    </tr>
                    <% for (Iterator iter = osumnjiceni.iterator(); iter.hasNext();) {
                        Osumnjiceni o = (Osumnjiceni) iter.next();
                    %>
                        <tr>
                            <td><%=o.getOsuIme() + " " + o.getOsuPrezime()%></td>
                            <td><%=o.getOsuDtrodj().toString()%></td>
                            <td><%=o.getOsuTelefon()%></td>
                            <td><%=o.getOsuAdresa()%></td>
                            <td><%=o.getOsuVisina()%> cm / <%=o.getOsuTezina()%> kg</td>
                            <td><%=o.getOsuOpis()%></td>
                            <td><a href="DeloServlet?action=removeOsumnjiceni&id=<%=o.getOsuId()%>&delo=<%=kd.getKriId()%>">
                                <div class="typcn icon-default typcn-times" style="text-align: center; font-size: 1.2em; color: red"></div>
                            </a></td>
                        </tr>
                    <% } %>
                </table>
            <% } else { %>
            <h5>Nema osumnjičenih za ovo delo!</h5>
            <% } %>
            
            <form action="DeloServlet" style="margin-top: 20px;">
                <input type="hidden" name="action" value="addOsumnjiceni">
                <input type="hidden" name="delo" value="<%=request.getParameter("id")%>">
                <div class="row">
                    <div class="col-md-12">
                        <label for="osumnjiceni">Nov osumnjičeni:</label>
                        <input class="form-control" name="osumnjiceni" id="osumnjiceni" type="text"  placeholder="Ime, Prezime, Datum rodj. (yyyy-mm-dd), Adresa, Telefon, Visina / Težina, Opis">
                    </div>
                </div><br/>
                <div class="row">
                    <div class="col-md-12">
                        <label for="osumnjiceniselect">Postojeći osumnjičeni:</label>
                        <select id="osumnjiceniselect" name="osumnjiceniselect" class="form-control">
                            <option value=""></option>
                            <%
                                if (svi_osumnjiceni.size() > 0){
                                    for (Osumnjiceni osu : svi_osumnjiceni) {
                            %>
                                    <option value="<%=osu.getOsuId()%>"><%=osu.getOsuIme() + " " + osu.getOsuPrezime()%></option>
                            <%
                                    }
                                }
                            %>
                        </select><br/>
                    </div>
                </div>
                <button style="margin-top: 20px" class="form-control btn btn-primary"><div class="typcn icon-default typcn-plus"> Dodaj osumnjičenog</div></button>
            </form>
            <hr/>
            
            <h4/>Svedoci</h4>
        
            <%
               Set svedoci = kd.getSvedoci();
               
               if (svedoci.size() > 0){
            %>
                <table class="table table-hover table-bordered table-striped">
                    <tr>
                        <th>Ime i prezime</th>
                        <th>Datum rođenja</th>
                        <th>Telefon</th>
                        <th>Adresa</th>
                        <th></th>
                    </tr>
                    <% for (Iterator iter = svedoci.iterator(); iter.hasNext();) {
                        Svedok s = (Svedok) iter.next();
                    %>
                        <tr>
                            <td><%=s.getSveIme() + " " + s.getSvePrezime()%></td>
                            <td><%=s.getSveDtrodj().toString()%></td>
                            <td><%=s.getSveTelefon()%></td>
                            <td><%=s.getSveAdresa()%></td>
                            <td><a href="DeloServlet?action=removeSvedok&id=<%=s.getSveId()%>&delo=<%=kd.getKriId()%>">
                                <div class="typcn icon-default typcn-times" style="text-align: center; font-size: 1.2em; color: red"></div>
                            </a></td>
                        </tr>
                        <tr>
                            <td colspan="5" style="text-align: justify; overflow-y: scroll">
                                <b><%=s.getSveIme() + " " + s.getSvePrezime()%>, svedočenje:</b> <%=hibSession.createSQLQuery("select kri_sve_opis from kri_sve where kri_id = " + request.getParameter("id") + " and sve_id = " + s.getSveId()).list().get(0)%>
                            </td>
                        </tr>
                    <% } %>
                </table>
            <% } else { %>
            <h5>Nema svedoka za ovo delo!</h5>
            <% } %>
            
            
            <form action="DeloServlet" style="margin-top: 20px;">
                <input type="hidden" name="action" value="addSvedok">
                <input type="hidden" name="delo" value="<%=request.getParameter("id")%>">
                <div class="row">
                    <div class="col-md-12">
                        <label for="svedok">Nov svedok:</label>
                        <input class="form-control" name="svedok" id="svedok" type="text" placeholder="Ime, Prezime, Datum rodj. (yyyy-mm-dd), Adresa, Telefon, Svedočenje">
                    </div>
                </div><br/>
                <div class="row">
                    <div class="col-md-12">
                        <label for="svedokselect">Postojeći svedok:</label>
                        <select id="svedokselect" name="svedokselect" class="form-control">
                            <option value=""></option>
                            <%
                                if (svi_svedoci.size() > 0){
                                    for (Svedok sve : svi_svedoci) {
                            %>
                                    <option value="<%=sve.getSveId()%>"><%=sve.getSveIme() + " " + sve.getSvePrezime()%></option>
                            <%
                                    }
                                }
                            %>
                        </select><br/>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <input type="text" id="svedocenje" name="svedocenje" class="form-control" placeholder="Svedočenje svedoka">
                    </div>
                </div>
                
                <button style="margin-top: 20px" class="form-control btn btn-primary"><div class="typcn icon-default typcn-plus"> Dodaj svedoka</div></button>
            </form>

        </div>
        
        <%@include file="include/footer.jsp" %>
        
        <script>
            $("#clanselect").val(null);
         
            function clanIzabran(){
                $("#clan").val($("#clanselect").val());
            }
        </script>
    </body>
</html>
