package co.za.flash.credential.management.helper.utils;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MailSenderEnvReader {
    private String userName = "";
    private String password = "";

    public MailSenderEnvReader() {
        try {
            Map<String, String> env = System.getenv();
            this.userName = env.get("CM_MAIL_SENDER_USERNAME");
            this.password = env.get("CM_MAIL_SENDER_PASSWORD");
        } catch (Exception e) {}
    }

    public String getUserName() {
        return userName;
    }

    public void setUserNameToMailSender(JavaMailSenderImpl mailSender) {
        mailSender.setUsername(this.userName);
    }

    public void setPasswordToMailSender(JavaMailSenderImpl mailSender) {
        mailSender.setPassword(this.password);
    }
}
