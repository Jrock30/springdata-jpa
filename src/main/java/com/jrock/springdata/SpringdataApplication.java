package com.jrock.springdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@EnableJpaRepositories(basePackages = "com.jrock.springdata.repository") // 스프링부트를 사용하면 엔트리 포인트 클래스 위치를 지정(해당 패키지와 하위패키지 인식) 그러므로 생략가능
@SpringBootApplication
public class SpringdataApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringdataApplication.class, args);
    }

}
