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

    /**
     * Writes the given content to a file at the specified path. If the file
     * does not exist, it will be created.
     *
     * @param path the path of the file to write to
     * @param content the content to write to the file
     * @return true if the content was successfully written to the file, false
     * otherwise
     */
    public static boolean writeContentToFile(String path, byte[] content) {
        String fullPath = ServerListener.getServerDirectoryRootPath() + path;
        File file = new File(fullPath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[1024];
                while ((fis.read(buffer)) != -1) {
                    content = buffer;
                }
            }
            return true;
        } catch (IOException ex) {
            LOG.severe(ex.getMessage());
            return false;
        }
    }
}
