package cn.it.learning.model;

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

    private Integer id;
    private String name;
    private String password;
}
