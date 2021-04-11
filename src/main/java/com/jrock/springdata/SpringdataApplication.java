package com.jrock.springdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sound.sampled.spi.AudioFileWriter;
import java.util.Optional;
import java.util.UUID;

//@EnableJpaRepositories(basePackages = "com.jrock.springdata.repository") // 스프링부트를 사용하면 엔트리 포인트 클래스 위치를 지정(해당 패키지와 하위패키지 인식) 그러므로 생략가능
@EnableJpaAuditing // 스프링 부트 설정 클래스에 적용해야함, 참고로 저장시점에 저장데이터만 입력하고 싶으면 @EnableJpaAuditing(modifyOnCreate = false)
@SpringBootApplication
public class SpringdataApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringdataApplication.class, args);
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of(UUID.randomUUID().toString());

        // 인터페이스에서 람다가 하나면 위 처럼 변환 가능
        // 실무에서는 세션 정보나, 스프링 시큐리티 로그인 정보에서 ID를 받음
//        return new AuditorAware<String>() {
//            @Override
//            public Optional<String> getCurrentAuditor() {
//                return Optional.of(UUID.randomUUID().toString());
//            }
//        };
    }

}
