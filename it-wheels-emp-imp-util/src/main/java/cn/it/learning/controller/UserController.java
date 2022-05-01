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
     * 导入案例
     *
     * @param file
     * @throws Exception
     */
    @PostMapping("/user/upload")
    public void uploadUserList(@RequestBody MultipartFile file,String username) throws Exception {
        userService.uploadUserListDemo(file,username);
    }

    /**
     * 导出案例
     * @param response
     */
    @PostMapping("/user/export")
    public void exportUserList(HttpServletResponse response) {
        userService.exportUserListDemo(response);
    }

    /**
     * 导出案例
     * @param response
     */
    @PostMapping("/user/download/template")
    public void downloadUserTemplate(HttpServletResponse response,String filePath,String saveFileName) {
        userService.downloadTemplateDemo(filePath, saveFileName, response);
    }

}
