package cn.it.learning.mapper;

import cn.it.learning.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    /**
     * xml当时导入案例
     *
     * @param userList
     * @return
     */
    boolean insertUserList(@Param("userList") List<User> userList);

}
