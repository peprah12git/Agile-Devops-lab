package bookshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;


import org.springframework.boot.jdbc.DataSourceBuilder;


@Configuration
public class DatabaseConnection {

    @Configuration
    public static class DataSourceConfig {

        @Bean
        public DataSource dataSource() {
            return DataSourceBuilder.create()
                    .url("jdbc:mysql://localhost:3306/bookshop")
                    .username("root")
                    .password("noah_1@23.Djanor")
                    .driverClassName("com.mysql.cj.jdbc.Driver")
                    .build();
        }
    }

}
