package services;

import domain.common.Common;
import domain.common.Enviroment;
import domain.request.MailRequest;
import utilities.AesUtilities;
import utilities.HttpUtilities;

public class MailServices {

    private static final String SECRET_KEY = AesUtilities.aes256EcbDecrypt(Enviroment.SECRET_KEY);
    private static final String SEND_MAIL_API = AesUtilities.aes256EcbDecrypt(Enviroment.SEND_MAIL_API);

    public Common.Status sendEmail(MailRequest mailRequest) {
        mailRequest.setSecretKey(SECRET_KEY);
        return HttpUtilities.sendHttpJsonRequest(SEND_MAIL_API, mailRequest);
    }
}
