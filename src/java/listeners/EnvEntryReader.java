package listeners;

import domain.common.Enviroment;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EnvEntryReader implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            Context context = new InitialContext();
            Context env = (Context) context.lookup("java:comp/env");
            Enviroment.AES_KEY = (String) env.lookup(Enviroment.AES_KEY);
            Enviroment.MAIL_SERVER_API = (String) env.lookup(Enviroment.MAIL_SERVER_API);
            Enviroment.SECRET_KEY = (String) env.lookup(Enviroment.SECRET_KEY);
        } catch (NamingException ex) {
            Logger.getLogger(EnvEntryReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
