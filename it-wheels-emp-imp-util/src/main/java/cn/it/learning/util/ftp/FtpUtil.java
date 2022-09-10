package cn.it.learning.util.ftp;

import cn.it.learning.model.ftp.FtpDownloadParam;
import cn.it.learning.model.ftp.FtpUploadParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * @Author it
 * @Description ftp工具类
 * @Date 2022/7/12 21:01
 * @Version 1.0
 */
@Slf4j
@Component
public class FtpUtil {
    /**
     * ftp客户端文件使用的字符集
     */
    @Value("${ftp.client.charset}")
    private String ftpClientCharset;
    /**
     * ftp服务器文件使用的字符集，FTP协议里面，规定文件名编码为iso-8859-1
     */
    @Value("${ftp.server.charset}")
    private String ftpServerCharset;

    /**
     * @description: 测试连接ftp是否成功
     * @param:
     * @param: ftpClient ftp链接对象
     * @param: hostname 主机名
     * @param: port 端口
     * @param: username 用户名
     * @param: password 密码
     * @return:
     * @author: it-learning-diary
     * @date: 2022/9/10 17:31
     */
    public Boolean connect(FTPClient ftpClient, String hostname, Integer port, String username, String password) {
        boolean flag = false;
        try {
            // ftp初始化的一些参数
            ftpClient.connect(hostname, port);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            if (ftpClient.login(username, password)) {
                flag = true;
            } else {
                disconnect(ftpClient);
                log.info("FtpUtils connect unsuccessfully in error:{}");
            }
            return flag;
        } catch (Exception e) {
            log.error("FtpUtils connect in error:{}!", e);
            return flag;
        }
    }

    /**
     * @description: 断开ftp连接
     * @param:
     * @param: ftpClient ftp客户端连接对象
     * @return:
     * @author: it-learning-diary
     * @date: 2022/9/10 17:32
     */
    public void disconnect(FTPClient ftpClient) {
        if (ftpClient.isConnected()) {
            try {
                ftpClient.disconnect();
                log.info("FtpUtils disconnect successfully!");
            } catch (IOException e) {
                log.error("FtpUtils disconnect unsuccessfully!");
            }
        }
    }


    /**
     * @description: 上传w文件
     * @param:
     * @param: param 上传参数
     * @return:
     * @author: it-learning-diary
     * @date: 2022/9/10 17:32
     */
    public boolean upload(FtpUploadParam param) {
        boolean flag = false;
        FTPClient ftpClient = new FTPClient();
        //1 测试连接
        if (connect(ftpClient, param.getHostname(), param.getPort(), param.getUsername(), param.getPassword())) {
            try {
                //2 检查工作目录是否存在，不存在则创建
                if (!ftpClient.changeWorkingDirectory(param.getWorkingPath())) {
                    ftpClient.makeDirectory(param.getWorkingPath());
                }
                // 将文件编码成Ftp服务器支持的编码类型（FTP协议里面，规定文件名编码为iso-8859-1，所以目录名或文件名需要转码。）
                String fileName = new String(param.getSaveName().getBytes(ftpClientCharset), ftpServerCharset);
                // 3 上传文件
                if (ftpClient.storeFile(fileName, param.getInputStream())) {
                    flag = true;
                } else {
                    log.warn("FtpUtils uploadFile unsuccessfully!!");
                }
            } catch (IOException e) {
                log.error("FtpUtils upload in error:{}", e);
            } finally {
                disconnect(ftpClient);
            }
        }
        return flag;
    }

    /**
     * @description: 下载ftp文件
     * @param:
     * @param: param 下载参数
     * @param: downloadFileName 需要下载的文件名称
     * @return:
     * @date: 2022/7/14 10:56
     */
    public boolean download(FtpDownloadParam param, String downloadFileName) {
        FTPClient ftpClient = new FTPClient();
        FileOutputStream out = null;
        boolean downloadResult = false;
        //1 测试连接
        if (connect(ftpClient, param.getHostname(), param.getPort(), param.getUsername(), param.getPassword())) {
            try {
                String localPath = param.getDownloadPath() + param.getFileName();
                out = new FileOutputStream(new File(localPath));
                //2 检查工作目录是否存在，不存在返回
                // if (!ftpClient.changeWorkingDirectory(param.getWorkingPath())) {
                //    return false;
                // }
                /*
                 * 打开FTP服务器的PASS模式(不记得FTP协议支持的模式请翻到文章第一阶段)
                 * 这个方法的意思就是每次数据连接之前，ftp client告诉ftp server开通一个端口来传输数据. 因为ftp
                 * server可能每次开启不同的端口来传输数据，但是在linux上，由于安全限制，可能某些端口没有开启，可能出现出现阻塞
                 */
                ftpClient.enterLocalPassiveMode();
                // 设置文件的传输方式-二进制
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                // 将文件编码成Ftp服务器支持的编码类型（FTP协议里面，规定文件名编码为iso-8859-1，所以目录名或文件名需要转码。）
                // 缺少编码转换会导致：从FTP服务器下载下来的文件是破损的，无法被打开
                downloadResult = ftpClient.retrieveFile(new String(downloadFileName
                        .getBytes(ftpClientCharset), ftpServerCharset), out);
                out.flush();
            } catch (IOException e) {
                log.error("FtpUtils upload in error:{}", e);
                return false;
            } finally {
                try {
                    if (Objects.nonNull(out)) {
                        out.close();
                    }
                } catch (Exception e) {
                    log.error("FtpUtils upload in error:{}", e);
                }
                disconnect(ftpClient);
            }
        }
        return downloadResult;
    }

    /**
     * @description: 从ftp服务器下载文件到目标地址上
     * @param: ftpClient ftp客户端
     * @param: targetPath 下载地址
     * @param: destinationPath 保存地址
     * @return: 下载结果
     * @author: it-learning
     * @date: 2022/8/26 17:05
     */
    public Boolean downloadFileToDestination(FTPClient ftpClient, String downloadPath, String savePath) throws Exception {
        Boolean result = null;
        FileOutputStream outputStream = null;
        // 切换到目标目录
        ftpClient.changeWorkingDirectory(downloadPath);
        // 读取到目标的所有文件，并下载到指定目录
        FTPFile[] ftpFiles = ftpClient.listFiles();
        try {
            for (FTPFile ftpFile : ftpFiles) {
                outputStream = new FileOutputStream(new File(savePath + ftpFile.getName()));
                //result = ftpClient.retrieveFile(new String(ftpFile.getName().getBytes("GBK"),"ISO-8859-1"), outputStream);
                result = ftpClient.retrieveFile(ftpFile.getName(), outputStream);
                outputStream.flush();
                outputStream.close();
            }
        } finally {
            if (Objects.nonNull(outputStream)) {
                outputStream.close();
            }
        }
        return result;
    }
}
