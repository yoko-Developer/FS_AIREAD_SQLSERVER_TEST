package jp.co.ariseinnovation.tbccd.api.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "jp.co.ariseinnovation.tbccd.api.dao")
public class JpaConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        // エンティティクラスをスキャン
        List<String> managedClassNames = new ArrayList<>();
        ClassPathScanningCandidateComponentProvider scanner = 
            new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
        
        for (BeanDefinition bd : scanner.findCandidateComponents("jp.co.ariseinnovation.tbccd.api.entity")) {
            managedClassNames.add(bd.getBeanClassName());
        }
        
        // Hibernate設定
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "none");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
        properties.setProperty("hibernate.show_sql", "false");
        properties.setProperty("hibernate.format_sql", "false");
        properties.setProperty("hibernate.bytecode.use_reflection_optimizer", "false");
        properties.setProperty("hibernate.enhancer.enableDirtyTracking", "false");
        properties.setProperty("hibernate.enhancer.enableLazyInitialization", "false");
        properties.setProperty("hibernate.enhancer.enableAssociationManagement", "false");
        
        // CustomPersistenceUnitInfoを作成
        CustomPersistenceUnitInfo persistenceUnitInfo = new CustomPersistenceUnitInfo(
            "default", managedClassNames, properties, dataSource
        );
        
        // HibernatePersistenceProviderを直接使用してEntityManagerFactoryを作成
        HibernatePersistenceProvider persistenceProvider = new HibernatePersistenceProvider();
        return persistenceProvider.createContainerEntityManagerFactory(persistenceUnitInfo, properties);
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }
}
