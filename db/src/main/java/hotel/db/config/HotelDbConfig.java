package hotel.db.config;

import hotel.db.entity.Item;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "hotelEntityManagerFactory",
                       transactionManagerRef = "hotelTransactionManager",
                       basePackages = {"hotel.db.repository"})
public class HotelDbConfig {

    private static final String DATASOURCE_PROPS_PREFIX = "spring.hotel.datasource";

    private static final String HIBERNATE_PROPS_PREFIX = "spring.hotel.jpa";

    protected static final String ENTITIES_PATH = Item.class.getPackage()
            .getName();

    private HibernateProperties hibernateProperties;

    public HotelDbConfig(HibernateProperties hibernateProperties) {
        this.hibernateProperties = hibernateProperties;
    }

    @Primary
    @Bean(name = "hotelDataSource")
    @ConfigurationProperties(prefix = DATASOURCE_PROPS_PREFIX)
    public DataSource hotelDataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Primary
    @Bean(name = "hotelEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean hotelEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                             @Qualifier("hotelDataSource")
                                                                                     DataSource hotelDataSource) {
        return builder.dataSource(hotelDataSource)
                .properties(hibernateProperties.getProperties(HIBERNATE_PROPS_PREFIX))
                .packages(ENTITIES_PATH)
                .persistenceUnit("hotelPersistenceUnit")
                .build();
    }

    @Primary
    @Bean(name = "hotelTransactionManager")
    public PlatformTransactionManager hotelTransactionManager(
            @Qualifier("hotelEntityManagerFactory")
                    EntityManagerFactory hotelEntityManagerFactory) {
        return new JpaTransactionManager(hotelEntityManagerFactory);
    }
}

