package cn.it.learning.util.zip;

import cn.hutool.core.util.StrUtil;
import cn.it.learning.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Enumeration;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @Author it-diary-learning
 * @Description 压缩工具类
 * @Date 2022/9/15 14:50
 * @Version 1.0
 */
@Slf4j
public class ZipUtil {

    /**
     * @description: 压缩给定文件(可以是多个)到zip中并导出
     * @param:
     * @param: response 响应流
     * @param: exportFileName 导出时的文件名称，如demo.zip
     * @param: files 需要压缩的文件
     * @return:
     * @author: it-learning
     * @date: 2022/9/15 16:55
     */
    public static void exportZipByFiles(HttpServletResponse response, String exportFileName, File... files) throws Exception {
        ZipOutputStream outputStream = new ZipOutputStream(response.getOutputStream());
        BufferedInputStream inputStream = null;
        try {
            // 封装响应流参数
            buildZipResponseParam(response, exportFileName);
            addFileToZip(outputStream, inputStream, files);
        } catch (Exception e) {
            log.error("ZipUtil exportZipByFiles in error:{}", e);
        } finally {
            if (Objects.nonNull(inputStream)) {
                inputStream.close();
            }
            // 必须调用finish，否则导出的压缩文件会是损坏的
            outputStream.finish();
        }
    }

    /**
     * @description: 打包并以zip方式导出指定目录下的所有文件
     * @param: response 响应流
     * @param: filePath 需要压缩的文件路径
     * @param: exportFileName 导出时的文件名称，如demo.zip
     * @author: it-diary-learning
     * @date: 2022/9/15 14:51
     */
    public static void exportZipFileByFilePath(HttpServletResponse response, String filePath, String exportFileName) throws Exception {
        // 封装响应流参数
        buildZipResponseParam(response, exportFileName);
        ZipOutputStream outputStream = new ZipOutputStream(response.getOutputStream());
        File[] listFiles = new File(filePath).listFiles();
        BufferedInputStream inputStream = null;
        try {
            // 压缩目录下的所有文件,添加到zipEntry
            addFileToZip(outputStream, inputStream, listFiles);
        } catch (Exception e) {
            log.error("ZipUtil exportZipFileByFilePath in error:{}", e);
        } finally {
            if (Objects.nonNull(inputStream)) {
                inputStream.close();
            }
            // 必须调用finish，否则导出的压缩文件会是损坏的
            outputStream.finish();
            //outputStream.close();
        }
    }

    /**
     * @description: 添加文件到zip压缩包中
     * @param:
     * @param: outputStream
     * @param: inputStream
     * @param: listFiles
     * @return:
     * @author: it-learning
     * @date: 2022/9/15 16:52
     */
    private static void addFileToZip(ZipOutputStream outputStream, BufferedInputStream inputStream, File... listFiles) throws Exception {
        // 压缩目录下的所有文件,添加到zipEntry
        for (File file : listFiles) {
            ZipEntry zipEntry = new ZipEntry(file.getName());
            FileInputStream fileInputStream = new FileInputStream(file.getPath());
            byte[] buffer = new byte[1024];
            outputStream.putNextEntry(zipEntry);
            int result = 0;
            inputStream = new BufferedInputStream(fileInputStream, buffer.length);
            while ((result = inputStream.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, result);
            }
            inputStream.close();
            outputStream.flush();
            outputStream.closeEntry();
            fileInputStream.close();
        }
    }

    /**
     * @description: 封装zip压缩文件返回头
     * @param:
     * @param: response
     * @return:
     * @author: it
     * @date: 2022/9/15 16:38
     */
    private static void buildZipResponseParam(HttpServletResponse response, String fileName) {
        response.setContentType(CommonConstant.CONTENT_TYPE);
        response.setCharacterEncoding(CommonConstant.UTF_8);
        response.setHeader(CommonConstant.CONTENT_DISPOSITION, CommonConstant.ATTACHMENT_FILENAME + fileName);
    }

    /**
     * @description: 解压指定文件到具体目录下
     * @param:
     * @param: saveDecompressPath 解压后的文件保存目录
     * @param: zipFileList 需要解压的zip文件
     * @return:
     * @author: it-diary
     * @date: 2022/9/16 14:47
     */
    public static void decompressZipFile(String saveDecompressPath, ZipFile... zipFileList) throws Exception {
        File savePath = new File(saveDecompressPath);
        if (!savePath.exists()) {
            savePath.mkdirs();
        }
        for (ZipFile zipFile : zipFileList) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            // 遍历压缩包下的所有文件，并保存到给定的压缩地址
            while (entries.hasMoreElements()) {
                InputStream inputStream = null;
                FileOutputStream fileOutputStream = null;
                BufferedOutputStream outputStream = null;
                try {
                    ZipEntry zipEntry = entries.nextElement();
                    inputStream = zipFile.getInputStream(zipEntry);
                    // 将文件解压到指定位置
                    fileOutputStream = new FileOutputStream(new File(saveDecompressPath + StrUtil.SLASH + zipEntry.getName()));
                    outputStream = new BufferedOutputStream(fileOutputStream);
                    byte[] buffer = new byte[1024];
                    int result = 0;
                    while ((result = inputStream.read(buffer, 0, buffer.length)) != -1) {
                        outputStream.write(buffer, 0, result);
                    }
                } catch (Exception e) {
                    throw e;
                } finally {
                    if (Objects.nonNull(inputStream)) {
                        inputStream.close();
                    }
                    if (Objects.nonNull(outputStream)) {
                        outputStream.flush();
                        if (Objects.nonNull(fileOutputStream)) {
                            fileOutputStream.close();
                        }
                        outputStream.close();
                    }
                }

            }
            zipFile.close();
        }
    }

}
