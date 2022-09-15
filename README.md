## 项目示例在线文档

<a href="http://it-learning-diary.gitee.io/it-wheels-king-inline-doc" target="_blank">轮子之王使用示例-在线文档(使用必看)</a>


## 项目初衷

轮子之王：将平常开发中非常常用的功能做成轮子，减少开发时间，让开发者拥有更多的时间能够摸鱼
<b>如果本项目给你提供了帮助，请给予支持(star一下，或者推荐给你的朋友)！</b>

轮子即调即用，符合绝大部分导入导出业务逻辑，节省开发时间！


## 已完成轮子

- <b>zip轮子：</b>支持压缩给定文件或者路径下所有文件
- <b>集成复杂报表导出轮子：</b>freemarker实现，用于复杂excel、word导出)
- <b>zip轮子：</b> 支持压缩给定文件或者路径下所有文件
- <b>文件服务器(seaweedfs)轮子：</b> 支持上传、下载、删除任何类型的文件
- <b>excel导入轮子：</b> 支持导入任何exccel数据，可以自定义转换后excel数据处理的业务逻辑(支持抛出异常、事务回滚、记录解析时的异常数据)。
- <b>excel导出轮子：</b> 支持固定表头(兼容多sheet页)和动态表头(兼容多sheet页)方式的数据导出。
- <b>项目模板下载轮子:</b> 支持导出项目或者服务器指定目录下的任意模板文件
- <b>csv导出轮子：</b> 支持String和bean两种定制表头导出方式
- <b>csv导入轮子：</b> 支持String和bean两种数组映射方式导入方式
- <b>ftp轮子</b>: 支持ftp上传、下载
- 其他功能,持续迭代中....



## 分支说明

- main：主分支，会定期合并最新代码
- master：主分支，包含演示代码(学习建议拉取该分支代码)
- release：发布分支，只包含核心代码，不包含演示代码(<b>项目引入推荐使用该分支</b>)

## 引入方式


- <b>方式一：</b> 将项目打成jar包，在项目中引入(100%推荐)
- <b>方式二：</b> 引入所需依赖，将工具包复制到自己项目的代码中

## 技术栈

- spring-boot(用于提供测试样例)
- easyexcel(大数据量下excel导入导出)
- postgresql(可选，用于写演示案例)
- mybatis/mybatis-plus(可选，用于写演示案例)
- hutool(一些集成工具类)
- lombok(自动生成get/set方法)
- univocity-parsers(用于csv导出导出)
- commons-net(用于ftp上传、下载)
- seaweedfs-client(用于文件服务器上传、下载、删除操作)
- freemarker(实现复杂报表excel、word导出)



## 更新日志

  - 2022-9-15: 集成zip压缩轮子，支持压缩指定文件和指定目录下所有文件
  - 2022-9-10：删除部分用于字段，整理正式分支release(项目需要使用直接打包release分支依赖即可)
  - 2022-9-09: 集成复杂报表导出工具(freemarker实现，用于复杂excel、word导出)
  - 2022-9-07: 集成使用示例在线文档
  - 2022-9-06: ftp工具集成从远端下载文件到本地，引入springboot-test添加本地测试
  - 2022-7-16: 引入文件服务器，支持上传、下载、删除任何类型的文件(兼容中英文名称下载)
  - 2022-7-14：引入ftp上传、下载轮子
  - 2022-6-06：新增excel动态导出案例
  - 2022-5-22：excel导入轮子 + csv导入轮子 添加导入文件类型校验和导入字段校验
  - 2022-5-18：csv导入轮子集成(支持事务、异常日志记录，数组+实体映射数据两种方式) + 使用案例
  - 2022-5-17：csv导出轮子集成 + 使用案例
  - 2022-4-30：excel导入轮子添加事务回滚、异常日志记录支持，新增下载项目模板文件轮子
  - 2022-4-25：项目导入、excel导出轮子

## 🔍关于作者
<br/>


> - <b>InfoQ(极客邦)和阿里云平台签约作者</b>，CSDN、掘金、头条、知乎等平台优质创作者，全网粉丝5w+，致力于输出JAVA、数据库、算法等领域优质文章，帮助更多在学习中有疑惑的朋友。
>
> - 热爱IT知识、热爱探究技术
> 
> - **成就感是一直支撑我往前走的原因，也希望自己的分享能够帮助到更多朋友！**

## 📞联系方式



- **技术交流群**

	相遇即是缘，意在为志同道合的朋友提供一个交流的平台，**广告党勿扰！**<br>
	
	因为微信群的二维码有效期限制，进群者先添加博主微信【yyuuyy1235】或扫码，后续统一拉进群，**添加时备注：加群**
	
   <div align="center">
        <img src="https://it-diary-1308244209.cos.ap-guangzhou.myqcloud.com//image20220501120429.png" width="260px">
    </div>
  
  


- **公众号**

	**最新文章、咨询、资料第一时间会在公众号更新！**<br>
	
	目标：专注于计算机、JAVA、算法、开发经验,面试经验等编程知识讲解，只搞干货!<br>
	
	方式：微信搜索【**IT学习日记**】或者扫码下面的二维码即可关注

<div align="center">
        <img src="https://it-diary-1308244209.cos.ap-guangzhou.myqcloud.com//image20220501120516.png" width="260px">
    	</div>

<br>

    关注后回复:【技术圈子大礼包】一次性领取号中所有学习资源



- **粉丝福利**

	现在云厂商在大力搞活动，两三百块即可购买2核4G6M的服务器，有意向的点击优惠链接购买：

	优惠劵领取：<a href="https://curl.qcloud.com/YIRVbiCZ" title="优惠劵" target="_blank">优惠劵</a>
	
	购买链接：<a href="https://curl.qcloud.com/EMjgIkAC" title="云服务器" target="_blank">云服务器</a>
	


- **捐赠鼓励**

	如果觉的【轮子之王】帮助到您，可以请博主喝杯爪哇(咖啡)，荣幸至极！
<div align="center">
        <img src="https://it-diary-1308244209.cos.ap-guangzhou.myqcloud.com//image20220501120559.png" width="260px">
    	</div>



- **参与贡献**

	如果使用项目时发现有问题的地方或者对项目有任何建议，欢迎提Issue，根据实际情况给与奖励。
	
	如果想给轮子之王集成新的组件，欢迎提PR。
