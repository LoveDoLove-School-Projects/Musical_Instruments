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
            LOG.severe(ex.getMessage());
            throw new RuntimeException("Error during JNDI lookup", ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
