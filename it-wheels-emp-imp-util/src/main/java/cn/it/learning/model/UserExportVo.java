package cn.it.learning.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author it-learning-diary
 * @Description TODO
 * @Date 2022/4/25 16:18
 * @Version 1.0
 */
@Getter
@Setter
@ColumnWidth(20) //设置单元格长度
public class UserExportVo {
    @ExcelProperty("编号")
    private Integer id;
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("密码")
    private String password;
}
