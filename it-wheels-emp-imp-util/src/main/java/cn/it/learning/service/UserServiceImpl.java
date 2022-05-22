package cn.it.learning.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.StrUtil;
import cn.it.learning.constant.ImportConstant;
import cn.it.learning.mapper.UserMapper;
import cn.it.learning.model.*;
import cn.it.learning.util.CsvExportUtil;
import cn.it.learning.util.CsvImportUtil;
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
    public void uploadUserListDemoWithExcel(MultipartFile file, String username) throws Exception {
        // 此处先校验导入的文件类型是否为excel
        String type = FileTypeUtil.getType(file.getInputStream());
        if (StrUtil.isBlank(type) || type.contains(ImportConstant.XLS_TYPE) || type.contains(ImportConstant.XLSX_TYPE)) {
            // 返回校验失败信息
            return;
        }
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
    public void exportUserListDemoWithExcel(HttpServletResponse response) {
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
    public void downloadTemplateDemo(String filePath, String saveFileName, String fileSuffix, HttpServletResponse response) {
        ExcelExportUtil.downloadTemplate(filePath, saveFileName, fileSuffix, response);
    }

    /**
     * 导出案例
     *
     * @param response
     */
    public void exportUserListWithCsv(HttpServletResponse response) {
        List<UserExportCsvVo> exportItem = new ArrayList<>();
        // 查询数据
        List<User> dbData = userService.list();
        // 将数据转换成导出需要的实际数据格式,此处只是演示
//        for (User user : dbData) {
//            UserExportCsvVo vo = new UserExportCsvVo();
//            BeanUtil.copyProperties(user, vo);
//            exportItem.add(vo);
//        }
        // 使用bean字段的方式作为表头，导出csv文件
        //CsvExportUtil.exportCsvWithBean(response,"demo",new UserExportCsvVo(),exportItem);

        // 使用字符串数组方式作为表头导出csv数据
        List<Object> head = Stream.of("id", "name", "password").collect(Collectors.toList());
        List<List<Object>> dataList = new ArrayList<>();
        for (User user : dbData) {
            List<Object> row = new ArrayList<>();
            row.add(user.getId());
            row.add(user.getName());
            row.add(user.getPassword());
            dataList.add(row);
        }
        CsvExportUtil.exportCsvWithString(response, "demo", head, dataList);
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

    /**
     * 导入用户数据案例（csv模式）
     *
     * @param file
     */
    @Transactional(rollbackFor = Exception.class)
    public void uploadUserListWithCsv(MultipartFile file) throws Exception {
        // 此处先校验导入的文件类型是否为csv
        String type = FileTypeUtil.getType(file.getInputStream());
        if (StrUtil.isBlank(type) || type.contains(ImportConstant.CSV_TYPE)) {
            // 返回校验失败信息
            return;
        }
        User user = new User();
        user.setId(100);
        user.setName("外层");
        user.setPassword("外层");
        userService.save(user);
        List<String> errorLogList = new ArrayList<>();
        // 调用统一导入方法
        // 方式一：使用csv数据映射到dto实体的方式进行数据导入
        //CsvImportUtil.importCsvWithBean(file.getInputStream(), errorLogList, UserCsvDto.class, UserServiceImpl::saveUserListWithCsv);

        // 方式二、使用csv数据映射到字符串数组的方式进行数据导入
        CsvImportUtil.importCsvWithString(file.getInputStream(), errorLogList, UserCsvDto.class, UserServiceImpl::saveUserListWithCsvStringArrDemo);

        // 如果存在解析异常，输出解析异常并进行事务回滚
        if (CollUtil.isNotEmpty(errorLogList)) {
            throw new RuntimeException(StrUtil.toString(errorLogList));
        }
    }

    /**
     * 方式一、csv数据映射到字符串数组，进行落库操作
     *
     * @param userDtoArray
     */
    private static void saveUserListWithCsvStringArrDemo(List<String[]> userDtoArray) {
        List<User> userList = new ArrayList<>();
        for (String[] valueArr : userDtoArray) {
            User user = new User();
            user.setId(Integer.valueOf(valueArr[0]));
            user.setName(valueArr[1]);
            user.setPassword(valueArr[2]);
            userList.add(user);
        }
        userService.saveBatch(userList);
    }


    /**
     * 方式二：csv数据映射到dto实体，进行落库操作
     * <p>
     * 实际处理从csv中读取出来数据的业务方法(根据自己业务需求定义)
     *
     * @param userDtoList
     */
    private static void saveUserListWithCsv(List<UserCsvDto> userDtoList) {
        List<User> userList = new ArrayList<>();
        for (UserCsvDto userDto : userDtoList) {
            User user = new User();
            BeanUtil.copyProperties(userDto, user);
            userList.add(user);
        }
        userService.saveBatch(userList);
    }
}
