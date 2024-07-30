package com.brights.fi.backend.model.unit.test;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.brights.fi.backend.model.*;

public class AusflugUnitTest {

    @Test
    public void testAusflugCreation() {

        Ausflug ausflug = new Ausflug();
        Long expectedId = 1L;
        String expectedTitel = "Test Ausflug";
        String expectedBeschreibung = "Test Beschreibung";
        String expectedReiseziel = "Test Reiseziel";
        LocalDateTime expectedAusflugsdatum = LocalDateTime.now();
        LocalDateTime expectedEnddatum = LocalDateTime.now().plusDays(1);
        LocalDateTime expectedEditierdatum = LocalDateTime.now();
        Integer expectedTeilnehmerAnzahl = 5;

        ausflug.setId(expectedId);
        ausflug.setTitel(expectedTitel);
        ausflug.setBeschreibung(expectedBeschreibung);
        ausflug.setReiseziel(expectedReiseziel);
        ausflug.setAusflugsdatum(expectedAusflugsdatum);
        ausflug.setEnddatum(expectedEnddatum);
        ausflug.setEditierdatum(expectedEditierdatum);
        ausflug.setTeilnehmerAnzahl(expectedTeilnehmerAnzahl);

        assertEquals(expectedId, ausflug.getId());
        assertEquals(expectedTitel, ausflug.getTitel());
        assertEquals(expectedBeschreibung, ausflug.getBeschreibung());
        assertEquals(expectedReiseziel, ausflug.getReiseziel());
        assertEquals(expectedAusflugsdatum, ausflug.getAusflugsdatum());
        assertEquals(expectedEnddatum, ausflug.getEnddatum());
        assertEquals(expectedEditierdatum, ausflug.getEditierdatum());
        assertEquals(expectedTeilnehmerAnzahl, ausflug.getTeilnehmerAnzahl());
    }

    @Test
    public void testAusflugInitialValues() {

        Ausflug ausflug = new Ausflug();

        assertNull(ausflug.getId());
        assertNull(ausflug.getTitel());
        assertNull(ausflug.getBeschreibung());
        assertNull(ausflug.getReiseziel());
        assertNull(ausflug.getAusflugsdatum());
        assertNull(ausflug.getEnddatum());
        assertNull(ausflug.getEditierdatum());
        assertNull(ausflug.getTeilnehmerAnzahl());
        assertNull(ausflug.getTeilnehmerListe());
        assertEquals(0, ausflug.getSachenZumMitbringen().size());
    }
}

