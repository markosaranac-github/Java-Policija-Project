<%@page import="entities.Inspektor"%>
<%@page import="entities.Nacelnik"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<nav class="navbar navbar-expand-lg navbar-dark" style="background-color: #2C3E50; margin-bottom: 25px;">
  <div class="container">
    <a class="navbar-brand" href="index.jsp">POLICIJA</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navigationbar" aria-controls="navigationbar" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navigationbar">
      <ul class="navbar-nav mr-auto" style="width: 75%">
        <li class="nav-item">
          <a class="nav-link" href="index.jsp">Početna</a>
        </li>
        <%if (session.getAttribute("user") != null) {%>
            <li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle" href="#" id="dropdown" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Podaci</a>
              <div class="dropdown-menu" aria-labelledby="dropdown">
                <a class="dropdown-item" href="stanica.jsp">Informacije o stanici</a>
                <a class="dropdown-item" href="inspektori.jsp">Inspektori</a>
              </div>
            </li>
             <li class="nav-item">
              <a class="nav-link" href="krivicnadela.jsp">Krivična dela</a>
            </li>
        <%}%>
      </ul>
        <ul class="navbar-nav mr-auto">
            <li class="nav-item dropdown float-right">
                <%if (session.getAttribute("user") != null) {
                    if (session.getAttribute("user").getClass().equals(Nacelnik.class)) { %>
                    <a class="nav-link dropdown-toggle" href="#" id="user-dropdown" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><div class="typcn icon-default typcn-user" style="width: 100px; float: left;"> <%=((Nacelnik)session.getAttribute("user")).getNacKorisnickoIme()%></div></a>
                <%} else {%>
                    <a class="nav-link dropdown-toggle" href="#" id="user-dropdown" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><div class="typcn icon-default typcn-user" style="width: 100px; float: left;"> <%=((Inspektor)session.getAttribute("user")).getInsKorisnickoIme()%></div></a>
                <%}%>
                    <div class="dropdown-menu" aria-labelledby="user-dropdown">
                        <a class="dropdown-item" href="profil.jsp">Moji podaci</a>
                        <a class="dropdown-item" href="UserServlet?action=logout">Izloguj se</a>
                    </div>
                <%} else {%>
                    <a class="nav-link" href="login.jsp"><div class="typcn icon-default typcn-eject" style="margin-top: -10px; width: 100px; float: left;"> Uloguj se</div></a>
                <%}%>
            </li>
        </ul>
    </div>
  </div>
</nav>
            
        <%
            if (request.getParameter("message") != null){
                %>
                <script>
                    alert('<%=request.getParameter("message")%>');
                </script>
                <%
            }
        %>