package org.appslapp.AppsLappServer.business.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthenticationManagerTest {
    @InjectMocks
    AuthenticationManager authenticationManager;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationManager)
                .build();
    }

    @Test
    public void testGetEncoder() {
        assertTrue(authenticationManager.getEncoder() instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testConfigure() throws Exception {
        mockMvc.perform(get(new URI("http://localhost:8080/api/student/")))
                .andExpect(status().isForbidden());

//        mockMvc.perform(get("/api/auth/register"))
//                .andExpect(status().isOk());
    }
}
