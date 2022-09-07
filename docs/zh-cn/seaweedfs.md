## 使用示例

&emsp;&emsp;说明：需要先将项目打包成jar，然后在pom文件中引入。

&emsp;&emsp;简介：一款Apache基金会开源项目，基于go语言开发的高度可拓展开源的分布式存储系统、它支持基于Restful API风格进行增、删、查操作，非常适用于处理小文件。

&emsp;&emsp;想要了解更多关于Seaweedfs的内容，可以看我之前编写的文章：<a href="https://it-learning-diary.blog.csdn.net/article/details/126114259" target="_blank">Gitee 图床被屏蔽后，我搭建了一个文件系统并封装成轮子开源-Seadweedfs</a>


### 1、引入依赖

```java

<dependency>
 	<groupId>cn.it.learning</groupId>
    <artifactId>it-wheels-king</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 2、上传文件

&emsp;&emsp;接口直接指定上传文件即可完成上传

```java
public void uploadFile(MultipartFile file) {
        try {
            String fileUrl = seaweedFsUtil.uploadFile(file);
            System.out.println(fileUrl);
        } catch (Exception e) {
            log.error("TestSeaweedFsController uploadFile in error:{}", e);
        }
    }
```

&emsp;&emsp;指定需要下载的文件标识符fid(在上传后会返回)和指定下载后的文件名称即可

### 3、下载文件

```java
public void downloadFile(HttpServletResponse response, HttpServletRequest request, String fileId, String fileName) {
        try {
            seaweedFsUtil.downloadFileByFid(response, request, fileId, fileName);
        } catch (Exception e) {
            log.error("TestSeaweedFsController downloadFile in error:{}", e);
        }
  }
```

### 4、删除文件

&emsp;&emsp;指定要删除的文件fid即可

```java
public void deleteFile(String fileId) {
        try {
            seaweedFsUtil.deleteFileByFid(fileId);
        } catch (Exception e) {
            log.error("TestSeaweedFsController downloadFile in error:{}", e);
        }
}
```