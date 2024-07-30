package com.brights.fi.backend.model.integration.test;

import com.brights.fi.backend.repositories.AusflugRepo;
import com.brights.fi.backend.repositories.BenutzerRepo;
import com.brights.fi.backend.repositories.ArtikelRepo;
import com.brights.fi.backend.model.Artikel;
import com.brights.fi.backend.model.Ausflug;
import com.brights.fi.backend.model.Benutzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
public class AusflugIntegrationTest {

    @Autowired
    private AusflugRepo ausflugRepo;

    @Autowired
    private BenutzerRepo benutzerRepo;

    @Autowired
    private ArtikelRepo artikelRepo;

    private Benutzer benutzer;
    private Artikel artikel;

    @BeforeEach
    public void setUp() {
        benutzer = new Benutzer();
        benutzer.setUserName("Test Benutzer");
        benutzer = benutzerRepo.save(benutzer);

        artikel = new Artikel();
        artikel.setName("Test Artikel");
        artikel.setBenutzer(benutzer);
        artikel = artikelRepo.save(artikel);
    }

    @Test
    public void testSaveAndFindAusflug() {
        // Arrange
        Ausflug ausflug = new Ausflug();
        ausflug.setTitel("Test Ausflug");
        ausflug.setBeschreibung("Test Beschreibung");
        ausflug.setReiseziel("Test Reiseziel");
        ausflug.setAusflugsdatum(LocalDateTime.now());
        ausflug.setEnddatum(LocalDateTime.now().plusDays(1));
        ausflug.setEditierdatum(LocalDateTime.now());
        ausflug.setTeilnehmerAnzahl(5);
        ausflug.setTeilnehmerListe(new HashSet<>());
        ausflug.getTeilnehmerListe().add(benutzer);
        ausflug.getSachenZumMitbringen().add(artikel);

        // Act
        Ausflug savedAusflug = ausflugRepo.save(ausflug);
        Optional<Ausflug> foundAusflug = ausflugRepo.findById(savedAusflug.getId());

        assertThat(foundAusflug).isPresent();
        assertThat(foundAusflug.get().getTitel()).isEqualTo("Test Ausflug");
        assertThat(foundAusflug.get().getBeschreibung()).isEqualTo("Test Beschreibung");
        assertThat(foundAusflug.get().getReiseziel()).isEqualTo("Test Reiseziel");
        assertThat(foundAusflug.get().getTeilnehmerAnzahl()).isEqualTo(5);
        assertThat(foundAusflug.get().getTeilnehmerListe()).contains(benutzer);
        assertThat(foundAusflug.get().getSachenZumMitbringen()).contains(artikel);
    }

    @Test
    public void testDeleteAusflug() {

        Ausflug ausflug = new Ausflug();
        ausflug.setTitel("Test Ausflug to Delete");
        Ausflug savedAusflug = ausflugRepo.save(ausflug);

        ausflugRepo.deleteById(savedAusflug.getId());
        Optional<Ausflug> deletedAusflug = ausflugRepo.findById(savedAusflug.getId());

        assertThat(deletedAusflug).isNotPresent();
    }
}

