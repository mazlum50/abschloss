package com.brights.fi.backend.model.integration.test;

import com.brights.fi.backend.repositories.BenutzerRepo;
import com.brights.fi.backend.repositories.ArtikelRepo;
import com.brights.fi.backend.repositories.AusflugRepo;
import com.brights.fi.backend.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
public class BenutzerIntegrationTest {

    @Autowired
    private BenutzerRepo benutzerRepo;

    @Autowired
    private ArtikelRepo artikelRepo;

    @Autowired
    private AusflugRepo ausflugRepo;

    @Test
    public void testSaveAndFindBenutzer() {

        Benutzer benutzer = new Benutzer();
        benutzer.setUserName("TestUser");
        benutzer.setUserEmail("testuser@example.com");
        benutzer.setPasswort("securepassword");

        Benutzer savedBenutzer = benutzerRepo.save(benutzer);
        Optional<Benutzer> foundBenutzer = benutzerRepo.findById(savedBenutzer.getId());


        assertThat(foundBenutzer).isPresent();
        assertThat(foundBenutzer.get().getUserName()).isEqualTo("TestUser");
        assertThat(foundBenutzer.get().getUserEmail()).isEqualTo("testuser@example.com");
        assertThat(foundBenutzer.get().getPasswort()).isEqualTo("securepassword");
    }

    @Test
    public void testDeleteBenutzer() {

        Benutzer benutzer = new Benutzer();
        benutzer.setUserName("UserToDelete");
        Benutzer savedBenutzer = benutzerRepo.save(benutzer);


        benutzerRepo.deleteById(savedBenutzer.getId());
        Optional<Benutzer> deletedBenutzer = benutzerRepo.findById(savedBenutzer.getId());

        assertThat(deletedBenutzer).isNotPresent();
    }

    @Test
    public void testBenutzerWithAusfluegeAndArtikel() {

        Benutzer benutzer = new Benutzer();
        benutzer.setUserName("UserWithAusfluegeAndArtikel");
        Benutzer savedBenutzer = benutzerRepo.save(benutzer);

        Ausflug ausflug = new Ausflug();
        ausflug.setTitel("Test Ausflug");
        ausflug = ausflugRepo.save(ausflug);

        Artikel artikel = new Artikel();
        artikel.setName("Test Artikel");
        artikel.setBenutzer(savedBenutzer);
        artikel.setAusflug(ausflug);
        artikel = artikelRepo.save(artikel);

        savedBenutzer.getSachenZumMitbringen().add(artikel);
        savedBenutzer.getAusfluege().add(ausflug);
        benutzerRepo.save(savedBenutzer);


        Optional<Benutzer> foundBenutzer = benutzerRepo.findById(savedBenutzer.getId());


        assertThat(foundBenutzer).isPresent();
        assertThat(foundBenutzer.get().getSachenZumMitbringen()).contains(artikel);
        assertThat(foundBenutzer.get().getAusfluege()).contains(ausflug);
    }
}

