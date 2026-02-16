package com.springproject.universitymanagementsystem.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAll() {
    }

    @Test
    void getById() {
    }

    @Test
    void create() {
    }

    @Test
    void updateSelf() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void testGetById_NotFound() throws Exception {
        mockMvc.perform(get("/api/students/999")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}