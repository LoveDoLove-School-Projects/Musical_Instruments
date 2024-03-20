package infrastructure.services;

import domain.common.Common;
import domain.response.MailResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MailServices {

    private final String MAIL_SERVER_API = "https://lovedolove-mailserver.vercel.app/sendEmail";

    private final String SECRET_KEY = "b4fb9ad7b2b95413399ba07326fa58827b90f28070f4e9bf8572344e4d0e6f57";

    public MailResponse sendEmail(String toEmail, String subject, String body) {
        MailResponse mailResponse = new MailResponse();
        String json = String.format("{\"toEmail\": \"%s\", \"subject\": \"%s\", \"body\": \"%s\", \"secretKey\": \"%s\"}",
                toEmail, subject, body, SECRET_KEY);

        try {
            URL url = new URL(MAIL_SERVER_API);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(json.getBytes(StandardCharsets.UTF_8));
            outputStream.close();
            conn.disconnect();
            mailResponse.setStatus(Common.Status.OK);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            mailResponse.setStatus(Common.Status.INTERNAL_SERVER_ERROR);
        }
        return mailResponse;
    }
}
