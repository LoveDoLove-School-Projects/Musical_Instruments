package utilities;

import entities.Environment;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public final class HttpUtilities {

    public static String sendHttpJsonRequest(String urlConnection, String jsonPayload) {
        HttpsURLConnection connection = null;
        try {
            URL url = new URL(urlConnection);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + Environment.SECRET_KEY);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.writeBytes(jsonPayload);
                outputStream.flush();
            }
            StringBuilder response;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
            int status = connection.getResponseCode();
            if (status != 200) {
                return null;
            }
            return response.toString();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
