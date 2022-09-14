package cn.it.learning.controller;

import cn.it.learning.model.User;
import cn.it.learning.util.complex_report.ComplexExcelReportUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @Author it-learning
 * @Description 复杂报表导出
 * @Date 2022/9/9 17:40
 * @Version 1.0
 */
@Slf4j
@RestController
public class TestComplexReportController {
    /**
     * @description: 复杂模板导出案例
     * @param:
     * @param: response
     * @return:
     * @author: it-learning-diary
     * @date: 2022/9/10 10:50
     */
    @RequestMapping("/complex/download")
    public void downloadComplexReport(HttpServletResponse response) throws Exception {
        HashMap<String, Object> hashMap = new HashMap<>();
        User user = new User();
        user.setId(1);
        user.setPassword("password");
        user.setName("name");
        hashMap.put("user", user);
        ComplexExcelReportUtil.exportReportByBrowser(response, "/template", "complex_report_word.ftl", hashMap, Boolean.FALSE, "demo.doc");
    }

}
