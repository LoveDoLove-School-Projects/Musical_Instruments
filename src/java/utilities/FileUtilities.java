package utilities;

import listeners.ServerListener;

public class FileUtilities {

    public static String getDirectoryPath() {
        return ServerListener.servletContext.getRealPath("/");
    }
}
