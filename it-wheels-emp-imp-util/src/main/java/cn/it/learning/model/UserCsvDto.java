package cn.it.learning.model;

import cn.it.learning.annotation.ImportFieldValid;
import com.baomidou.mybatisplus.annotation.TableName;
import com.univocity.parsers.annotations.Parsed;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author it-learning-diary
 * @Description CSV导入类
 * @Date 2022/5/18 10:41
 * @Version 1.0
 */
@Getter
@Setter
@TableName(value = "t_user_test")
// @Headers() 该注解可以控制在解析/写入时使用哪些标题
public class UserCsvDto {
    /**
     * Parsed注解将属性名称映射到CSV文件字段名称绑定，也可以使用:index字段指定映射的位置
     * ImportFieldValid 自定义注解，用于判断导入文件中必填字段是否满足条件
     */
    @Parsed(field = "id")
    @ImportFieldValid(message = "用户ID不能为空")
    private Integer id;
    @Parsed(field = "name")
    @ImportFieldValid(message = "用户名称不能为空")
    private String name;
    @Parsed(field = "pass")
    @ImportFieldValid(message = "用户密码不能为空")
    private String password;
    /**
     * 日期格式字段示例如下：
     * @Format(formats = {"dd-MMM-yyyy", "yyyy-MM-dd"}, options = "locale=en")
     * @Parsed
     * private Date date;
     */
}
