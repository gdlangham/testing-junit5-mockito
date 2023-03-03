package guru.springframework.sfgpetclinic.services.springdatajpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.repositories.VisitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VisitSDJpaServiceTest {

    @Mock
    VisitRepository repository;

    @InjectMocks
    VisitSDJpaService service;

    @Test
    void findAll() {
        Visit v = new Visit();
        Set<Visit> visits = new HashSet<>();
        visits.add(v);
        when(repository.findAll()).thenReturn(visits);
        Set<Visit> found = service.findAll();
        assertThat(found).isNotNull();
        assertThat(found).isEqualTo(visits);
        assertThat(found).hasSize(1);
        verify(repository).findAll();
    }

    @Test
    void findById() {
        Visit visit = new Visit();
        visit.setDescription("new visit");
        when(repository.findById(1L)).thenReturn(Optional.of(visit));
        Visit found = service.findById(1L);
        assertThat(found).isNotNull();
        assertThat(found.getDescription()).isEqualTo("new visit");
        verify(repository).findById(anyLong());
        verify(repository, atMost(1)).findById(anyLong());
    }

    @Test
    void save() {
        Visit visit = new Visit();
        visit.setDescription("new visit");
        when(service.save(any(Visit.class))).thenReturn(visit);
        Visit found = service.save(visit);
        assertThat(found).isNotNull();
        assertThat(found.getDescription()).isEqualTo("new visit");
        verify(repository).save(any());
    }

    @Test
    void delete() {
        service.delete(new Visit());
        verify(repository).delete(any(Visit.class));
        verify(repository, atMost(1)).delete(any());
    }

    @Test
    void deleteById() {
        service.deleteById(1L);
        verify(repository).deleteById(anyLong());
        verify(repository, atMost(1)).deleteById(anyLong());
    }
}