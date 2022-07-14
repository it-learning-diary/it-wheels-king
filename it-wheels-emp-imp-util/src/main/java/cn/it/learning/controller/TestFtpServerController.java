package cn.it.learning.controller;

import cn.it.learning.config.FtpConfig;
import cn.it.learning.model.test.ftp.FtpDownloadParam;
import cn.it.learning.model.test.ftp.FtpUploadParam;
import cn.it.learning.util.ftp.FtpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @Author it
 * @Description 测试程序实现ftp的上传下载功能
 * @Date 2021/9/29 21:31
 * @Version 1.0
 */
@Slf4j
@RestController
public class TestFtpServerController {

    @Autowired
    private FtpUtil ftpUtils;

    @Autowired
    private FtpConfig ftpConfig;

    @PostMapping("/ftp/upload")
    public void upload() {
        try {
            FtpUploadParam param = new FtpUploadParam();
            param.setHostname(ftpConfig.getServerHostname());
            param.setPort(ftpConfig.getServerPort());
            param.setUsername(ftpConfig.getServerUsername());
            param.setPassword(ftpConfig.getServerPassword());
            param.setWorkingPath(ftpConfig.getServerWorkingPath());
            param.setSaveName("测试程序上传文件到ftp服务器.mp3");
            // 删除D盘下的like到对应的FTP服务器上
            InputStream in = new FileInputStream(new File("D:/uploadfile/like.mp3"));
            param.setInputStream(in);
            ftpUtils.upload(param);
        } catch (Exception e) {
            log.error("TestFtpServerController upload 错误:{}", e);
        }
    }

    @PostMapping("/ftp/download")
    public void download() {
        try {
            FtpDownloadParam param = new FtpDownloadParam();
            param.setHostname(ftpConfig.getServerHostname());
            param.setPort(ftpConfig.getServerPort());
            param.setUsername(ftpConfig.getServerUsername());
            param.setPassword(ftpConfig.getServerPassword());
            param.setWorkingPath(ftpConfig.getServerWorkingPath());
            param.setDownloadPath("D:/downloadFile/");
            param.setFileName("从ftp服务器下载的音乐.mp3");
            ftpUtils.download(param, "测试程序上传文件到ftp服务器.mp3");
        } catch (Exception e) {
            log.error("TestFtpServerController download 错误:{}", e);
        }

    }

}