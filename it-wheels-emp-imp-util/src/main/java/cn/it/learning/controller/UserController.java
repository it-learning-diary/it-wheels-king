package cn.it.learning.controller;

import cn.it.learning.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author it-leaning-diary
 * @Description 测试导入数据用例
 * @Date 2022/4/25 10:04
 * @Version 1.0
 */
@RestController
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    /**
     * 导入excel案例
     *
     * @param file
     * @throws Exception
     */
    @PostMapping("/user/upload")
    public void uploadUserListWithExcel(@RequestBody MultipartFile file, String username) throws Exception {
        userService.uploadUserListDemoWithExcel(file, username);
    }

    /**
     * 导出excel案例
     *
     * @param response
     */
    @PostMapping("/user/export")
    public void exportUserListWithExcel(HttpServletResponse response) {
        userService.exportUserListDemoWithExcel(response);
    }

    /**
     * 场景: 表头不固定，动态导出数据
     * 动态导出导出excel案例
     *
     * @param response
     */
    @PostMapping("/user/export/dynamic/excel")
    public void exportUserDynamicListWithExcel(HttpServletResponse response) {
        userService.exportUserListDynamicDemoWithExcel(response);
    }

    /**
     * 导出指定路径下模板案例
     *
     * @param response
     * @param filePath     文件所在路径(包含模板名称)：一般是放在项目的resources目录下的temolate
     * @param fileSuffix   导出的文件后缀如xlsx、csv等
     * @param saveFileName 下载后的模板名称如demo
     */
    @PostMapping("/user/download/template")
    public void downloadUserTemplate(HttpServletResponse response, String filePath, String saveFileName, String fileSuffix) {
        userService.downloadTemplateDemo(filePath, saveFileName, fileSuffix, response);
    }

    /**
     * csv导出案例
     *
     * @param response
     */
    @PostMapping("/user/export/csv")
    public void exportUserListWithCsv(HttpServletResponse response) {
        userService.exportUserListWithCsv(response);
    }

    /**
     * csv导入案例
     *
     * @param file
     * @throws Exception
     */
    @PostMapping("/user/upload/csv")
    public void uploadUserListWithCsv(@RequestBody MultipartFile file) throws Exception {
        userService.uploadUserListWithCsv(file);
    }

}
