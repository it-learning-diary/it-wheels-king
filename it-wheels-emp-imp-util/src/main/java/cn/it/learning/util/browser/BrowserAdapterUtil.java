package cn.it.learning.util.browser;

import cn.hutool.core.util.StrUtil;
import cn.it.learning.constant.CommonConstant;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;

/**
 * @Author it-learning
 * @Description 浏览器适配工具类
 * @Date 2022/9/9 14:59
 * @Version 1.0
 */
public class BrowserAdapterUtil {

    /**
     * @description: 导出中文名称文件时适配不同的浏览器
     * @return:
     * @author: it
     * @date: 2022/7/16 14:48
     */
    public static String buildingFileNameAdapterBrowser(HttpServletRequest request, String fileName) throws Exception {
        String userAgent = request.getHeader("User-Agent");
        // 火狐浏览器中文名称时使用Base64进行编码，格式如下：
        // Content-Disposition: attachment;filename==?charset?B?xxx?=中文名称编码后的字符串
        // 其中、=?：表示编码内容开始、charset:表示字符集、B:表示Base64编码、?=:表示编码内容的结束
        if (StrUtil.isNotBlank(userAgent) && userAgent.contains("Firefox")) {
            String encodeFileName = new BASE64Encoder().encode(fileName.getBytes(CommonConstant.UTF_8));
            String fullName = "=?" + CommonConstant.UTF_8 + "?B?" + encodeFileName + "?=";
            return fullName;
        } else {
            // 谷歌或者IE浏览器中文名称时使用urlencoder
            // url编码实际上就是将中文汉字转换成%xx%xx的格式
            String encodeFileName = URLEncoder.encode(fileName, CommonConstant.UTF_8);
            return encodeFileName;
        }
    }
}
