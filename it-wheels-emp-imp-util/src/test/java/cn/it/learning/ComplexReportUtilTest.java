package cn.it.learning;

import cn.it.learning.model.User;
import cn.it.learning.util.complex_report.ComplexExcelReportUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;

/**
 * @Author it-learning
 * @Description 复杂报表导出示例
 * @Date 2022/9/9 15:23
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ComplexReportUtilTest {

    /***
     * 复杂报表导出
     * 1、在电脑上创建一个模板如：xlsx
     * 2、在模板上编写好对应的占位符，然后另存为xml格式
     */
    @Test
    public void complexReportExportTest() throws Exception {
        HashMap<String, Object> hashMap = new HashMap<>();
        User user = new User();
        user.setId(1);
        user.setPassword("password");
        user.setName("name");
        hashMap.put("user",user);
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("D:/freemarker/demo.xlsx"));
        ComplexExcelReportUtil.exportComplexByTemplate("/template", "complex_report_excel.ftl", hashMap, writer, Boolean.TRUE);
    }

    /**
     * Html模板导出测试用例
     * @throws Exception
     */
    @Test
    public void complexReportExportHtmlTest() throws Exception{
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("title","这个是标题");
        hashMap.put("body","因为你喜欢海，所以我一直浪。");
        hashMap.put("span","IT学习日记：InfoQ(极客邦)&阿里云签约作者，开源项目300+Star，专注输出JAVA、数据库、算法、服务器等领域优质文章！博客地址：https://blog.csdn.net/qq_40891009");
        hashMap.put("path","D://壁纸//5.png");
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("D:/freemarker/demo.html"));
        ComplexExcelReportUtil.exportComplexByTemplate("/template", "complex_report_html.html", hashMap, writer, Boolean.TRUE);
    }

}
