package utilities;

import com.google.gson.Gson;
import environments.Enviroment;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;

public final class HttpUtilities {

    private static final Logger LOG = Logger.getLogger(HttpUtilities.class.getName());
    private static final Gson gson = new Gson();

    public static String sendHttpJsonRequest(String urlConnection, Object object) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String jsonPayload = gson.toJson(object);
        String combineContent = timestamp + jsonPayload;
        String signature = AesUtilities.aes256EcbEncrypt(combineContent, Enviroment.SECRET_KEY);
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlConnection);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("timestamp", timestamp);
            connection.setRequestProperty("signature", signature);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.writeBytes(jsonPayload);
                outputStream.flush();
            }
            int responseCode = connection.getResponseCode();
            StringBuilder response;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
            System.out.println("Response Code: " + responseCode);
            System.out.println("Response Body: " + response.toString());
            return response.toString();
        } catch (IOException ex) {
            LOG.severe(ex.getMessage());
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String sendHttpJsonRequest(String urlConnection, String jsonPayload) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String combineContent = timestamp + jsonPayload;
        String signature = AesUtilities.aes256EcbEncrypt(combineContent, Enviroment.SECRET_KEY);
        HttpsURLConnection connection = null;
        try {
            URL url = new URL(urlConnection);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("timestamp", timestamp);
            connection.setRequestProperty("signature", signature);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.writeBytes(jsonPayload);
                outputStream.flush();
            }
            int responseCode = connection.getResponseCode();
            StringBuilder response;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
            System.out.println("Response Code: " + responseCode);
            System.out.println("Response Body: " + response.toString());
            return response.toString();
        } catch (IOException ex) {
            LOG.severe(ex.getMessage());
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
