package cn.it.learning.util.file_system;

import cn.hutool.core.util.StrUtil;
import cn.it.learning.config.SeaweedFsConfig;
import cn.it.learning.constant.CommonConstant;
import cn.it.learning.util.browser.BrowserAdapterUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.entity.ContentType;
import org.lokra.seaweedfs.core.FileSource;
import org.lokra.seaweedfs.core.FileTemplate;
import org.lokra.seaweedfs.core.file.FileHandleStatus;
import org.lokra.seaweedfs.core.http.StreamResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * @Author it
 * @Description 文件服务上传工具类
 * @Date 2022/6/28 16:32
 * @Version 1.0
 */
@Slf4j
@Component
public class SeaweedFsUtil {

    @Autowired
    private SeaweedFsConfig seaweedFsConfig;

    /**
     * 表单形式文件传输类型
     */
    private ContentType contentType = ContentType.create("application/x-www-form-urlencoded", Consts.UTF_8);

    /**
     * @description: 获取文件操作对象
     * @author: it
     * @date: 2022/7/14 18:37
     */
    private FileSource getFileSource() {
        FileSource fileSource = new FileSource();
        fileSource.setHost(seaweedFsConfig.getHost());
        fileSource.setPort(seaweedFsConfig.getPort());
        fileSource.setConnectionTimeout(1000);
        fileSource.setIdleConnectionExpiry(200);
        try {
            fileSource.startup();
        } catch (Exception e) {
            log.error("SeaweedFsUtil getFileSource in error:{}", e);
        }
        return fileSource;
    }

    /**
     * @description: 上传单个文件到文件服务器
     * @param: file
     * @return: 文件的fid + 文件的请全访问地址
     * @author: it
     */
    public String uploadFile(MultipartFile file) throws Exception {
        FileSource fileSource = getFileSource();
        FileTemplate fileTemplate = new FileTemplate(fileSource.getConnection());
        // 上传文件
        FileHandleStatus handleStatus = fileTemplate.saveFileByStream(file.getOriginalFilename(), file.getInputStream(), contentType);
        // 获取上传文件的访问地址
        String fileUrl = fileTemplate.getFileUrl(handleStatus.getFileId());
        // 关闭当前连接
        fileSource.shutdown();
        return handleStatus.getFileId() + StrUtil.DASHED + fileUrl;
    }

    /**
     * @description: 根据文件ID删除文件
     * @author: it
     */
    public void deleteFileByFid(String fileId) throws Exception {
        FileSource fileSource = getFileSource();
        FileTemplate fileTemplate = new FileTemplate(fileSource.getConnection());
        fileTemplate.deleteFile(fileId);
        fileSource.shutdown();
    }

    /**
     * @description: 根据文件下载文件
     * @param: fid
     * @param: response
     * @param: fileName
     * @author: it
     */
    public void downloadFileByFid(HttpServletResponse response, HttpServletRequest request, String fid, String fileName) throws Exception {
        FileSource fileSource = getFileSource();
        FileTemplate fileTemplate = new FileTemplate(fileSource.getConnection());
        StreamResponse fileStream = fileTemplate.getFileStream(fid);
        // 解决跨域问题
//        response.addHeader("Access-Control-Allow-Origin", "*");
//        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//        response.addHeader("Access-Control-Allow-Headers",
//                "Content-Type,X-Requested-With,accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers,token");
        // 设置响应头
        response.setContentType(CommonConstant.CONTENT_TYPE);
        response.setCharacterEncoding(CommonConstant.UTF_8);
        String encodeFileName = BrowserAdapterUtil.buildingFileNameAdapterBrowser(request, fileName);
        response.setHeader(CommonConstant.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + encodeFileName);
        // 读取并写入到响应输出
        InputStream inputStream = fileStream.getInputStream();
        byte[] fileByte = new byte[inputStream.available()];
        inputStream.read(fileByte);
        response.getOutputStream().write(fileByte);
        response.getOutputStream().flush();
        fileSource.shutdown();
    }


}
