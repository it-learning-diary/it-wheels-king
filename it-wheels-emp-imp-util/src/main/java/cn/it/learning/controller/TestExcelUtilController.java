package cn.it.learning.controller;

import cn.it.learning.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: excel工具类导出测试入口
 * @Author: it-learning-diary
 * @Version: v1
 * @Date: 2023/2/10:14:21
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TestExcelUtilController {

    private final UserServiceImpl userService;

    /**
     * 导入excel案例
     *
     * @param file
     * @throws Exception
     */
    @PostMapping("/excel/multi-param/upload")
    public void uploadUserListWithExcel(@RequestBody MultipartFile file, String username) throws Exception {
        userService.testMultiParamImportExcelUtilDemo(file, username);
    }

}
