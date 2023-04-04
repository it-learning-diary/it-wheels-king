package cn.it.learning.util.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import cn.it.learning.constant.ImportConstant;
import cn.it.learning.refactor.ThrowingBiConsumer;
import cn.it.learning.valid.ImportValid;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ConverterUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Description:
 * @Author: it-learning-diary
 * @Version: v1
 * @Date: 2023/2/10:14:02
 */
@Slf4j
public class ExcelImportBiCommonListener<T, U> implements ReadListener<T> {

    /**
     * 转换后插入数据表的实体
     */
    private List<T> persistentDataList = Lists.newArrayList();

    private ThrowingBiConsumer<List<T>, U> persistentActionMethod;

    /**
     * 额外参数(可以是单个对象，也可以是集合，看个人需要定义)
     */
    private U paramDto;

    /**
     * 异常日志记录
     */
    private List<String> errorLogList = new ArrayList<>();

    /**
     * 构造函数(不包含异常信息)
     *
     * @param persistentActionMethod 从excel读取到的数据到落库的业务逻辑
     */
    public ExcelImportBiCommonListener(ThrowingBiConsumer<List<T>, U> persistentActionMethod) {
        this.persistentActionMethod = persistentActionMethod;
    }

    /**
     * 构造函数(包含异常信息)
     *
     * @param persistentActionMethod
     * @param persistentActionMethod 从excel读取到的数据到落库的业务逻辑
     */
    public ExcelImportBiCommonListener(ThrowingBiConsumer<List<T>, U> persistentActionMethod, U paramDto, List<String> errorLogLIst) {
        this.paramDto = paramDto;
        this.errorLogList = errorLogLIst;
        this.persistentActionMethod = persistentActionMethod;
    }

    /**
     * 构造函数，包含额外参数
     * @param persistentActionMethod
     * @param paramDto
     */
    public ExcelImportBiCommonListener(ThrowingBiConsumer<List<T>, U> persistentActionMethod, U paramDto) {
        this.paramDto = paramDto;
        this.persistentActionMethod = persistentActionMethod;
    }

    /**
     * 在转换异常 获取其他异常情况下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     * 根据自己需要看是否抛出异常，如果没有特殊要求，则可以不修改。
     *
     * @param exception
     * @param context
     * @throws Exception
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
            log.error("第{}行，第{}列解析异常，数据为:{}", excelDataConvertException.getRowIndex(), excelDataConvertException.getColumnIndex(), excelDataConvertException.getCellData().getStringValue());
            if (Objects.nonNull(errorLogList)) {
                // 记录异常日志
                String errorLog = "第" + excelDataConvertException.getRowIndex() + "行，第" + excelDataConvertException.getColumnIndex() + "列解析异常，数据为:" + excelDataConvertException.getCellData().getStringValue() + "";
                errorLogList.add(errorLog);
            }
        }
    }

    /**
     * 返回每个sheet页的表头，根据自己实际业务进行表头字段等校验逻辑，如果没有则保持不动
     *
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        Map<Integer, String> headMapping = ConverterUtils.convertToStringMap(headMap, context);
        log.info("表头数据: " + StrUtil.toString(headMapping));
        if (CollUtil.isEmpty(headMapping)) {
            errorLogList.add("The header of file can't be empty!");
        }
    }

    /**
     * 解析excel表中每行数据
     *
     * @param t
     * @param analysisContext
     */
    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        // 校验导入字段
        ImportValid.validRequireField(t, errorLogList);
        if (Objects.isNull(errorLogList) || CollUtil.isEmpty(errorLogList)) {
            persistentDataList.add(t);
            // 当数据达到最大插入数量后则进行落库操作，防止大数量情况下OOM
            if (persistentDataList.size() >= ImportConstant.MAX_INSERT_COUNT) {
                // 进行业务数据插入
                this.persistentDataToDb(persistentDataList, paramDto);
                // 清空集合
                persistentDataList.clear();
            }
        }
    }


    /**
     * 所有数据解析完后，回调
     *
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 在此处调用落库操作，防止有剩余数据未落库
        if (CollUtil.isNotEmpty(persistentDataList)) {
            persistentDataToDb(persistentDataList, paramDto);
        }
    }

    /**
     * 将数据持久化到数据库中
     *
     * @param data
     */
    private void persistentDataToDb(List<T> data, U u) {
        // 对数据分组，批量插入
        List<List<T>> dataList = ListUtil.split(data, ImportConstant.MAX_INSERT_COUNT);
        for (List<T> listItem : dataList) {
            persistentActionMethod.accept(listItem, u);
        }
    }
}