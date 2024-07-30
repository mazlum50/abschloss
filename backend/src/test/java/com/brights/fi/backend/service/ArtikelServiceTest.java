package com.brights.fi.backend.service;

import com.brights.fi.backend.model.Artikel;
import com.brights.fi.backend.model.Ausflug;
import com.brights.fi.backend.model.Benutzer;
import com.brights.fi.backend.repositories.ArtikelRepo;
import com.brights.fi.backend.repositories.AusflugRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ArtikelServiceTest {
    @Mock
    private ArtikelRepo artikelRepo;

    @Mock
    private AusflugRepo ausflugRepo;
    @InjectMocks
    private ArtikelService artikelService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
//////////////////////////////////testgibAlleArtikel/////////////////////
    @Test
    public void testGibAlleArtikel() {
        Artikel artikel1 = new Artikel();
        artikel1.setId(1L);
        artikel1.setName("Artikel 1");

        Artikel artikel2 = new Artikel();
        artikel2.setId(2L);
        artikel2.setName("Artikel 2");

        List<Artikel> artikelListe = Arrays.asList(artikel1, artikel2);

        when(artikelRepo.findAll()).thenReturn(artikelListe);

        List<Artikel> result = artikelService.gibAlleArtikel();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Artikel 1", result.get(0).getName());
        assertEquals("Artikel 2", result.get(1).getName());

        verify(artikelRepo, times(1)).findAll();
    }
    //////////////////////////////////testSpeichereArtikel/////////////////////
    @Test
    public void testSpeichereArtikel() {
        Long ausflugId = 1L;
        Artikel artikel = new Artikel();
        artikel.setName("Reiseführer");
        System.out.println(artikel);

        Ausflug ausflug = new Ausflug();
        ausflug.setId(ausflugId);
        System.out.println(ausflug);

        when(ausflugRepo.findById(ausflugId)).thenReturn(Optional.of(ausflug));

        artikelService.speichereArtikel(ausflugId, artikel);
        assertEquals(ausflug, artikel.getAusflug());
        assertTrue(ausflug.getSachenZumMitbringen().contains(artikel));
        verify(ausflugRepo, times(1)).findById(ausflugId);
        verify(ausflugRepo, times(1)).save(ausflug);

        System.out.println(ausflug);
        System.out.println(artikel.getAusflug());
    }

    ////////////////////////////testLoescheArtikel////////////////////
    @Test
    public void testLoescheArtikelExistiert() {
        Long artikelId = 1L;

        when(artikelRepo.existsById(artikelId)).thenReturn(true);

        artikelService.loescheArtikel(artikelId);

        verify(artikelRepo, times(1)).existsById(artikelId);
        verify(artikelRepo, times(1)).deleteById(artikelId);
    }
    @Test
    public void testLoescheArtikelExistiertNicht() {
        Long artikelId = 1L;

        when(artikelRepo.existsById(artikelId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> artikelService.loescheArtikel(artikelId));

        verify(artikelRepo, times(1)).existsById(artikelId);
        verify(artikelRepo, never()).deleteById(artikelId);
    }
    ////////////////////////////aktualisiereArtikel////////////////////
//    @Test
//    public void testAktualisiereArtikelExistiert() {
//        Long artikelId = 1L;
//        Artikel vorhandenerArtikel = new Artikel();
//        vorhandenerArtikel.setId(artikelId);
//        vorhandenerArtikel.setName("Altes Name");
//
//        Artikel aktualisierterArtikel = new Artikel();
//        aktualisierterArtikel.setName("Neues Name");
//        aktualisierterArtikel.setBenutzer(new Benutzer());
//        aktualisierterArtikel.setAusflug(new Ausflug());
//
//        when(artikelRepo.findById(artikelId)).thenReturn(Optional.of(vorhandenerArtikel));
//        when(artikelRepo.save(any(Artikel.class))).thenReturn(vorhandenerArtikel);
//
//        Artikel result = artikelService.aktualisiereArtikel(artikelId, aktualisierterArtikel);
//
//        assertEquals("Neues Name", result.getName());
//        verify(artikelRepo, times(1)).findById(artikelId);
//        verify(artikelRepo, times(1)).save(vorhandenerArtikel);
//    }

//    @Test
//    public void testAktualisiereArtikelExistiertNicht() {
//        Long artikelId = 1L;
//        Artikel aktualisierterArtikel = new Artikel();
//
//        when(artikelRepo.findById(artikelId)).thenReturn(Optional.empty());
//
//        assertThrows(IllegalArgumentException.class, () -> artikelService.aktualisiereArtikel(artikelId, aktualisierterArtikel));
//
//        verify(artikelRepo, times(1)).findById(artikelId);
//        verify(artikelRepo, never()).save(any(Artikel.class));
//    }
    ////////////////////////////// gibArtikelDurchId/////////////////////////////////////////////
    @Test
    public void testGibArtikelDurchIdExistiert() {
        Long artikelId = 1L;
        Artikel artikel = new Artikel();
        artikel.setId(artikelId);
        artikel.setName("Test Artikel");

        when(artikelRepo.findById(artikelId)).thenReturn(Optional.of(artikel));

        Artikel gefundenArtikel = artikelService.gibArtikelDurchId(artikelId);

        assertNotNull(gefundenArtikel);
        assertEquals(artikelId, gefundenArtikel.getId());
        assertEquals("Test Artikel", gefundenArtikel.getName());

        verify(artikelRepo, times(1)).findById(artikelId);
    }

    @Test
    public void testGibArtikelDurchIdExistiertNicht() {
        Long artikelId = 1L;

        when(artikelRepo.findById(artikelId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            artikelService.gibArtikelDurchId(artikelId);
        });

        assertEquals("Artikel with id " + artikelId + " does not exist", exception.getMessage());

        verify(artikelRepo, times(1)).findById(artikelId);
    }
}
