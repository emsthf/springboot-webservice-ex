package com.emsthf.webserv.springboot.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 이 클래스를 상속할 경우 이 클래스의 필드를 컬럼으로 인식하도록
@EntityListeners(AuditingEntityListener.class) // 엔티티 리스너에 Auditing 기능 포함
public abstract class BaseTimeEntity { // abstract = 추상 클래스

    @CreatedDate // 엔티티가 생성어 저장될 때 시간 저장
    private LocalDateTime createdDate;

    @LastModifiedDate // 엔티티 값이 변경될 때 시간 저장
    private LocalDateTime modifiedDate;
}
