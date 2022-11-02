package com.studentinformation.controller;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
public abstract class ControllerTestSetup {

    protected static final String TEST_STUDENT_NUM = "student";
    protected static final String TEST_PROFESSOR_NUM = "professor";
    protected static final String TEST_ADMIN_NUM = "admin";

    @Autowired
    protected WebApplicationContext wac;

    protected MockMvc mock;

    @BeforeEach
    void init() {
        mock = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    }
}
