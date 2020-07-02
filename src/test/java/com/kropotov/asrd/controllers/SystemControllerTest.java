package com.kropotov.asrd.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Artem Kropotov on 18.06.2020
 */
@SpringBootTest
@SqlGroup({@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:schema.sql", "classpath:data.sql"})})
class SystemControllerTest {


    @Autowired
    private SystemController systemController;

    @Mock
    Principal principal;


    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(systemController).build();
    }

    @Test
    void displaySystems() throws Exception {
    }

    @Test
    void testDisplayByIdValid() throws Exception {
        mockMvc.perform(get("/systems/1/show"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("system"));
    }

    @Test
    void testDisplayByIdNotValid() throws Exception {
        mockMvc.perform(get("/systems/999/show"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/systems"
                ));
    }

    @Test
    void displaySystemForm() throws Exception {
        mockMvc.perform(get("/systems/999/update"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"
                ));
    }


    @Test
    void editSystem() throws Exception {
    }
}