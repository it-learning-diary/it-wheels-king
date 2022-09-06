package cn.it.learning;


import cn.it.learning.util.ftp.FtpUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author it-learning
 * @Description ftp工具测试类
 * @Date 2022/9/6 11:00
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class FtpUtilTest {

    @Autowired
    private FtpUtil ftpUtil;

    /***
    * 测试从ftp服务器下载文件到本地
    */
    @Test
    public void testFtpDownloadFile() throws Exception {
        FTPClient ftpClient = new FTPClient();
        ftpUtil.connect(ftpClient, "127.0.0.1", 21, "admin", "123");
        ftpUtil.downloadFileToDestination(ftpClient, "/data/ftp", "D:/ftptest/");
    }

}
