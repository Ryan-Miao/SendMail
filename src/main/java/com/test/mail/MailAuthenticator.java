package com.test.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by Ryan on 2017/2/17/017.
 */
public class MailAuthenticator extends Authenticator {

    private String username;
    private String password;

    public MailAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }

}
