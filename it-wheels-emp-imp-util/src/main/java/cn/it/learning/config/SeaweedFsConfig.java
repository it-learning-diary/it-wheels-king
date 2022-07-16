package cn.it.learning.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author it
 * @Description seaweedfs相关配置信息
 * @Date 2022/6/28 16:28
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "seaweedfs")
public class SeaweedFsConfig {
    
    /***
    * 主机
    */
    private String host;
    
    /***
    * 端口
    */
    private Integer port;
    
}