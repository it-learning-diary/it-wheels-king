## 使用示例

&emsp;&emsp;说明：需要先将项目打包成jar，然后在pom文件中引入。

&emsp;&emsp;想要了解更多关于Excel导出导入的内容，可以看我之前编写的文章：<a href="https://blog.csdn.net/qq_40891009/article/details/105616526" target="_blank">操作Excel,除了使用POI你还会其他的?</a>

### 1、引入依赖

```java

<dependency>
 	<groupId>cn.it.learning</groupId>
    <artifactId>it-wheels-king</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 2、Excel导入示例

&emsp;&emsp;**使用示例：**

```java
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
```

&emsp;&emsp;**注意细节：**

&emsp;&emsp;上面示例中统一导入方法中的【UserServiceImpl::saveUserList】编写的就是将导入文件数据封装到Dto后，我们业务需要做的处理，比如转成po，然后将数据落盘等，示例如下：

```java
private static void saveUserList(List<UserDto> userDtoList) {
        List<User> userList = new ArrayList<>();
        // 将数据转换成PO
        for (UserDto userDto : userDtoList) {
            User user = new User();
            BeanUtil.copyProperties(userDto, user);
            userList.add(user);
        }
        // 使用mybatis xml方式插入
        boolean b = userMapper.insertUserList(userList);

        // 使用mybatis-plus自带的api方式插入
        userService.saveBatch(userList);

        // 测试落盘异常时是否会进行事务回滚
        Long l = userDtoList.get(2).getRelationId();
        if (l == 1002l) {
            int i = 1 / 0;
            System.out.println(i);
        }
    }

```

&emsp;&emsp;如果需要添加自定义的一些数据读取逻辑，可以在ExcelImportCommonListener做定制化，该类主要是做文件数据读取和封装到Dto中。

### 3、 Excel导出示例

&emsp;&emsp;(1)、固定表头的数据导出(导出的字段已经确定，在对应Dto实体中定义好的场景)，导出示例如下：

```java
public void exportUserListDemoWithExcel(HttpServletResponse response) {
        // 表头
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
    	// 调用固定表头导出API即可(具体的参数含义在代码中有详细解释)
        ExcelExportUtil.exportFile("user", head, exportDataList, sheetNameList, response);
    }

```

&emsp;&emsp;(2)、动态表头数据导出：动态表头是通过字符串集合实现，有些时候要根据前端选定的列进行数据导出，此时则可以通过这种方式，这种方式也支持多sheet页导出，示例如下：

```java
public void exportUserListDynamicDemoWithExcel(HttpServletResponse response) {
        try {
            // 表头
            List<List<List<String>>> head = new ArrayList<>();
            List<List<String>> headRowList = new ArrayList<>();

            // 数据(使用两层list为了兼容多个sheet页，如果是不同的sheet页则放在不同的List集合中)
            List<List<List<String>>> exportDataList = new ArrayList<>();
            List<List<String>> rowList = new ArrayList<>();

            // 查询数据
            List<User> dbData = userService.list();

            // 使用反射接收导出的数据(反射可以保证如果获取到的数据字段新增或者减少，同时可以不变动代码情况下动态导出)
            for (User user : dbData) {
                List<String> exportItem = new ArrayList<>();
                // 使用反射获取实体中的所有属性，或者直接指定需要导出的属性
                Class<? extends User> userClass = user.getClass();
                Field[] fields = userClass.getDeclaredFields();
                for (int index = 0; index < fields.length; index++) {
                    // 组装数据
                    Field field = fields[index];
                    field.setAccessible(Boolean.TRUE);
                    Object value = field.get(user);
                    List<String> rowHead = new ArrayList<>();
                    exportItem.add(Objects.nonNull(value) ? String.valueOf(value) : "");
                    // 组装表头(一个sheet页表头只要组装一次就好，如果是多个sheet页，需要进行修改)
                    if(CollUtil.isEmpty(rowList)){
                        rowHead.add(field.getName());
                        headRowList.add(rowHead);
                    }
                }
                rowList.add(exportItem);
            }
            head.add(headRowList);
            exportDataList.add(rowList);
            // sheet页名称-自定义，如果没有则传空
            List<String> sheetNameList = Stream.of("sheet1").collect(Collectors.toList());
            ExcelExportUtil.exportWithDynamicData("user", head, exportDataList, sheetNameList, response);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

```

&emsp;&emsp;(3)、下载指定服务器模板文件：一般情况下，导入之前需要先下载模板，然后填充数据导入，此时可以直接调用该方法完成模板下载，示例如下：

```java
public void downloadTemplateDemo(String filePath, String saveFileName, String fileSuffix, HttpServletResponse response) {
        ExcelExportUtil.downloadTemplate(filePath, saveFileName, fileSuffix, response);
}
```
