package com.example.elastic_api_service;

import com.example.elastic_api_service.service.MethodService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MethodControllerTest extends AbstractContainer {

    @Autowired
    public MockMvc mockMvc;

    @MockitoBean
    public MethodService service;

    @Test
    public void testSearch() throws Exception {
        mockMvc.perform(get("/api/audit/methods/search")
                        .param("query", "")
                        .param("level", ""))
                .andExpect(status().isOk());
    }

    @Test
    public void testStats() throws Exception {
        mockMvc.perform(get("/api/audit/methods/stats")
                        .param("groupBy", "")
                        .param("from", LocalDateTime.now().toString())
                        .param("to", LocalDateTime.now().toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void testMatch() throws Exception {
        mockMvc.perform(get("/api/audit/methods")
                        .param("method", "")
                        .param("level", "")
                        .param("eventType", ""))
                .andExpect(status().isOk());
    }

}
