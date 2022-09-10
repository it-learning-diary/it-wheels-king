package cn.it.learning.model.ftp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @Author it
 * @Description FTP上传参数模拟类
 * @Date 2022/7/12 21:10
 * @Version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FtpUploadParam {
    /**
     * ip或域名地址
     */
    private String hostname;
    /**
     * 端口
     */
    private Integer port;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 上传的服务器的工作目录
     */
    private String workingPath;
    /**
     * 上传文件的输入流
     */
    private InputStream inputStream;
    /**
     * 上传之后的文件名
     */
    private String saveName;
}