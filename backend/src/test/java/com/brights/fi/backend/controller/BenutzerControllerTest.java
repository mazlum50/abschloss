package com.brights.fi.backend.controller;

import com.brights.fi.backend.model.Benutzer;
import com.brights.fi.backend.service.BenutzerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BenutzerController.class)
public class BenutzerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BenutzerService benutzerService;

    private Benutzer benutzer;

    @BeforeEach
    void setUp() {
        benutzer = new Benutzer();
        benutzer.setId(1L);
        benutzer.setUserName("TestUser");
        benutzer.setUserEmail("test@example.com");
        benutzer.setPasswort("password");
    }

    @Test
    void testErstelleBenutzer() throws Exception {
        when(benutzerService.speichereBenutzer(any(Benutzer.class))).thenReturn(benutzer);

        mockMvc.perform(post("/api/benutzer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\": \"TestUser\", \"userEmail\": \"test@example.com\", \"passwort\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userName").value("TestUser"))
                .andExpect(jsonPath("$.userEmail").value("test@example.com"));
    }

    @Test
    void testFindeBenutzerDurchId() throws Exception {
        when(benutzerService.findeBenutzerDurchId(1L)).thenReturn(benutzer);

        mockMvc.perform(get("/api/benutzer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userName").value("TestUser"))
                .andExpect(jsonPath("$.userEmail").value("test@example.com"));
    }

    @Test
    void testGibAlleBenutzer() throws Exception {
        Benutzer benutzer2 = new Benutzer();
        benutzer2.setId(2L);
        benutzer2.setUserName("TestUser2");
        benutzer2.setUserEmail("test2@example.com");
        benutzer2.setPasswort("password2");

        when(benutzerService.gibAlleBenutzer()).thenReturn(Arrays.asList(benutzer, benutzer2));

        mockMvc.perform(get("/api/benutzer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].userName").value("TestUser"))
                .andExpect(jsonPath("$[0].userEmail").value("test@example.com"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].userName").value("TestUser2"))
                .andExpect(jsonPath("$[1].userEmail").value("test2@example.com"));
    }

    @Test
    void testAktualisiereBenutzer() throws Exception {
        when(benutzerService.aktualisiereBenutzer(anyLong(), any(Benutzer.class))).thenReturn(benutzer);

        mockMvc.perform(put("/api/benutzer/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\": \"UpdatedUser\", \"userEmail\": \"updated@example.com\", \"passwort\": \"newpassword\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userName").value("TestUser"))
                .andExpect(jsonPath("$.userEmail").value("test@example.com"));
    }

    @Test
    void testLoescheBenutzer() throws Exception {
        doNothing().when(benutzerService).loescheBenutzer(1L);

        mockMvc.perform(delete("/api/benutzer/1"))
                .andExpect(status().isOk());

        verify(benutzerService, times(1)).loescheBenutzer(1L);
    }
}
