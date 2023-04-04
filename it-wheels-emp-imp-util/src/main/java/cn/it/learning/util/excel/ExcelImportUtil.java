package cn.it.learning.util.excel;

import cn.it.learning.refactor.ThrowingBiConsumer;
import cn.it.learning.refactor.ThrowingConsumer;
import com.alibaba.excel.EasyExcel;

import java.io.InputStream;
import java.util.List;

/**
 * @Author it-learning-diary
 * @Description 导入excel模板
 * @Date 2022/4/24 14:09
 * @Version 1.0
 */
public class ExcelImportUtil<T, U> {

    /**
     * 通用导入excel文件方法
     *
     * @param fileStream 导入的文件流
     * @param rowDto     接收excel每行数据的实体
     * @param rowAction  将接收到的实体进行自定义的业务处理逻辑方法
     * @param <T>        导入数据实体类型
     */
    public static <T> void importFile(InputStream fileStream, T rowDto, ThrowingConsumer<List<T>> rowAction) {
        // 获取excel通用监听器
        ExcelImportCommonListener<T> commonListener = new ExcelImportCommonListener<>(rowAction);
        // 读取excel文件并导入
        EasyExcel.read(fileStream, rowDto.getClass(), commonListener).sheet().doRead();
    }

    /**
     * 通用导入excel文件方法(多参数)
     * 适用于：落盘的数据中还需要传递而外参数时使用，比如导入的数据中需要统一设置导入者Id和其他信息，此时id和其他信息可以放在【U】对象中传递进来
     * T：报表中数据映射到的实际实体
     * U：业务方法中需要使用到的额外参数
     *
     * @param fileStream 导入的文件流
     * @param rowHeadDto 接收excel每行数据的实体
     * @param rowAction  将接收到的实体进行自定义的业务处理逻辑方法
     * @param <T>        实体类型
     */
    public static <T, U> void importBiFile(InputStream fileStream, T rowHeadDto, ThrowingBiConsumer<List<T>, U> rowAction,U paramDto) {
        // 获取excel通用监听器
        ExcelImportBiCommonListener<T, U> commonListener = new ExcelImportBiCommonListener<>(rowAction,paramDto);
        // 读取excel文件并导入
        EasyExcel.read(fileStream, rowHeadDto.getClass(), commonListener).sheet().doRead();
    }

}
