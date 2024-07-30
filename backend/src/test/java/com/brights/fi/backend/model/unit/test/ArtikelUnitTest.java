package com.brights.fi.backend.model.unit.test;

import com.brights.fi.backend.model.Artikel;
import com.brights.fi.backend.model.Ausflug;
import com.brights.fi.backend.model.Benutzer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ArtikelUnitTest {

    @Test
    public void testArtikelCreation() {

        Artikel artikel = new Artikel();
        Long expectedId = 1L;
        String expectedName = "Test Artikel";
        Benutzer expectedBenutzer = new Benutzer();
        Ausflug expectedAusflug = new Ausflug();


        artikel.setId(expectedId);
        artikel.setName(expectedName);
        artikel.setBenutzer(expectedBenutzer);
        artikel.setAusflug(expectedAusflug);


        assertEquals(expectedId, artikel.getId());
        assertEquals(expectedName, artikel.getName());
        assertEquals(expectedBenutzer, artikel.getBenutzer());
        assertEquals(expectedAusflug, artikel.getAusflug());
    }

    @Test
    public void testArtikelInitialValues() {
        Artikel artikel = new Artikel();

        assertNull(artikel.getId());
        assertNull(artikel.getName());
        assertNull(artikel.getBenutzer());
        assertNull(artikel.getAusflug());
    }
}
