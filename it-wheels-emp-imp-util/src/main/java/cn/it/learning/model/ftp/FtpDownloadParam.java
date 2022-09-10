package cn.it.learning.model.ftp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author it
 * @Description FTP下载参数类
 * @Date 2022/7/12 21:10
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FtpDownloadParam {
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
     *
     */
    private String downloadPath;
    /**
     * 下载的之后的文件名
     */
    private String fileName;
}