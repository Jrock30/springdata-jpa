package com.jrock.springdata.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

/**
 * JPA 주요 이벤트 어노테이션
 * @PrePersist, @PostPersist @PreUpdate, @PostUpdate
 */
@MappedSuperclass // Entity 상속 관계 적용
@Getter
public class JpaBaseEntity {

    @Column(updatable = false) // 직접 update false
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @PrePersist // persist 하기 전에 이벤트 발생
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createDate = now;
        updateDate = now;
    }

    @PreUpdate // persist
    public void preUpdate() {
        updateDate = LocalDateTime.now();
    }
}
