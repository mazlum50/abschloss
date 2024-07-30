package com.brights.fi.backend.controller;

import com.brights.fi.backend.model.Artikel;
import com.brights.fi.backend.model.Ausflug;
import com.brights.fi.backend.service.ArtikelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ArtikelControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ArtikelService artikelService;

    @InjectMocks
    private ArtikelController artikelController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(artikelController).build();
    }

    @Test
    public void testGibAlleArtikel() throws Exception {
        when(artikelService.gibAlleArtikel()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/artikel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(artikelService, times(1)).gibAlleArtikel();
    }

    @Test
    public void testSpeichereArtikel() throws Exception {
        Ausflug ausflug = new Ausflug();
        Artikel artikel = new Artikel();
        artikel.setName("Test Artikel");

        when(artikelService.speichereArtikel(any(Long.class), any(Artikel.class))).thenReturn(ausflug);

        mockMvc.perform(post("/api/artikel/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Artikel\"}"))
                .andExpect(status().isOk());

        verify(artikelService, times(1)).speichereArtikel(any(Long.class), any(Artikel.class));
    }

    @Test
    public void testLoescheArtikel() throws Exception {
        doNothing().when(artikelService).loescheArtikel(any(Long.class));

        mockMvc.perform(delete("/api/artikel/1"))
                .andExpect(status().isOk());

        verify(artikelService, times(1)).loescheArtikel(any(Long.class));
    }

//    @Test
//    public void testAktualisiereArtikel() throws Exception {
//        Artikel artikel = new Artikel();
//        artikel.setId(1L);
//        artikel.setName("Updated Artikel");
//
//        when(artikelService.aktualisiereArtikel(any(Long.class), any(Artikel.class))).thenReturn(artikel);
//
//        mockMvc.perform(put("/api/artikel/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"Updated Artikel\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Updated Artikel"));
//
//        verify(artikelService, times(1)).aktualisiereArtikel(any(Long.class), any(Artikel.class));
//    }

    @Test
    public void testGibArtikelDurchId() throws Exception {
        Artikel artikel = new Artikel();
        artikel.setId(1L);
        artikel.setName("Test Artikel");

        when(artikelService.gibArtikelDurchId(any(Long.class))).thenReturn(artikel);

        mockMvc.perform(get("/api/artikel/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Artikel"));

        verify(artikelService, times(1)).gibArtikelDurchId(any(Long.class));
    }
}
