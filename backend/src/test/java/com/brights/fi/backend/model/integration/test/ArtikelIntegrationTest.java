package com.brights.fi.backend.model.integration.test;

import com.brights.fi.backend.model.Ausflug;
import com.brights.fi.backend.model.Benutzer;
import com.brights.fi.backend.model.Artikel;
import com.brights.fi.backend.repositories.ArtikelRepo;
import com.brights.fi.backend.repositories.BenutzerRepo;
import com.brights.fi.backend.repositories.AusflugRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
public class ArtikelIntegrationTest {

    @Autowired
    private ArtikelRepo artikelRepo;

    @Autowired
    private BenutzerRepo benutzerRepo;

    @Autowired
    private AusflugRepo ausflugRepo;

    private Benutzer benutzer;
    private Ausflug ausflug;

    @BeforeEach
    public void setUp() {
        benutzer = new Benutzer();
        benutzer.setUserName("Test Benutzer");
        benutzer = benutzerRepo.save(benutzer);

        ausflug = new Ausflug();
        ausflug.setTitel("Test Ausflug");
        ausflug = ausflugRepo.save(ausflug);
    }

    @Test
    public void testSaveAndFindArtikel() {

        Artikel artikel = new Artikel();
        artikel.setName("Test Artikel");
        artikel.setBenutzer(benutzer);
        artikel.setAusflug(ausflug);

        Artikel savedArtikel = artikelRepo.save(artikel);
        Optional<Artikel> foundArtikel = artikelRepo.findById(savedArtikel.getId());

        assertThat(foundArtikel).isPresent();
        assertThat(foundArtikel.get().getName()).isEqualTo("Test Artikel");
        assertThat(foundArtikel.get().getBenutzer()).isEqualTo(benutzer);
        assertThat(foundArtikel.get().getAusflug()).isEqualTo(ausflug);
    }

    @Test
    public void testDeleteArtikel() {
        Artikel artikel = new Artikel();
        artikel.setName("Test Artikel to Delete");
        artikel.setBenutzer(benutzer);
        artikel.setAusflug(ausflug);
        Artikel savedArtikel = artikelRepo.save(artikel);

        artikelRepo.deleteById(savedArtikel.getId());
        Optional<Artikel> deletedArtikel = artikelRepo.findById(savedArtikel.getId());

        assertThat(deletedArtikel).isNotPresent();
    }
}

