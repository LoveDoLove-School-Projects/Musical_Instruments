package controllers;

import entities.Environment;
import entities.TrustAllCertificates;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.http.HttpServletRequest;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class InitServlet implements ServletContextListener {

    /**
     * Called when the ServletContext is initialized. This method performs the
     * necessary initialization tasks for the server.
     *
     * @param servletContextEvent the ServletContextEvent object containing the
     * event details
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            Context context = new InitialContext();
            Context env = (Context) context.lookup("java:comp/env");
            Environment.AES_KEY = (String) env.lookup(Environment.AES_KEY);
            Environment.SEND_MAIL_API = (String) env.lookup(Environment.SEND_MAIL_API);
            Environment.SECRET_KEY = (String) env.lookup(Environment.SECRET_KEY);
            Environment.ACCESS_TOKEN_API = (String) env.lookup(Environment.ACCESS_TOKEN_API);
            Environment.CREATE_PAYMENT_API = (String) env.lookup(Environment.CREATE_PAYMENT_API);
            Environment.GET_PAYMENT_API = (String) env.lookup(Environment.GET_PAYMENT_API);
            Environment.EXECUTE_PAYMENT_API = (String) env.lookup(Environment.EXECUTE_PAYMENT_API);
            TrustAllCertificates.trustAllCertificates();
        } catch (NamingException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Returns the base URL of the server.
     *
     * @param request the HttpServletRequest object representing the client's
     * request
     * @return the base URL of the server
     */
    public static String getServerBaseURL(HttpServletRequest request) {
        String scheme = request.getScheme() + "://";
        String serverName = request.getServerName();
        String serverPort = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
        String contextPath = request.getContextPath();
        return scheme + serverName + serverPort + contextPath;
    }
}
