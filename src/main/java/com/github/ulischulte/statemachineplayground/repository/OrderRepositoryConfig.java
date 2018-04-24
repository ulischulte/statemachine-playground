package com.github.ulischulte.statemachineplayground.repository;

import com.github.ulischulte.statemachineplayground.model.Order;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.statemachine.data.jpa.JpaRepositoryStateMachine;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackageClasses = {
    OrderRepository.class,
    JpaStateMachineRepository.class })
public class OrderRepositoryConfig {

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
    final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactoryBean.setDataSource(dataSource);
    entityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter());
    entityManagerFactoryBean.setPersistenceUnitName("orderUnit");
    entityManagerFactoryBean.setPackagesToScan(ClassUtils.getPackageName(Order.class),
        ClassUtils.getPackageName(JpaRepositoryStateMachine.class));

    return entityManagerFactoryBean;
  }

  private HibernateJpaVendorAdapter hibernateJpaVendorAdapter() {
    final HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
    hibernateJpaVendorAdapter.setGenerateDdl(true);
    hibernateJpaVendorAdapter.setShowSql(true);
    hibernateJpaVendorAdapter.getJpaPropertyMap().put("hibernate.hbm2ddl.auto", "validate");
    hibernateJpaVendorAdapter.getJpaPropertyMap().put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
    return hibernateJpaVendorAdapter;
  }

  @Bean
  public JpaTransactionManager transactionManager(DataSource dataSource) {
    final JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory(dataSource).getObject());
    transactionManager.setDataSource(dataSource);
    return transactionManager;
  }

}
