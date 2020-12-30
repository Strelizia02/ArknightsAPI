package com.strelizia.arknights.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @author wangzy
 * @Date 2020/11/20 17:01
 **/
@Data
@Component
@ConfigurationProperties(prefix = "spring.datasource.localdatasource")
/**
 * 读取配置文件中的数据库信息
 */
public class LocalDataSourceProperties {

  private String url;
  private String username;
  private String password;
  private String driverClassName;
}
