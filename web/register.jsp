<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register Page</title>
    </head>
    <body>
    <center>
        <h3>Register</h3>
        <form method="POST" action="RegisterServlet">
            <table class="table-bordered">
                <thead>
                    <tr>
                        <th colspan="2">Enter your details</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>First Name: </td>
                        <td><input type="text" name="first_name" value="" /></td>
                    </tr>
                    <tr>
                        <td>Last Name: </td>
                        <td><input type="text" name="last_name" value="" /></td>
                    </tr>
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
            <input type="submit" class="btn btn-success" value="Register" />
            <p>Already registered? <a href="login.jsp">Login Here</a><p/>
    </center>
    <br>
    <jsp:include page="foot.jsp"/>
</form>
</body>
</html>