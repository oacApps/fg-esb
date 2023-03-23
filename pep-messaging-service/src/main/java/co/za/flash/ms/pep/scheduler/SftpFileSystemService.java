package co.za.flash.ms.pep.scheduler;

import co.za.flash.ms.pep.config.SftpProperties;
import com.jcraft.jsch.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.connection.SimpleResourceHolder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.Arrays;

public class SftpFileSystemService {

    @Autowired
    public SftpProperties config;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    Logger LOGGER = LoggerFactory.getLogger(SftpFileSystemService.class);

    // Set the prompt when logging in for the first time. Optional value: (ask | yes | no)
    private static final String SESSION_CONFIG_STRICT_HOST_KEY_CHECKING = "StrictHostKeyChecking";



    public SftpFileSystemService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    public SftpFileSystemService(){}

    protected ChannelSftp createSftp() throws Exception {
        JSch jsch = new JSch();
        LOGGER.info("Try to connect sftp[" + config.getUsername() + "@" + config.getHost() + "], use password[" + config.getPassword() + "]");

        Session session = createSession(jsch, config.getHost(), config.getUsername(), config.getPort());

        if(!session.isConnected()) {
            session.setConfig("PreferredAuthentications", "password");
            session.setPassword(config.getPassword());
            try {
                session.connect(config.getSessionConnectTimeout());
            } catch (Exception e){
                LOGGER.error("Failed to connect FTP server: " + e);
            }
        }
        LOGGER.info("Session connected to {}.", config.getHost());
        Channel channel = session.openChannel(config.getProtocol());
        channel.connect(config.getChannelConnectedTimeout());
        LOGGER.info("Channel created to {}.", config.getHost());
        return (ChannelSftp) channel;
    }

    /**
     *
     * @param rootPath - location
     * @param fileName
     * @return
     * @throws Exception
     */
    public BufferedReader downloadFileContent(String rootPath, String fileName) throws Exception, IOException {
        ChannelSftp sftp = this.createSftp();
        OutputStream outputStream = null;
        BufferedReader fileReader = null;
        try {
            sftp.cd(rootPath);
            LOGGER.info("Change path to {}", rootPath);

            File file = new File(fileName.substring(fileName.lastIndexOf("/") + 1));
            outputStream = new FileOutputStream(file);
            sftp.get(fileName, outputStream);
            fileReader = new BufferedReader(new FileReader(file));
        } catch (IOException e) {
            LOGGER.error("IOException. TargetPath: {}", fileName, e);
            throw new Exception("File read failure");
        } catch (Exception e) {
            LOGGER.error("Download file failure. TargetPath: {}", fileName, e);
            throw new Exception("Download File failure");
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            this.disconnect(sftp);
        }
        return fileReader;
    }
    /**
     * Create session
     * @param jsch
     * @param host
     * @param username
     * @param port
     * @return
     * @throws Exception
     */
    private Session createSession(JSch jsch, String host, String username, Integer port) throws Exception {
        Session session = null;

        if (port <= 0) {
            session = jsch.getSession(username, host);
        } else {
            session = jsch.getSession(username, host, port);
        }

        if (session == null) {
            throw new Exception(host + " session is null");
        }

        session.setConfig(SESSION_CONFIG_STRICT_HOST_KEY_CHECKING, config.getSessionStrictHostKeyChecking());
        return session;
    }

    /**
     * Close the connection
     * @param sftp
     */
    public void disconnect(ChannelSftp sftp) {
        try {
            if (sftp != null) {
                if (sftp.isConnected()) {
                    sftp.disconnect();
                } else if (sftp.isClosed()) {
                    LOGGER.info("sftp is closed already");
                }
                if (null != sftp.getSession()) {
                    sftp.getSession().disconnect();
                }
            }
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    public boolean uploadFile(String targetPath, InputStream inputStream) throws Exception {
        ChannelSftp sftp = this.createSftp();
        try {
            sftp.cd(config.getPepRoot());
            LOGGER.info("Change path to {}", config.getPepRoot());

            int index = targetPath.lastIndexOf("/");
            String fileDir = targetPath.substring(0, index);
            String fileName = targetPath.substring(index + 1);
            boolean dirs = this.createDirs(fileDir, sftp);
            if (!dirs) {
                LOGGER.error("Remote path error. path:{}", targetPath);
                throw new Exception("Upload File failure");
            }
            sftp.put(inputStream, fileName);
            return true;
        } catch (Exception e) {
            LOGGER.error("Upload file failure. TargetPath: {}", targetPath, e);
            throw new Exception("Upload File failure");
        } finally {
            this.disconnect(sftp);
        }
    }

    private boolean createDirs(String dirPath, ChannelSftp sftp) {
        if (dirPath != null && !dirPath.isEmpty()
                && sftp != null) {
            String[] dirs = Arrays.stream(dirPath.split("/"))
                    .filter(StringUtils::isNotBlank)
                    .toArray(String[]::new);

            for (String dir : dirs) {
                try {
                    sftp.cd(dir);
                    LOGGER.info("Change directory {}", dir);
                } catch (Exception e) {
                    try {
                        sftp.mkdir(dir);
                        LOGGER.info("Create directory {}", dir);
                    } catch (SftpException e1) {
                        LOGGER.error("Create directory failure, directory:{}", dir, e1);
                        e1.printStackTrace();
                    }
                    try {
                        sftp.cd(dir);
                        LOGGER.info("Change directory {}", dir);
                    } catch (SftpException e1) {
                        LOGGER.error("Change directory failure, directory:{}", dir, e1);
                        e1.printStackTrace();
                    }
                }
            }
            return true;
        }
        return false;
    }

    public boolean uploadFile(String targetPath, File file) throws Exception {
        return this.uploadFile(targetPath, new FileInputStream(file));
    }
    /**
     * Delete Files
     * @param targetPath
     * @return
     * @throws Exception
     */
    public boolean deleteFile(String targetPath) throws Exception {
        ChannelSftp sftp = null;
        try {
            sftp = this.createSftp();
            sftp.cd(config.getPepRoot());
            sftp.rm(targetPath);
            return true;
        } catch (Exception e) {
            LOGGER.error("Delete file failure. TargetPath: {}", targetPath, e);
            throw new Exception("Delete File failure");
        } finally {
            this.disconnect(sftp);
        }
    }
}
