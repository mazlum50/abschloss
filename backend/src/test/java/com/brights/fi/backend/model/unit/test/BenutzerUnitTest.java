package com.brights.fi.backend.model.unit.test;

import org.junit.jupiter.api.Test;
import com.brights.fi.backend.model.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BenutzerUnitTest {

    @Test
    public void testBenutzerCreation() {

        Benutzer benutzer = new Benutzer();
        Long expectedId = 1L;
        String expectedUserName = "TestUser";
        String expectedUserEmail = "testuser@example.com";
        String expectedPasswort = "securepassword";


        benutzer.setId(expectedId);
        benutzer.setUserName(expectedUserName);
        benutzer.setUserEmail(expectedUserEmail);
        benutzer.setPasswort(expectedPasswort);


        assertEquals(expectedId, benutzer.getId());
        assertEquals(expectedUserName, benutzer.getUserName());
        assertEquals(expectedUserEmail, benutzer.getUserEmail());
        assertEquals(expectedPasswort, benutzer.getPasswort());
    }

    @Test
    public void testBenutzerInitialValues() {

        Benutzer benutzer = new Benutzer();

        assertNull(benutzer.getId());
        assertNull(benutzer.getUserName());
        assertNull(benutzer.getUserEmail());
        assertNull(benutzer.getPasswort());
        assertEquals(0, benutzer.getAusfluege().size());
        assertEquals(0, benutzer.getSachenZumMitbringen().size());
    }
}

