package listeners;

import domain.common.Enviroment;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import utilities.FileUtilities;

public class ServerListener implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(ServerListener.class.getName());
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
            servletContext = servletContextEvent.getServletContext();
            LOG.info(FileUtilities.getDirectoryPath());
        } catch (NamingException ex) {
            throw new RuntimeException("Error during JNDI lookup", ex);
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
}
