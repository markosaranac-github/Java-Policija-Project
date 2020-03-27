<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Logovanje | Policijska stanica</title>
        <link rel="stylesheet" href="css/bootstrap.flatly.min.css">
        <link rel="stylesheet" href="css/typicons/typicons.min.css">
    </head>
    <body>
        <%@include file="include/header.jsp" %>
        
        <div class="container">
            
            <h4>Logovanje na sistem:</h4>
            <br/>
            <form action="UserServlet" method="post">
                <input type="hidden" name="action" value="login">
                <input class="form-control" type="text" name="username" placeholder="Vaše korisničko ime"><br/>
                <input class="form-control" type="password" name="password" placeholder="Vaša šifra"><br/>
                <input class="form-control btn btn-primary" type="submit" value="Uloguj se">
            </form>
            
        </div>
        
        <%@include file="include/footer.jsp" %>
    </body>
</html>
