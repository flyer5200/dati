package com.dati.auth.server.controller;

import com.dati.auth.domain.service.GuestService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GuestController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("GuestController integration tests")
class GuestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GuestService guestService;

    @Test
    @DisplayName("Should successfully issue guest token")
    void guestLogin_success() throws Exception {
        when(guestService.issueGuestToken()).thenReturn("mock-guest-token");

        mockMvc.perform(post("/v1/auth/guest")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-guest-token"));

        verify(guestService).issueGuestToken();
    }
}
