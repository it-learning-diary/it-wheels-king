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
     * 导出案例
     *
     * @param file
     * @throws Exception
     */
    @PostMapping("/user/upload")
    public void uploadUserList(@RequestBody MultipartFile file) throws Exception {
        userService.uploadUserList(file);
    }


    @PostMapping("/user/download")
    public void downloadUserList(HttpServletResponse response) {
        userService.downloadUserList(response);
    }
}
