package com.brights.fi.backend.controller;

import com.brights.fi.backend.model.Artikel;
import com.brights.fi.backend.model.Ausflug;
import com.brights.fi.backend.service.AusflugService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AusflugControllerTest {

    @InjectMocks
    private AusflugController ausflugController;

    @Mock
    private AusflugService ausflugService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ausflugController).build();
    }

    @Test
    public void gibAlleAusfluege() throws Exception {
        Ausflug ausflug1 = new Ausflug();
        ausflug1.setId(1L);
        Ausflug ausflug2 = new Ausflug();
        ausflug2.setId(2L);

        when(ausflugService.gibAlleAusfluege()).thenReturn(Arrays.asList(ausflug1, ausflug2));

        mockMvc.perform(get("/api/ausfluege"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    public void speicherAusflug() throws Exception {
        Ausflug ausflug = new Ausflug();
        ausflug.setId(1L);
        when(ausflugService.speicherAusflug(anyLong(), any(Ausflug.class))).thenReturn(ausflug);

        mockMvc.perform(post("/api/ausfluege/1")
                        .contentType("application/json")
                        .content("{\"titel\":\"Test Ausflug\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void hinzufuegeTeilnehmer() throws Exception {
        Ausflug ausflug = new Ausflug();
        ausflug.setId(1L);
        when(ausflugService.hinzufuegeTeilnehmer(anyLong(), anyLong())).thenReturn(ausflug);

        mockMvc.perform(post("/api/ausfluege/1/teilnehmer/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void hinzufuegeArtikel() throws Exception {
        Artikel artikel = new Artikel();
        artikel.setId(1L);
        when(ausflugService.hinzufuegeArtikel(anyLong(), any(Artikel.class))).thenReturn(artikel);

        mockMvc.perform(post("/api/ausfluege/1/artikel")
                        .contentType("application/json")
                        .content("{\"name\":\"Test Artikel\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void entferneTeilnehmer() throws Exception {
        Ausflug ausflug = new Ausflug();
        ausflug.setId(1L);
        when(ausflugService.entferneTeilnehmer(anyLong(), anyLong())).thenReturn(ausflug);

        mockMvc.perform(delete("/api/ausfluege/1/teilnehmer/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void gibAusflugDurchId() throws Exception {
        Ausflug ausflug = new Ausflug();
        ausflug.setId(1L);
        when(ausflugService.gibAusflugDurchId(anyLong())).thenReturn(ausflug);

        mockMvc.perform(get("/api/ausfluege/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

//    @Test
//    public void aktualisiereAusflug() throws Exception {
//        Ausflug ausflug = new Ausflug();
//        ausflug.setId(1L);
//        when(ausflugService.aktualisiereAusflug(anyLong(), any(Ausflug.class))).thenReturn(ausflug);
//
//        mockMvc.perform(put("/api/ausfluege/1")
//                        .contentType("application/json")
//                        .content("{\"titel\":\"Aktualisierter Titel\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L));
//    }

//    @Test
//    public void loescheAusflug() throws Exception {
//        Ausflug ausflug = new Ausflug();
//        ausflug.setId(1L);
//        when(ausflugService.loescheAusflug(anyLong())).thenReturn(ausflug);
//
//        mockMvc.perform(delete("/api/ausfluege/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L));
//    }
}

