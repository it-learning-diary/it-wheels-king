package cn.it.learning;

import cn.it.learning.util.zip.ZipUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.nio.charset.Charset;
import java.util.zip.ZipFile;

/**
 * @Author it-learning
 * @Description zip解压
 * @Date 2022/9/16 14:17
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ZipUtilTest {

    @Test
    public void decompressTest() throws Exception{
        // 说明：new ZipFile时需要根据程序所在的环境指定对应的字符集，比如Window环境下，默认为GBK字符集，而ZipFile默认为UTF-8，当文件名存在中文时，如果不指定程序就会报错
        ZipUtil.decompressZipFile("D:/ziptest2/",new ZipFile("D:/ziptest/ziptest.zip", Charset.forName("GBK")));
        // hutool工具自带的解压API，实现原理基本类似
        cn.hutool.core.util.ZipUtil.unzip(new File("D:/ziptest/ziptest.zip"),new File("D:/ziptest3/"),Charset.forName("GBK"));
    }

}
