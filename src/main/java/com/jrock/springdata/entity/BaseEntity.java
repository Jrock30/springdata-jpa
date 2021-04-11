package com.jrock.springdata.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 *  @EnableJpaAuditing -> 스프링 부트 설정 클래스에 적용해야함
 *  @EntityListeners(AuditingEntityListener.class) -> 엔티티에 적용
 *  참고로 저장시점에 저장데이터만 입력하고 싶으면 @EnableJpaAuditing(modifyOnCreate = false)
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity extends  BaseTimeEntity{

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;
}
