package com.strelizia.arknights.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangzy
 * @Date 2020/11/20 17:01
 **/
@Configuration
@MapperScan(basePackages = "com.strelizia.arknights.dao",
        sqlSessionTemplateRef = "localSqlSessionTemplate")
public class LocalDataSourceConfig {

    @Autowired
    private LocalDataSourceProperties prop;

    /**
     * 创建数据源
     */
    @Bean(name = "localDataSource")
    public DataSource getFirstDataSource() {
        DataSource build = DataSourceBuilder.create()
                .driverClassName(prop.getDriverClassName())
                .url(prop.getUrl())
                .username(prop.getUsername())
                .password(prop.getPassword())
                .build();
        return build;
    }

    @Bean(name = "localSqlSessionFactory")
    public SqlSessionFactory localSqlSessionFactory(
            @Qualifier("localDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:mapping/*.xml"));

        bean.setDataSource(dataSource);
        return bean.getObject();
    }

    @Bean("localTransactionManger")
    public DataSourceTransactionManager localTransactionManger(
            @Qualifier("localDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    // 创建SqlSessionTemplate

    @Bean(name = "localSqlSessionTemplate")
    public SqlSessionTemplate localSqlSessionTemplate(
            @Qualifier("localSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MyMappingJackson2HttpMessageConverter());
        return restTemplate;
    }

}

class MyMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
    public MyMappingJackson2HttpMessageConverter(){
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.TEXT_PLAIN);
        mediaTypes.add(MediaType.TEXT_HTML);
        setSupportedMediaTypes(mediaTypes);
    }
}
