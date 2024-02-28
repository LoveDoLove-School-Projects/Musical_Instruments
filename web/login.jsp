<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
    </head>
    <body>
    <center>
        <h3>Login</h3>
        <form method="POST" action="LoginServlet">
            <table class="table-bordered">
                <thead>
                    <tr>
                        <th colspan="2">Enter your details</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Email: </td>
                        <td><input type="text" name="email" value="" /></td>
                    </tr>
                    <tr>
                        <td>Password: </td>
                        <td><input type="password" name="password" value="" /></td>
                    </tr>
                </tbody>
            </table>
            <br>
            <input type="submit" class="btn btn-success" value="Login" />
            <p>No Account? <a href="register.jsp">Register Here</a><p/>
    </center>
    <br>
    <jsp:include page="foot.jsp"/>
</form>
</body>
</html>