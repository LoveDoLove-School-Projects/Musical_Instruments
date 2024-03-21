package services;

import com.google.gson.Gson;
import domain.common.Common;
import domain.common.Enviroment;
import domain.request.MailRequest;
import domain.response.MailResponse;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import utilities.AesUtilities;

public class MailServices {

    private final Gson gson = new Gson();

    private final TrustManager[] trustAllCerts = new TrustManager[]{
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

    public MailResponse sendEmail(MailRequest mailRequest) {
        mailRequest.setSecretKey(AesUtilities.aes256EcbDecrypt(Enviroment.SECRET_KEY));
        MailResponse mailResponse = new MailResponse();
        String jsonPayload = gson.toJson(mailRequest);

        HttpsURLConnection connection = null;
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            URL url = new URL(AesUtilities.aes256EcbDecrypt(Enviroment.MAIL_SERVER_API));
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
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
            if (responseCode == 200) {
                mailResponse.setStatus(Common.Status.OK);
            } else {
                mailResponse.setStatus(Common.Status.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException | KeyManagementException | NoSuchAlgorithmException ex) {
            mailResponse.setStatus(Common.Status.INTERNAL_SERVER_ERROR);
            System.err.println(ex.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return mailResponse;
    }
}
