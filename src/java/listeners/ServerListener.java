package listeners;

import domain.common.Enviroment;
import exceptions.JndiLookupException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import utilities.FileUtilities;

public class ServerListener implements ServletContextListener {

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
            System.out.println(FileUtilities.getDirectoryPath());
        } catch (NamingException ex) {
            throw new JndiLookupException("Error during JNDI lookup", ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
