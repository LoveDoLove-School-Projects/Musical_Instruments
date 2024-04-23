package utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;
import listeners.ServerListener;

public class FileUtilities {

    private static final Logger LOG = Logger.getLogger(FileUtilities.class.getName());

    /**
     * Reads the content of a file located at the specified path and returns it
     * as a byte array.
     *
     * @param path the path of the file to read
     * @return the content of the file as a byte array
     */
    public static byte[] readDirectoryContent(String path) {
        String fullPath = ServerListener.getServerDirectoryRootPath() + path;
        File file = new File(fullPath);
        ByteArrayOutputStream content = new ByteArrayOutputStream();
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                content.write(buffer, 0, length);
            }
            return content.toByteArray();
        } catch (IOException ex) {
            LOG.severe(ex.getMessage());
            return null;
        }
    }
}
