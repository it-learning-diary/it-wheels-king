package cn.it.learning.service;

import cn.hutool.core.bean.BeanUtil;
import cn.it.learning.mapper.UserMapper;
import cn.it.learning.model.User;
import cn.it.learning.model.UserDto;
import cn.it.learning.model.UserExportVo;
import cn.it.learning.util.ExcelExportUtil;
import cn.it.learning.util.ExcelImportUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author it-leaning-diary
 * @Description 测试案例：用户用例业务处理
 * @Date 2022/4/25 10:00
 * @Version 1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> {

    /**
     * 静态属性注入方式1
     */
//    private static UserMapper userMapper;
//
//    @Autowired
//    public void setUserMapper(UserMapper userMapper) {
//        UserServiceImpl.userMapper = userMapper;
//    }

    /**
     * 静态属性注入方式2
     */

    private static UserServiceImpl userService;

    @Autowired
    public void setUserMapper(UserServiceImpl userService) {
        UserServiceImpl.userService = userService;
    }

//    @PostConstruct
//    public void init() {
//        userService = this;
//    }


    /**
     * 导入用户数据案例
     *
     * @param file
     */
    @Transactional(rollbackFor = Exception.class)
    public void uploadUserListDemo(MultipartFile file, String username) throws Exception {
        User user = new User();
        user.setId(100);
        user.setName("外层");
        user.setPassword("外层");
        userService.save(user);
        // 调用统一导入方法
        ExcelImportUtil.importFile(file.getInputStream(), new UserDto(), UserServiceImpl::saveUserList);
    }

    /**
     * 导出案例
     *
     * @param response
     */
    public void exportUserListDemo(HttpServletResponse response) {
        // 表头(使用excel中的注解定义，如果表头不固定，请使用ExcelExportUtil.exportWithDynamicData进行导出)
        List<UserExportVo> head = Stream.of(new UserExportVo()).collect(Collectors.toList());
        // 数据(使用两层list为了兼容多个sheet页，如果是不同的sheet页则放在不同的List集合中)
        List<List<UserExportVo>> exportDataList = new ArrayList<>();
        List<UserExportVo> exportItem = new ArrayList<>();
        // 查询数据
        List<User> dbData = userService.list();
        // 将数据转换成导出需要的实际数据格式,此处只是演示
        for (User user : dbData) {
            UserExportVo vo = new UserExportVo();
            BeanUtil.copyProperties(user, vo);
            exportItem.add(vo);
        }
        exportDataList.add(exportItem);
        // sheet页名称-自定义，如果没有则传空
        List<String> sheetNameList = Stream.of("sheet1").collect(Collectors.toList());
        ExcelExportUtil.exportFile("user", head, exportDataList, sheetNameList, response);
    }

    /**
     * 下载指定目录下的模板案例
     *
     * @param filePath
     * @param saveFileName
     * @param response
     */
    public void downloadTemplateDemo(String filePath, String saveFileName, HttpServletResponse response) {
        ExcelExportUtil.downloadTemplate(filePath, saveFileName, response);
    }

    /**
     * 实际处理从excel中读取出来数据的业务方法(根据自己业务需求定义)
     *
     * @param userDtoList
     */
    private static void saveUserList(List<UserDto> userDtoList) {
        List<User> userList = new ArrayList<>();
        for (UserDto userDto : userDtoList) {
            User user = new User();
            BeanUtil.copyProperties(userDto, user);
            userList.add(user);
        }
        // 使用mybatis xml方式插入
        // boolean b = userMapper.insertUserList(userList);

        // 使用mybatis-plus自带的api方式插入
        userService.saveBatch(userList);

        // 测试落盘异常时是否会进行事务回滚
//        Long l = userDtoList.get(2).getRelationId();
//        if (l == 1002l) {
//            int i = 1 / 0;
//            System.out.println(i);
//        }

    }
}
