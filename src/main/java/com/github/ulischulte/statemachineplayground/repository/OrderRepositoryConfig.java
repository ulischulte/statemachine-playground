package com.github.ulischulte.statemachineplayground.repository;

import com.github.ulischulte.statemachineplayground.model.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackageClasses = {OrderRepository.class}, entityManagerFactoryRef = "orderStatemachineEntityManagerFactory",
    transactionManagerRef = "orderStatemachineTransactionManager")
public class OrderRepositoryConfig {

  private DataSource dataSource;

  @Bean
  public DataSource initializeDataSource() {
    return new EmbeddedDatabaseBuilder()
        .setType(EmbeddedDatabaseType.H2)
        .addScript("db/createOrders.sql")
        .build();
  }

  @Bean(name = "orderStatemachineEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactoryBean.setDataSource(getDataSource());
    entityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter());
    entityManagerFactoryBean.setPersistenceUnitName("orderUnit");
    entityManagerFactoryBean.setPackagesToScan(ClassUtils.getPackageName(Order.class));

    return entityManagerFactoryBean;
  }

  private HibernateJpaVendorAdapter hibernateJpaVendorAdapter() {
    final HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
    hibernateJpaVendorAdapter.setGenerateDdl(false);
    hibernateJpaVendorAdapter.setShowSql(true);
    hibernateJpaVendorAdapter.getJpaPropertyMap().put("hibernate.hbm2ddl.auto", "validate");
    hibernateJpaVendorAdapter.getJpaPropertyMap().put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
    return hibernateJpaVendorAdapter;
  }

  @Bean(name = "orderStatemachineTransactionManager")
  public JpaTransactionManager transactionManager() {
    final JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
    transactionManager.setDataSource(getDataSource());
    return transactionManager;
  }

  private DataSource getDataSource() {
    if (dataSource == null) {
      dataSource = initializeDataSource();
    }
    return dataSource;
  }
}
