package com.studentinformation.repository;

import com.studentinformation.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

}
