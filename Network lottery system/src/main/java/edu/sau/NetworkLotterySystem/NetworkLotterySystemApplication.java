package edu.sau.NetworkLotterySystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@EnableCaching
@MapperScan("edu.sau.NetworkLotterySystem.mapper")
@SpringBootApplication
public class NetworkLotterySystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetworkLotterySystemApplication.class, args);
    }

}
