# Spring Data Jpa 
- - -
- 라이브러리
  > - spring-boot-starter-web 
  > >  - spring-boot-starter-tomcat: 톰캣 (웹서버)    
  > >  - spring-webmvc: 스프링 웹 MVC
  > 
  > - spring-boot-starter-data-jpa       
  > >  - spring-boot-starter-aop    
  > >  - spring-boot-starter-jdbc   
  > > > HikariCP 커넥션 풀 (부트 2.0 기본)
  > >
  > > - hibernate + JPA: 하이버네이트 + JPA    
  > > - spring-data-jpa: 스프링 데이터 JPA
  > 
  > - spring-boot-starter(공통): 스프링 부트 + 스프링 코어 + 로깅 
  > > - spring-boot
  > > > - spring-core
  > >
  > > - spring-boot-starter-logging  
  > > > - logback, slf4j   
  > 
  > - spring-boot-starter-test   
  > > - junit: 테스트 프레임워크, 스프링 부트 2.2부터 junit5( jupiter ) 사용
  > > > - 과거 버전은 vintage
  > >
  > > - mockito: 목 라이브러리  
  > > - assertj: 테스트 코드를 좀 더 편하게 작성하게 도와주는 라이브러리   
  > > > - https://joel-costigliola.github.io/assertj/index.html   
  > >
  > > - spring-test: 스프링 통합 테스트 지원

- 순수 JPA
- Spring Data JPA
  - 공통 인터페이스
  - 쿼리 메소드 기능
> 쿼리 메소드 필터 조건
스프링 데이터 JPA 공식 문서 참고:   
> https://docs.spring.io/spring-data/jpa/docs/current/ reference/html/#jpa.query-methods.query-creation  
> https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.limit-query-result
  - 쿼리 메소드
    - @NamedQuery (JPA NameQuery, 실무X)
    - @Query (리포지토리 메소드에 쿼리 정의, 실무O)
    - 컬렉션 파라미터 바인딩
    - 반환타입   
  - 페이징
    * 순수 JPA 페이징 정렬
    * Spring Data JPA 페이징 정렬
      * Page, Slice
  - @EntityGraph
