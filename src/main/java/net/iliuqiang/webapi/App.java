package net.iliuqiang.webapi;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.HttpStatus;
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
@EnableScheduling
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
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {

        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {

                ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401.html");
                ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/me.html");
                ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");

                container.addErrorPages(error401Page, error404Page, error500Page);
            }
        };
    }

}

