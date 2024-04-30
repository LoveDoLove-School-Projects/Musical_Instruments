package controllers;

import entities.TrustAllCertificates;
import entities.Enviroment;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.http.HttpServletRequest;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class InitServlet implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(InitServlet.class.getName());
    public static ServletContext servletContext;

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
            Enviroment.AES_KEY = (String) env.lookup(Enviroment.AES_KEY);
            Enviroment.SEND_MAIL_API = (String) env.lookup(Enviroment.SEND_MAIL_API);
            Enviroment.SECRET_KEY = (String) env.lookup(Enviroment.SECRET_KEY);
            Enviroment.ACCESS_TOKEN_API = (String) env.lookup(Enviroment.ACCESS_TOKEN_API);
            Enviroment.CREATE_PAYMENT_API = (String) env.lookup(Enviroment.CREATE_PAYMENT_API);
            TrustAllCertificates.trustAllCertificates();
            servletContext = servletContextEvent.getServletContext();
            LOG.info(getServerDirectoryRootPath());
        } catch (NamingException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Receives notification that the ServletContext is about to be destroyed.
     *
     * @param servletContextEvent the ServletContextEvent containing the
     * ServletContext that is being destroyed
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }

    /**
     * Returns the root directory path of the server.
     *
     * @return the root directory path of the server
     */
    public static String getServerDirectoryRootPath() {
        return getServerDirectoryPath("/");
    }

    /**
     * Returns the server directory path for the given URI.
     *
     * @param uri the URI for which the server directory path is to be obtained
     * @return the server directory path for the given URI
     */
    public static String getServerDirectoryPath(String uri) {
        return servletContext.getRealPath("/") + uri;
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
