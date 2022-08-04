package com.studentinformation.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public abstract class BaseEntity {

    @Column(name = "create_date", updatable = false)
    protected LocalDateTime createDate;

    @Column(name = "last_modified_date")
    protected LocalDateTime lastModifiedDate;
}