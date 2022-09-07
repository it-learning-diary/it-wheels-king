## 使用示例

&emsp;&emsp;说明：需要先将项目打包成jar，然后在pom文件中引入。

&emsp;&emsp;简介：FTP即文件传输协议的简称，它是TCP/IP协议簇中的一员，也是Internet上最早使用的协议之一，通过它可以实现电脑与电脑间对文件的各种操作（如文件的增、删、改、查、传送等），FTP的目标是提高文件的共享性，提供非直接使用远程计算机，实现计算机文件的相互操作，使存储介质对用户透明和可靠高效地传送数据。

&emsp;&emsp;想要了解更多关于FTP的内容，可以看我之前编写的文章：<a href="https://blog.csdn.net/qq_40891009/article/details/120497976" target="_blank">【实战-干货】手把手带你搭建自己的FTP服务器，实现文件上传、下载</a>


### 1、引入依赖

```java

<dependency>
 	<groupId>cn.it.learning</groupId>
    <artifactId>it-wheels-king</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 2、FTP文件上传

```java
    public void upload() {
        try {
        	// 封装上传参数
            FtpUploadParam param = new FtpUploadParam();
            param.setHostname(ftpConfig.getServerHostname());
            param.setPort(ftpConfig.getServerPort());
            param.setUsername(ftpConfig.getServerUsername());
            param.setPassword(ftpConfig.getServerPassword());
            param.setWorkingPath(ftpConfig.getServerWorkingPath());
            param.setSaveName("测试程序上传文件到ftp服务器.mp3");
            // 上传D盘下的like到对应的FTP服务器上
            InputStream in = new FileInputStream(new File("D:/uploadfile/like.mp3"));
            param.setInputStream(in);
            ftpUtils.upload(param);
        } catch (Exception e) {
            log.error("TestFtpServerController upload 错误:{}", e);
        }
    }

```


### 3、FTP文件下载

&emsp;&emsp;下载FTP服务器上指定的文件，示例如下：

```java
public void download() {
        try {
        	// 封装下载文件参数
            FtpDownloadParam param = new FtpDownloadParam();
            param.setHostname(ftpConfig.getServerHostname());
            param.setPort(ftpConfig.getServerPort());
            param.setUsername(ftpConfig.getServerUsername());
            param.setPassword(ftpConfig.getServerPassword());
            param.setWorkingPath(ftpConfig.getServerWorkingPath());
            param.setDownloadPath("D:/downloadFile/");
            param.setFileName("从ftp服务器下载的音乐.mp3");
            ftpUtils.download(param, "测试程序上传文件到ftp服务器.mp3");
        } catch (Exception e) {
            log.error("TestFtpServerController download 错误:{}", e);
        }

    }

```


&emsp;&emsp;下载FTP服务器上指定目录下的所有文件，示例如下：

```java
public void testFtpDownloadFile() throws Exception {
        FTPClient ftpClient = new FTPClient();
        ftpUtil.connect(ftpClient, "127.0.0.1", 21, "admin", "123");
        ftpUtil.downloadFileToDestination(ftpClient, "/data/ftp", "D:/ftptest/");
    }
```