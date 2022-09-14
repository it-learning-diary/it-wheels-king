package cn.it.learning.util.complex_report;

import cn.hutool.core.util.CharsetUtil;
import cn.it.learning.constant.ExportConstant;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;


/**
 * @Author it-learning
 * @Description 复杂excel报表工具类
 * @Date 2022/9/9 11:37
 * @Version 1.0
 */
@Slf4j
public class ComplexExcelReportUtil {

    /***
     * 存储FreeMarker应用核心配置，通常是应用级别的，一个应用只有一个
     */
    private final static Configuration configuration;

    static {
        configuration = new Configuration(Configuration.VERSION_2_3_20);
        // 设置模板文件存储时的字符集
        configuration.setDefaultEncoding(CharsetUtil.UTF_8);
        // 设置模板出现问题时的提示策略
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
    }

    /**
     * @description: 生成复杂报表
     * @param:
     * @param: templatePath 模板的存放路径，相对应classpath路径
     * @param: templateFileName 模板名称如file.xlsx
     * @param: data 填充到模板的数据
     * @param: writer 输入流
     * @param: isNeedCloseStream 是否需要关闭流
     * @return:
     * @author: it-learning
     * @date: 2022/9/9 14:31
     */
    public static void exportComplexByTemplate(String templatePath, String templateFileName, Map<String, Object> data, Writer writer, Boolean isNeedCloseStream) throws Exception {
        // 设置模板加载路径
        configuration.setTemplateLoader(new ClassTemplateLoader(ComplexExcelReportUtil.class, templatePath));
        // 创建模板
        Template template = configuration.getTemplate(templateFileName);
        // 填充数据
        template.process(data, writer);
        // 根据实际情况判断是否需要关闭流，目的是为了适应像response这样会自动关闭的流，无需进行手动处理，否则可能会出现意想不到的问题
        if (isNeedCloseStream) {
            writer.close();
        }
    }


    /**
     * @description: 使用浏览器导出文件(此时会直接让浏览器以文件方式下载)
     * @param:
     * @param: response 响应流
     * @param: templatePath 模板的存放路径，相对应classpath路径
     * @param: templateFileName 模板名称如file.xlsx
     * @param: data 填充到模板的数据
     * @param: isNeedCloseStream 是否需要关闭流(目的是为了适应像response这样会自动关闭的流，无需进行手动处理，否则可能会出现意想不到的问题)
     * @param: exportFileName 导出时的文件名称
     * @return:
     * @author: it-learning
     * @date: 2022/9/9 14:41
     */
    public static void exportReportByBrowser(HttpServletResponse response, String templatePath, String templateFileName, Map<String, Object> data, Boolean isNeedCloseStream, String exportFileName) {
        try {
            // 设置返回内容为二进制形式
            response.setContentType(ExportConstant.EXCEL_CONTENT_TYPE);
            // 设置发送到客户端的响应的字符编码（MIME 字符集）
            response.setCharacterEncoding(ExportConstant.UTF_8);
            // 设置以附件形式导出文件
            response.setHeader(ExportConstant.CONTENT_DISPOSITION, ExportConstant.ATTACHMENT_FILENAME + exportFileName);
            // 融合模板和数据
            exportComplexByTemplate(templatePath, templateFileName, data, response.getWriter(), Boolean.FALSE);
        } catch (Exception e) {
            log.error("ComplexExcelReportUtil exportReportByBrowser in error:", e);
        }
    }

}
