package com.example.elastic_api_service;

import com.example.elastic_api_service.service.RequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RequestControllerTest extends AbstractContainer {

    @Autowired
    public MockMvc mockMvc;

    @MockitoBean
    public RequestService service;

    @Test
    public void testSearch() throws Exception {
        mockMvc.perform(get("/api/audit/requests/search")
                        .param("query", "")
                        .param("statusCode", ""))
                .andExpect(status().isOk());
    }

    @Test
    public void testStats() throws Exception {
        mockMvc.perform(get("/api/audit/requests/stats")
                        .param("groupBy", "")
                        .param("direction", ""))
                .andExpect(status().isOk());
    }

    @Test
    public void testMatch() throws Exception {
        mockMvc.perform(get("/api/audit/requests")
                        .param("url", "")
                        .param("method", "")
                        .param("statusCode", "0"))
                .andExpect(status().isOk());
    }

}
