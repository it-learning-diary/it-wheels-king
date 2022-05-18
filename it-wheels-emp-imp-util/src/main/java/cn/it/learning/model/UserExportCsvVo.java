package cn.it.learning.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.univocity.parsers.annotations.Parsed;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author csv导出表头案例
 * @Description TODO
 * @Date 2022/5/17 17:03
 * @Version 1.0
 */
@Getter
@Setter
public class UserExportCsvVo {
    /**
     * Parsed注解将属性名称映射到CSV文件中
     */
    @Parsed(field = "主键")
    private Integer id;
    @Parsed(field = "姓名")
    private String name;
    @Parsed(field = "密码")
    private String password;
}
