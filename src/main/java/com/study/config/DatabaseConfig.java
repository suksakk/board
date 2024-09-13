package com.study.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

// 스프링이 properties에서 MyBatis 설정을 읽을 수 있도록 빈(Bean)을 선언
@Configuration
@PropertySource("classpath:/application.properties")
public class DatabaseConfig {

	@Autowired
	private ApplicationContext context;

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public HikariConfig hikariConfig() {
		return new HikariConfig();
	}

	@Bean
	public DataSource dataSource() {
		return new HikariDataSource(hikariConfig());
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		// factoryBean.setMapperLocation() - 기존에 주석 처리되어 있던 코드 해당 메서드에 XML Mapper의 경로를 선언해 주어야
		// 스프링이 XML Mapper를 인식할 수 있다, 기존에는 XML Mapper가 없었으나. PostMapper.xml이 추가되어 주석을 해제한다.
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource());
		factoryBean.setMapperLocations(context.getResources("classpath:/mappers/**/*Mapper.xml"));
		// factoryBean.setConfiguration() - myBatisConfig() 빈을 이용해서 MyBatis 옵션을 설정
		factoryBean.setConfiguration(mybatisConfig());
		return factoryBean.getObject();
	}

	@Bean
	public SqlSessionTemplate sqlSession() throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory());
	}

	@Bean
	@ConfigurationProperties(prefix = "mybatis.configuration")
	// myBatisConfig() - application.properties에서 mybatis.configuration으로 시작하는 모든 설정을 읽어 스프링 컨테이너에 빈(Bean)으로 등록
	public org.apache.ibatis.session.Configuration mybatisConfig() {
		return new org.apache.ibatis.session.Configuration();
	}

}
