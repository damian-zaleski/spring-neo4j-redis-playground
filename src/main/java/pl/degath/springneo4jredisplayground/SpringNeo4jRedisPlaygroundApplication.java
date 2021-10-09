package pl.degath.springneo4jredisplayground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringNeo4jRedisPlaygroundApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringNeo4jRedisPlaygroundApplication.class, args);
    }

}
