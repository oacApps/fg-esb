package co.za.flash.credential.management.config;

import co.za.flash.credential.management.helper.interfaces.IConfigLoaded;
import co.za.flash.credential.management.helper.utils.MailSenderEnvReader;
import co.za.flash.credential.management.helper.utils.StringUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@ConfigurationProperties("spring.mail")
@Data
public class MailSenderConfig implements IConfigLoaded {
    private String host;
    private int port;

    @Override
    public boolean isLoaded() {
        return !StringUtil.isNullOrBlank(host) && port > 0;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        MailSenderEnvReader mailSenderEnvReader = new MailSenderEnvReader();
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.host);
        mailSender.setPort(this.port);

        mailSenderEnvReader.setUserNameToMailSender(mailSender);
        mailSenderEnvReader.setPasswordToMailSender(mailSender);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
