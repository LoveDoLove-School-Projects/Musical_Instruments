package services;

import domain.common.Common;
import domain.common.Enviroment;
import domain.request.MailRequest;
import utilities.AesUtilities;
import utilities.HttpUtilities;

public class MailServices {

    private final String secretKey = AesUtilities.aes256EcbDecrypt(Enviroment.SECRET_KEY);
    private final String sendMailApi = AesUtilities.aes256EcbDecrypt(Enviroment.SEND_MAIL_API);

    public Common.Status sendEmail(MailRequest mailRequest) {
        mailRequest.setSecretKey(secretKey);
        return HttpUtilities.sendHttpJsonRequest(sendMailApi, mailRequest);
    }
}
