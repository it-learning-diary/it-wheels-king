package cn.it.learning.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author it-learning-diary
 * @Description 测试实体
 * @Date 2022/4/25 9:59
 * @Version 1.0
 */
@Getter
@Setter
@TableName(value = "t_user_test")
public class User {

    private Integer id;
    private String name;
    private String password;

}
