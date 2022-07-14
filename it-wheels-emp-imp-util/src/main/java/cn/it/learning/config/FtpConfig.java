package cn.it.learning.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author it
 * @Description ftp配置信息
 * @Date 2021/9/29 21:35
 * @Version 1.0
 */
@Component
@Data
public class FtpConfig {

    @Value("${ftp.client.charset}")
    private String clientCharset;
    @Value("${ftp.server.hostname}")
    private String serverHostname;
    @Value("${ftp.server.port}")
    private Integer serverPort;
    @Value("${ftp.server.username}")
    private String serverUsername;
    @Value("${ftp.server.password}")
    private String serverPassword;
    @Value("${ftp.server.workingPath}")
    private String serverWorkingPath;
    @Value("${ftp.server.charset}")
    private String serverCharset;

}