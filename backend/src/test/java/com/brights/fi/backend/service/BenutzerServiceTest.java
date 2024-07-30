package com.brights.fi.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.brights.fi.backend.model.Benutzer;
import com.brights.fi.backend.repositories.BenutzerRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BenutzerServiceTest {

    @Mock
    private BenutzerRepo benutzerRepo;

    @InjectMocks
    private BenutzerService benutzerService;

    @Test
    public void testSpeichereBenutzer() {
        Benutzer benutzer = new Benutzer();
        when(benutzerRepo.save(benutzer)).thenReturn(benutzer);

        Benutzer result = benutzerService.speichereBenutzer(benutzer);

        assertEquals(benutzer, result);
        verify(benutzerRepo, times(1)).save(benutzer);
    }

    @Test
    public void testFindeBenutzerDurchIdExistiert() {
        Long benutzerId = 1L;
        Benutzer benutzer = new Benutzer();
        when(benutzerRepo.findById(benutzerId)).thenReturn(Optional.of(benutzer));

        Benutzer result = benutzerService.findeBenutzerDurchId(benutzerId);

        assertEquals(benutzer, result);
        verify(benutzerRepo, times(1)).findById(benutzerId);
    }

    @Test
    public void testFindeBenutzerDurchIdExistiertNicht() {
        Long benutzerId = 1L;
        when(benutzerRepo.findById(benutzerId)).thenReturn(Optional.empty());

        Benutzer result = benutzerService.findeBenutzerDurchId(benutzerId);

        assertNull(result);
        verify(benutzerRepo, times(1)).findById(benutzerId);
    }

    @Test
    public void testGibAlleBenutzer() {
        List<Benutzer> benutzerListe = new ArrayList<>();
        benutzerListe.add(new Benutzer());
        benutzerListe.add(new Benutzer());

        when(benutzerRepo.findAll()).thenReturn(benutzerListe);

        List<Benutzer> result = benutzerService.gibAlleBenutzer();

        assertEquals(2, result.size());
        verify(benutzerRepo, times(1)).findAll();
    }

    @Test
    public void testAktualisiereBenutzerExistiert() {
        Long benutzerId = 1L;
        Benutzer originalBenutzer = new Benutzer();
        originalBenutzer.setUserName("Original Name");
        Benutzer updatedBenutzer = new Benutzer();
        updatedBenutzer.setUserName("Updated Name");

        when(benutzerRepo.findById(benutzerId)).thenReturn(Optional.of(originalBenutzer));
        when(benutzerRepo.save(any(Benutzer.class))).thenReturn(originalBenutzer);

        Benutzer result = benutzerService.aktualisiereBenutzer(benutzerId, updatedBenutzer);

        assertEquals("Updated Name", result.getUserName());
        verify(benutzerRepo, times(1)).findById(benutzerId);
        verify(benutzerRepo, times(1)).save(originalBenutzer);
    }

    @Test
    public void testAktualisiereBenutzerExistiertNicht() {
        Long benutzerId = 1L;
        Benutzer updatedBenutzer = new Benutzer();

        when(benutzerRepo.findById(benutzerId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            benutzerService.aktualisiereBenutzer(benutzerId, updatedBenutzer);
        });

        assertEquals("Benutzer with benutzerId " + benutzerId + " does not exist", exception.getMessage());
        verify(benutzerRepo, times(1)).findById(benutzerId);
        verify(benutzerRepo, never()).save(any(Benutzer.class));
    }

    @Test
    public void testLoescheBenutzerExistiert() {
        Long benutzerId = 1L;
        when(benutzerRepo.existsById(benutzerId)).thenReturn(true);

        benutzerService.loescheBenutzer(benutzerId);

        verify(benutzerRepo, times(1)).existsById(benutzerId);
        verify(benutzerRepo, times(1)).deleteById(benutzerId);
    }

    @Test
    public void testLoescheBenutzerExistiertNicht() {
        Long benutzerId = 1L;
        when(benutzerRepo.existsById(benutzerId)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            benutzerService.loescheBenutzer(benutzerId);
        });

        assertEquals("Benutzer with id " + benutzerId + " does not exist", exception.getMessage());
        verify(benutzerRepo, times(1)).existsById(benutzerId);
        verify(benutzerRepo, never()).deleteById(benutzerId);
    }
}
