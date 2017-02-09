package net.iliuqiang.webapi;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Hello world!
 *
 */

@Configuration
@EnableAutoConfiguration
@ComponentScan
//@EnableScheduling
@MapperScan("net.iiuqiang.mapper")
public class App 
{
//	@Bean
//	@ConfigurationProperties(prefix="spring.datasource")
//	public DataSource dataSource(){
//		return new org.apache.tomcat.jdbc.pool.DataSource();
//	}
//	@Bean
//	public SqlSessionFactory sqlSessionFactoryBean() throws Exception{
//		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//		sqlSessionFactoryBean.setDataSource(dataSource());
//		
//		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//		sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/*.xml"));
//		return sqlSessionFactoryBean.getObject();
//	}
//	@Bean
//	public PlatformTransactionManager transactionManager(){
//		return new DataSourceTransactionManager(dataSource());
//	}
    public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
    }

}

