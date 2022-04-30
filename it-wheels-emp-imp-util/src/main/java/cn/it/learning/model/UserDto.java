package cn.it.learning.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author it-learning-diary
 * @Description 测试接收excel中每行数据的实体
 * @Date 2022/4/25 10:24
 * @Version 1.0
 */
@Getter
@Setter
public class UserDto {

//    @ExcelProperty(index = 0)
    private Integer id;
//    @ExcelProperty(index = 1)
    private String name;
//    @ExcelProperty(index = 2)
    private String password;
    @ExcelIgnore
    private Long relationId;
}
