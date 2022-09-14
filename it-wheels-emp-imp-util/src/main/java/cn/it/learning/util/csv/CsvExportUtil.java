package cn.it.learning.util.csv;

import cn.it.learning.constant.ExportConstant;
import cn.it.learning.model.UserExportVo;
import com.univocity.parsers.common.processor.BeanWriterProcessor;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Objects;

/**
 * @Author it-learning
 * @Description csv文件导出包
 * @Date 2022/5/16 15:55
 * @Version 1.0
 */
@Slf4j
public class CsvExportUtil<T> {

    /**
     * 导出csv文件(表头和数据都以字符串的形式)
     *
     * @param response
     * @param head
     * @param rowDataList
     */
    public static <T> void exportCsvWithString(HttpServletResponse response, String fileName, List<T> head, List<List<T>> rowDataList) {
        CsvWriter writer = null;
        try {
            // 设置响应头格式
            buildingCsvExportResponse(response,fileName);
            CsvWriterSettings setting = getDefaultWriteSetting();
            writer = new CsvWriter(response.getOutputStream(), setting);
            writer.writeHeaders(head);
            writer.writeStringRows(rowDataList);
            writer.flush();
        } catch (Exception e) {
            log.error("CsvExportUtil exportCsv in error:{}", e);
        } finally {
            if (Objects.nonNull(writer)) {
                writer.close();
            }
        }
    }

    /**
     * 导出csv文件(表头和行都以实体的方式)
     *
     * @param response
     * @param head
     * @param rowDataList
     */
    public static <T> void exportCsvWithBean(HttpServletResponse response, String fileName, T head, List<T> rowDataList) {
        CsvWriter writer = null;
        try {
            // 设置响应头格式
            buildingCsvExportResponse(response,fileName);

            // 设置导出格式
            CsvWriterSettings setting = getDefaultWriteSetting();
            // 创见bean处理器，用于处理写入数据
            BeanWriterProcessor<?> beanWriter = new BeanWriterProcessor<>(head.getClass());
            setting.setRowWriterProcessor(beanWriter);

            // 导出数据
            writer = new CsvWriter(response.getOutputStream(), setting);
            writer.processRecords(rowDataList);
            writer.flush();
        } catch (Exception e) {
            log.error("CsvExportUtil exportCsvWithBean in error:{}", e);
        } finally {
            if (Objects.nonNull(writer)) {
                writer.close();
            }
        }
    }

    /**
     * @description: 封装响应流参数，防止导出的csv文件用office打开出现乱码
     * @param: response
     * @param: fileName
     * @return:
     * @author: it-learning-diary
     * @date: 2022/9/14 10:13
     */
    private static void buildingCsvExportResponse(HttpServletResponse response, String fileName) throws IOException {
        // 设置响应头格式
        response.setContentType(ExportConstant.EXCEL_CONTENT_TYPE);
        response.setCharacterEncoding(ExportConstant.UTF_8);
        response.setHeader(ExportConstant.CONTENT_DISPOSITION, ExportConstant.ATTACHMENT_FILENAME + fileName + ExportConstant.CSV_SUFFIX);
        /***
         * 设置文件的BOM(byte-order-mark，字节顺序标记，是位于码点U+FEFF的Unicode字符的名称)头，，防止csv文件使用office打开时中文出现乱码
         * 原因：【Windows就是使用BOM来标记文本文件的编码方式的。UTF-8不需要BOM来表明字节顺序，但可以用BOM来表明编码方式。当文本程序读取到以EF BB BF开头的字节流时，就知道这是UTF-8编码了】
         * 因为CSV文件是纯文本文件，office打开这种文件默认是以excel的方式，这个时候需要通过元信息来识别打开它的编码，
         * 微软就定义了一个自己的格式叫 BOM 头，上面的代码就是给csv文件添加这个BOM头，没有指定时，工具会使用默认的编码，编码不匹配则会导致乱码。
         *
         */
        response.getOutputStream().write(0xef);
        response.getOutputStream().write(0xbb);
        response.getOutputStream().write(0xbf);
    }

    /**
     * 获取默认的CSV写入配置对象
     *
     * @return
     */
    private static CsvWriterSettings getDefaultWriteSetting() {
        CsvWriterSettings settings = new CsvWriterSettings();
        /**
         * 如果要设置值之间的间隔可以使用下面示例代码
         * FixedWidthFields lengths = new FixedWidthFields(10, 10, 35, 10, 40);
         * FixedWidthWriterSettings settings = new FixedWidthWriterSettings(lengths);
         **/
        // 设置值为null时替换的值
        settings.setNullValue("");
        // 修改分隔符,默认是逗号
        //settings.getFormat().setDelimiter("");
        // 设置是否自动写入标题
        settings.setHeaderWritingEnabled(Boolean.TRUE);
        return settings;
    }

}
