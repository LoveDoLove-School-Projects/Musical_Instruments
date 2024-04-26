package utilities;

import com.google.gson.Gson;
import domain.common.Common;
import domain.common.Enviroment;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpUtilities {

    private static final Logger LOG = Logger.getLogger(HttpUtilities.class.getName());
    private static final Gson gson = new Gson();
    private static final TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }
        }
    };

    public static Common.Status sendHttpJsonRequest(String urlConnection, Object object) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String jsonPayload = gson.toJson(object);
        String combineContent = timestamp + jsonPayload;
        String signature = AesUtilities.aes256EcbEncrypt(combineContent, Enviroment.SECRET_KEY);
        HttpURLConnection connection = null;
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
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
            return responseCode == 200 ? Common.Status.OK : Common.Status.INTERNAL_SERVER_ERROR;
        } catch (IOException | KeyManagementException | NoSuchAlgorithmException ex) {
            LOG.severe(ex.getMessage());
            return Common.Status.INTERNAL_SERVER_ERROR;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static Common.Status sendHttpJsonRequest(String urlConnection, String jsonPayload) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String combineContent = timestamp + jsonPayload;
        String signature = AesUtilities.aes256EcbEncrypt(combineContent, Enviroment.SECRET_KEY);
        HttpsURLConnection connection = null;
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
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
            return responseCode == 200 ? Common.Status.OK : Common.Status.INTERNAL_SERVER_ERROR;
        } catch (IOException | KeyManagementException | NoSuchAlgorithmException ex) {
            LOG.severe(ex.getMessage());
            return Common.Status.INTERNAL_SERVER_ERROR;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
