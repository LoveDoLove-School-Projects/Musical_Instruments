package services;

import domain.common.Common;
import domain.common.Enviroment;
import domain.request.MailRequest;
import features.AesHandler;
import utilities.HttpUtilities;

public class MailServices {

    private static final String SEND_MAIL_API = AesHandler.aes256EcbDecrypt(Enviroment.SEND_MAIL_API);

    public Common.Status sendEmail(MailRequest mailRequest) {
        return HttpUtilities.sendHttpJsonRequest(SEND_MAIL_API, mailRequest);
    }
}
