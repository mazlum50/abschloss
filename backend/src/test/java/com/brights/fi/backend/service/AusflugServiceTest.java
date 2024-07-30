package com.brights.fi.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.brights.fi.backend.model.Artikel;
import com.brights.fi.backend.model.Ausflug;
import com.brights.fi.backend.model.Benutzer;
import com.brights.fi.backend.repositories.AusflugRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AusflugServiceTest {
    @Mock
    private AusflugRepo ausflugRepo;

    @Mock
    private BenutzerService benutzerService;

    @InjectMocks
    private AusflugService ausflugService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /////////////////////////////////////testGibAlleAusfluege///////////////////////////////////////////
    @Test
    public void testGibAlleAusfluege() {
        List<Ausflug> ausfluege = new ArrayList<>();
        Ausflug ausfluege1 = new Ausflug();
        Ausflug ausfluege2 = new Ausflug();
        ausfluege1.setTitel("Hannover");
        ausfluege2.setTitel("Hamburg");
        ausfluege.add(ausfluege1);
        ausfluege.add(ausfluege2);
        when(ausflugRepo.findAll()).thenReturn(ausfluege);

        List<Ausflug> result = ausflugService.gibAlleAusfluege();

        assertEquals(2, result.size());
        verify(ausflugRepo, times(1)).findAll();
    }
    /////////////////////////////////////testSpeicherAusflug///////////////////////////////////////////

    @Test
    public void testSpeicherAusflug() {
        Long benutzerId = 1L;
        Benutzer benutzer = new Benutzer();
        benutzer.setId(benutzerId);
        Ausflug ausflug = new Ausflug();

        when(benutzerService.findeBenutzerDurchId(benutzerId)).thenReturn(benutzer);
        when(ausflugRepo.save(any(Ausflug.class))).thenReturn(ausflug);

        Ausflug result = ausflugService.speicherAusflug(benutzerId, ausflug);

        assertEquals(benutzerId.toString(), result.getErstellerDesAusflugsId());
        verify(benutzerService, times(1)).findeBenutzerDurchId(benutzerId);
        verify(ausflugRepo, times(1)).save(ausflug);
    }
    /////////////////////////////////////testHinzuFuegeArtikel///////////////////////////////////////////

    @Test
    public void testHinzuFuegeArtikel() {
        Long ausflugId = 1L;
        Artikel artikel = new Artikel();
        Ausflug ausflug = new Ausflug();

        when(ausflugRepo.findById(ausflugId)).thenReturn(Optional.of(ausflug));
        when(ausflugRepo.save(any(Ausflug.class))).thenReturn(ausflug);

        Artikel result = ausflugService.hinzufuegeArtikel(ausflugId, artikel);

        assertEquals(ausflug, result.getAusflug());
        verify(ausflugRepo, times(1)).findById(ausflugId);
        verify(ausflugRepo, times(1)).save(ausflug);
    }
    /////////////////////////////////////testHinzuFuegeTeilnehmer///////////////////////////////////////////
    @Test
    public void testHinzuFuegeTeilnehmer() {
        Long ausflugId = 1L;
        Long benutzerId = 1L;
        Ausflug ausflug = new Ausflug();
        Benutzer benutzer = new Benutzer();

        when(ausflugRepo.findById(ausflugId)).thenReturn(Optional.of(ausflug));
        when(benutzerService.findeBenutzerDurchId(benutzerId)).thenReturn(benutzer);
        when(ausflugRepo.save(any(Ausflug.class))).thenReturn(ausflug);

        Ausflug result = ausflugService.hinzufuegeTeilnehmer(ausflugId, benutzerId);

        assertTrue(result.getTeilnehmerListe().contains(benutzer));
        verify(ausflugRepo, times(1)).findById(ausflugId);
        verify(benutzerService, times(1)).findeBenutzerDurchId(benutzerId);
        verify(benutzerService, times(1)).speichereBenutzer(benutzer);
        verify(ausflugRepo, times(1)).save(ausflug);
    }
    /////////////////////////////////////testEntferneTeilnehmer///////////////////////////////////////////
    @Test
    public void testEntferneTeilnehmer() {
        Long ausflugId = 1L;
        Long benutzerId = 1L;
        Ausflug ausflug = new Ausflug();
        Benutzer benutzer = new Benutzer();

        ausflug.getTeilnehmerListe().add(benutzer);
        benutzer.getAusfluege().add(ausflug);

        when(ausflugRepo.findById(ausflugId)).thenReturn(Optional.of(ausflug));
        when(benutzerService.findeBenutzerDurchId(benutzerId)).thenReturn(benutzer);
        when(ausflugRepo.save(any(Ausflug.class))).thenReturn(ausflug);

        Ausflug result = ausflugService.entferneTeilnehmer(ausflugId, benutzerId);

        assertFalse(result.getTeilnehmerListe().contains(benutzer));
        verify(ausflugRepo, times(1)).findById(ausflugId);
        verify(benutzerService, times(1)).findeBenutzerDurchId(benutzerId);
        verify(benutzerService, times(1)).speichereBenutzer(benutzer);
        verify(ausflugRepo, times(1)).save(ausflug);
    }
///////////////////////////////////////testGibAusflugDurchId////////////////////////////////////////////////
    @Test
    public void testGibAusflugDurchIdExistiert() {
        Long ausflugId = 1L;
        Ausflug ausflug = new Ausflug();

        when(ausflugRepo.findById(ausflugId)).thenReturn(Optional.of(ausflug));

        Ausflug result = ausflugService.gibAusflugDurchId(ausflugId);

        assertNotNull(result);
        verify(ausflugRepo, times(1)).findById(ausflugId);
    }

    @Test
    public void testGibAusflugDurchIdExistiertNicht() {
        Long ausflugId = 1L;

        when(ausflugRepo.findById(ausflugId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ausflugService.gibAusflugDurchId(ausflugId);
        });

        assertEquals("Ausflug with id " + ausflugId + " does not exist", exception.getMessage());
        verify(ausflugRepo, times(1)).findById(ausflugId);
    }
//////////////////////////////////////////testAktualisiereAusflu////////////////////////////////////////////
//    @Test
//    public void testAktualisiereAusflug() {
//        Long ausflugId = 1L;
//        Ausflug originalAusflug = new Ausflug();
//        originalAusflug.setTitel("Original");
//        Ausflug updatedAusflug = new Ausflug();
//        updatedAusflug.setTitel("Updated");
//
//        when(ausflugRepo.findById(ausflugId)).thenReturn(Optional.of(originalAusflug));
//        when(ausflugRepo.save(any(Ausflug.class))).thenReturn(originalAusflug);
//
//        Ausflug result = ausflugService.aktualisiereAusflug(ausflugId, updatedAusflug);
//
//        assertEquals("Updated", result.getTitel());
//        verify(ausflugRepo, times(1)).findById(ausflugId);
//        verify(ausflugRepo, times(1)).save(originalAusflug);
//    }
////////////////////////////////////////testLoescheAusflug////////////////////////////////////////////
//    @Test
//    public void testLoescheAusflug() {
//        Long ausflugId = 1L;
//        Ausflug ausflug = new Ausflug();
//
//        when(ausflugRepo.findById(ausflugId)).thenReturn(Optional.of(ausflug));
//
//        Ausflug result = ausflugService.loescheAusflug(ausflugId);
//
//        assertEquals(ausflug, result);
//        verify(ausflugRepo, times(1)).findById(ausflugId);
//        verify(ausflugRepo, times(1)).delete(ausflug);
//    }
}
