package cn.it.learning.model;

import cn.it.learning.annotation.ImportFieldValid;
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
    /**
     * ImportFieldValid 自定义注解，用于判断导入文件中必填字段是否满足条件
     */

    @ExcelProperty(index = 0)
    @ImportFieldValid(message = "用户ID不能为空")
    private Integer id;
    @ExcelProperty(index = 1)
    @ImportFieldValid(message = "用户名称不能为空")
    private String name;
    @ExcelProperty(index = 2)
    @ImportFieldValid(message = "用户密码不能为空")
    private String password;
    @ExcelIgnore
    private Long relationId;
}
