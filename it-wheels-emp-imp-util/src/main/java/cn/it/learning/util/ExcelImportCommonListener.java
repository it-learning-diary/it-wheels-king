package cn.it.learning.util;

import cn.hutool.core.util.StrUtil;
import cn.it.learning.constant.ImportConstant;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ConverterUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @Author it-learning-diary
 * @Description 数据导入通用实体
 * @Date 2022/4/24 14:27
 * @Version 1.0
 */
@Slf4j
public class ExcelImportCommonListener<E, T> implements ReadListener<T> {

    /**
     * 业务类实现接口
     */
    private final IService service;

    /**
     * 接收每行excel数据的实体
     */
    private final E excelRowItemDto;

    private List<T> persistentData = Lists.newArrayList();

    private final Consumer<T> persistentActionMethod;

    /**
     * 构造函数
     */
    public ExcelImportCommonListener(IService service, E excelRowItemDto, Consumer<T> persistentActionMethod) {
        this.service = service;
        this.excelRowItemDto = excelRowItemDto;
        this.persistentActionMethod = persistentActionMethod;
    }

    /**
     * 在转换异常 获取其他异常情况下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     *
     * @param exception
     * @param context
     * @throws Exception
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
            log.error("第{}行，第{}列解析异常，数据为:{}", excelDataConvertException.getRowIndex(),
                    excelDataConvertException.getColumnIndex(), excelDataConvertException.getCellData());
        }
    }

    /**
     * 返回每个sheet页的表头
     *
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        Map<Integer, String> headMapping = ConverterUtils.convertToStringMap(headMap, context);
        log.info("表头数据: " + StrUtil.toString(headMapping));
    }

    /**
     * 解析excel表中每行数据
     *
     * @param t
     * @param analysisContext
     */
    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        if (persistentData.size() > ImportConstant.MAX_INSERT_COUNT) {
            // 进行业务数据插入
            this.persistentDataToDb(persistentData);
            // 清空集合
            persistentData.clear();
        }

    }


    /**
     * 所有数据解析完后，回调
     *
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    /**
     * 将数据持久化到数据库中
     *
     * @param data
     */
    private void persistentDataToDb(List<T> data) {
        data.stream().forEach(persistentActionMethod);
    }
}
