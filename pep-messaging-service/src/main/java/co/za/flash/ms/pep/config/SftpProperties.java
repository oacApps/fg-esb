package co.za.flash.ms.pep.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(ignoreUnknownFields = false, prefix = "sftp.client")
public class SftpProperties {

    private String host;
    private Integer port;
    private String protocol;
    private String username;
    private String password;
    private String pepRoot;
    private String localTmpLoc;
    private String shopRiteRoot;
    private String privateKey;
    private String passphrase;
    private String sessionStrictHostKeyChecking;
    private Integer sessionConnectTimeout;
    private Integer ChannelConnectedTimeout;
}
