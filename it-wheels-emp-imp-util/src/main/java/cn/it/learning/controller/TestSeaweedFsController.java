package cn.it.learning.controller;

import cn.it.learning.util.file_system.SeaweedFsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author it
 * @Description seaweedfs功能入口
 * @Date 2022/7/14 16:59
 * @Version 1.0
 */
@Slf4j
@Controller
@RequestMapping("file_system/")
public class TestSeaweedFsController {

    @Autowired
    private SeaweedFsUtil seaweedFsUtil;


    /**
     * @description: 上传文件
     * @param:
     * @param: file
     * @return:
     * @author: it
     * @date: 2022/7/14 17:01
     */
    @ResponseBody
    @RequestMapping("upload")
    public void uploadFile(MultipartFile file) {
        try {
            String fileUrl = seaweedFsUtil.uploadFile(file);
            System.out.println(fileUrl);
        } catch (Exception e) {
            log.error("TestSeaweedFsController uploadFile in error:{}", e);
        }
    }

    /**
     * @description: 下载文件
     * @param:
     * @param: fileId
     * @return:
     * @author: it
     * @date: 2022/7/14 17:01
     */
    @RequestMapping("download")
    public void downloadFile(HttpServletResponse response, HttpServletRequest request, String fileId, String fileName) {
        try {
            fileId = "3,03c59eb9f1";
            fileName = "中文标题.png";
            seaweedFsUtil.downloadFileByFid(response, request, fileId, fileName);
        } catch (Exception e) {
            log.error("TestSeaweedFsController downloadFile in error:{}", e);
        }
    }

    /**
     * @description: 删除文件
     * @param:
     * @param: fileId
     * @return:
     * @author: it
     * @date: 2022/7/14 17:02
     */
    @ResponseBody
    @RequestMapping("delete")
    public void deleteFile(String fileId) {
        try {
            seaweedFsUtil.deleteFileByFid(fileId);
        } catch (Exception e) {
            log.error("TestSeaweedFsController downloadFile in error:{}", e);
        }
    }

}
