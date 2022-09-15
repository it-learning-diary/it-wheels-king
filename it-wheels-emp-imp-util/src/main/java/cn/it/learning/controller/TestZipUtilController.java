package cn.it.learning.controller;

import cn.it.learning.util.zip.ZipUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @Author it
 * @Description zip压缩包工具测试
 * @Date 2022/9/15 17:05
 * @Version 1.0
 */
@RestController
public class TestZipUtilController {

    /***
    * 压缩给定文件并导出
    */
    @PostMapping("/zip/export_confirm_files")
    public void compressConfirmFilesToZip(HttpServletResponse response,String file) throws Exception {
        ZipUtil.exportZipByFiles(response, "demohello.zip", new File(file));
    }

    /***
    * 压缩给定路径下的所有文件到zip并导出
    */
    @PostMapping("/zip/export_filepath_files")
    public void compressFilePathAllFileToZip(HttpServletResponse response,String filePath) throws Exception {
        ZipUtil.exportZipFileByFilePath(response,filePath,"demo2222.zip");
    }
}
