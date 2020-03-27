<%@page import="org.hibernate.transform.Transformers"%>
<%@page import="org.hibernate.criterion.Projections"%>
<%@page import="entities.KrivicnoDelo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.hibernate.Session"%>
<%@page import="db.HibernateUtil"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Krivična dela | Policijska stanica</title>
        <link rel="stylesheet" href="css/bootstrap.flatly.min.css">
        <link rel="stylesheet" href="css/typicons/typicons.min.css">
    </head>
    <body>
        <%@include file="include/header.jsp" %>
        
        <div class="container">
            
        <%
            Session hibSession = HibernateUtil.getSessionFactory().openSession();

            ArrayList<KrivicnoDelo> dela = (ArrayList<KrivicnoDelo>) hibSession.createCriteria(KrivicnoDelo.class).list();
            
            // https://stackoverflow.com/questions/10731723/how-to-add-distinct-in-hibernate-criteria
            ArrayList<KrivicnoDelo> svi_clanovi = (ArrayList<KrivicnoDelo>) hibSession.createCriteria(KrivicnoDelo.class).setProjection(
                                                        Projections.distinct(Projections.projectionList()
                                                        .add(Projections.property("kriClan"), "kriClan")))
                                                    .setResultTransformer(Transformers.aliasToBean(KrivicnoDelo.class)).list(); 
            
            if (session.getAttribute("user") != null) {
                if (session.getAttribute("user").getClass().equals(Nacelnik.class)) {
        %>
            <h4>Dodaj krivično delo:</h4>
            <form action="DeloServlet" id="novodelo" method="get">
                <input type="hidden" name="action" value="add">
                <table style="width: 100%">
                    <tr style="width: 100%">
                        <td style="width: 33%">Prekršaj po članu: <br/><input type="text" id="clan" name="clan" class="form-control" placeholder="Član" style="width: 48%; float: left;">
                            <select id="clanselect" class="form-control" onchange="clanIzabran()" style="width: 48%; float: left; margin-left: 2%">
                                <%
                                    if (svi_clanovi.size() > 0){
                                        for (KrivicnoDelo kd : svi_clanovi) {
                                %>
                                        <option value="<%=kd.getKriClan()%>"><%=kd.getKriClan()%></option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </td>
                        <td style="width: 33%">Datum početka: <input type="date" name="pocetak" class="form-control"></td>
                        <td style="width: 33%">Datum kraja: <input type="date" name="kraj" class="form-control"></td>
                    </tr>
                    <tr style="width: 100%; height: 80px;">
                        <td style="width: 33%"><textarea name="opis" style="font-size: small; width: 200%" class="form-control-lg" form="novodelo" maxlength="500" placeholder="Kratak opis (do 500 karaktera)..."></textarea></td>
                        <td style="width: 33%"></td>
                        <td style="width: 33%; text-align: right"><button class="btn btn-primary w-100"><div class="typcn icon-default typcn-plus"> Dodaj krivično delo</div></button></td>
                    </tr>
                </table>
            </form>
            <br/>
        <%      }
            }
        %>
            
        <%
            if (dela.size() > 0) {
        %>
        <div>
            <div>
                <h4>Filtriranje:</h4>
                Po članu:
                <select id="fclan" onchange="filterClan()" class="form-control" style="width: 20%;">
                    <%
                        if (svi_clanovi.size() > 0){
                            for (KrivicnoDelo kd : svi_clanovi) {
                    %>
                            <option value="<%=kd.getKriClan()%>"><%=kd.getKriClan()%></option>
                    <%
                            }
                        }
                    %>
                </select>
                <br/>
                <a id="fbtn" href="krivicnadela.jsp?filter=clan&value=" class="btn btn-primary">Filtriraj</a>
                <a href="krivicnadela.jsp" class="btn btn-warning">Poništi</a>
            </div><br/>
                <h4>Lista krivičnih dela:</h4>
                <table class="table table-hover table-bordered table-striped">
                    <tr>
                        <th>Prekršaj po članu</th>
                        <th>Datum početka</th>
                        <th>Datum završetka</th>
                        <th>Kratak opis</th>
                        <%
                            if (session.getAttribute("user") != null) {
                        %>
                        <th></th>
                        <%      
                            }
                        %>
                    </tr>
                    <% 
                        String filter = "";
                        String value = "";
                        if (request.getParameter("filter") != null){
                            filter = request.getParameter("filter");
                            value = request.getParameter("value");
                        }
                        
                        for (KrivicnoDelo kd : dela) {
                            
                            boolean zadovoljava = false;
                            
                            if (filter.equals("")){
                                zadovoljava = true;
                            } else if (filter.equals("clan")){
                                if (("" + kd.getKriClan()).equals(value)){
                                    zadovoljava = true;
                                }
                            }
                            
                            if (zadovoljava) {
                    %>
                    <tr>
                        <td><%=kd.getKriClan()%></td>
                        <% if (kd.getKriPocetak() != null){ %>
                        <td><%=kd.getKriPocetak().toString()%></td>
                        <% } else { %>
                        <td></td>
                        <% } %>
                        <% if (kd.getKriKraj() != null){ %>
                        <td><%=kd.getKriKraj().toString()%></td>
                        <% } else { %>
                        <td></td>
                        <% } %>
                        <td><%=kd.getKriOpis()%></td>
                        <%
                            if (session.getAttribute("user") != null) {
                                boolean canedit = false;
                                
                                if (session.getAttribute("user").getClass().equals(Nacelnik.class)){
                                    canedit = true;
                                } else {
                                    Inspektor ins = (Inspektor)session.getAttribute("user");
                                    
                                    if (hibSession.createSQLQuery("select * from kri_ins where kri_id = " + kd.getKriId() + " and ins_id = " + ins.getInsId()).list().size() > 0){
                                        canedit = true;
                                    }
                                }
                                
                                if (canedit){
                        %>
                        <td>
                            <a href="krivicnodelo.jsp?id=<%=kd.getKriId()%>" class="btn btn-outline-primary w-100">
                                <div class="typcn icon-default typcn-pencil"> Izmeni</div>
                            </a>
                        </td>
                        <%      } else {
                        %>
                        <td>
                            <a href="#" class="btn btn-outline-primary w-100 disabled">
                                <div class="typcn icon-default typcn-pencil"> Izmeni</div>
                            </a>
                        </td>
                        <%
                                }
                            }
                        %>
                    </tr>
                    <% }
                    } %>
                </table>
            </div>
        <%
            }
        %>
        </div>
        
        <%@include file="include/footer.jsp" %>
        
        <script>
            $("#clanselect").val(null);
            $("#fclan").val(null);
            
            function clanIzabran(){
                $("#clan").val($("#clanselect").val());
            }
            
            function filterClan(){
                $("#fbtn").attr('href', "krivicnadela.jsp?filter=clan&value=" + $("#fclan").val());
            }
        </script>
    </body>
</html>
