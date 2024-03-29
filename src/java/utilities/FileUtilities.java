package utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import listeners.ServerListener;

public class FileUtilities {

    public static String getDirectoryPath() {
        return ServerListener.servletContext.getRealPath("/");
    }

    public static byte[] readDirectoryContent(String path) {
        String fullPath = getDirectoryPath() + path;
        File file = new File(fullPath);
        ByteArrayOutputStream content = new ByteArrayOutputStream();
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                content.write(buffer, 0, length);
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return content.toByteArray();
    }
}
