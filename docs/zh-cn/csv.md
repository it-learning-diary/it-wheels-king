## 使用示例

&emsp;&emsp;说明：需要先将项目打包成jar，然后在pom文件中引入。

&emsp;&emsp;简介：CSV(Comma Separated Values)逗号分隔值，与excel等文件相比，excel文件中会包含许多格式信息，占用的空间会更大，CSV是以纯文本的方式存储，故体积会更小，适合存放结构化的信息，如数据导出、流量统计等。

&emsp;&emsp;想要了解更多关于CSV的内容，可以看我之前编写的文章：<a href="https://blog.csdn.net/qq_40891009/article/details/124874311" target="_blank">开源项目-CSV导入导出工具类</a>

### 1、引入依赖

```java

<dependency>
 	<groupId>cn.it.learning</groupId>
    <artifactId>it-wheels-king</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```


### 2、CSV导入

&emsp;&emsp;方式一：封装文件数据到实体bean中，完成数据导入

```java
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

        // 使用csv数据映射到dto实体的方式进行数据导入
        CsvImportUtil.importCsvWithBean(file.getInputStream(), errorLogList, UserCsvDto.class, UserServiceImpl::saveUserListWithCsv);


        // 如果存在解析异常，输出解析异常并进行事务回滚
        if (CollUtil.isNotEmpty(errorLogList)) {
            throw new RuntimeException(StrUtil.toString(errorLogList));
        }
    }
    
    // 数据落盘业务方法
    private static void saveUserListWithCsv(List<UserCsvDto> userDtoList) {
        List<User> userList = new ArrayList<>();
        for (UserCsvDto userDto : userDtoList) {
            User user = new User();
            BeanUtil.copyProperties(userDto, user);
            userList.add(user);
        }
        userService.saveBatch(userList);
    }

```

&emsp;&emsp;方式二：封装文件数据到List集合中，完成数据导入

```java
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
        
        // 使用csv数据映射到字符串数组的方式进行数据导入
        CsvImportUtil.importCsvWithString(file.getInputStream(), errorLogList, UserServiceImpl::saveUserListWithCsvStringArrDemo);

        // 如果存在解析异常，输出解析异常并进行事务回滚
        if (CollUtil.isNotEmpty(errorLogList)) {
            throw new RuntimeException(StrUtil.toString(errorLogList));
        }
    }
    
    // 数据落库方法
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

```


## 3、CSV导出

&emsp;&emsp;方式一：固定表头导出(需要导出的字段定义好在具体的Dto中)

```java
public void exportUserListWithCsv(HttpServletResponse response) {
        List<UserExportCsvVo> exportItem = new ArrayList<>();
        // 查询数据
        List<User> dbData = userService.list();
        // 将数据转换成导出需要的实际数据格式,此处只是演示
        for (User user : dbData) {
            UserExportCsvVo vo = new UserExportCsvVo();
            BeanUtil.copyProperties(user, vo);
            exportItem.add(vo);        
         }
        // 使用bean字段的方式作为表头，导出csv文件
        CsvExportUtil.exportCsvWithBean(response,"demo",new UserExportCsvVo(),exportItem);
       
}

```

&emsp;&emsp;方式二：动态表头导出，有些时候要根据前端选定的列进行数据导出，此时则可以通过这种方式，这种方式也支持多sheet页导出，示例如下：

```java
public void exportUserListWithCsv(HttpServletResponse response) {
        List<UserExportCsvVo> exportItem = new ArrayList<>();
        // 查询数据
        List<User> dbData = userService.list();

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
```