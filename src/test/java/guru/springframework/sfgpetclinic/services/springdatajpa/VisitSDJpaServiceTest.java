package guru.springframework.sfgpetclinic.services.springdatajpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.setMaxElementsForPrinting;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
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
    void findAllVisits() {
        // given
        Visit v = new Visit();
        Set<Visit> visits = new HashSet<>();
        visits.add(v);
        given(repository.findAll()).willReturn(visits);
        // when
        Set<Visit> found = service.findAll();
        // then
        assertThat(found).isNotNull();
        assertThat(found).isEqualTo(visits);
        assertThat(found).hasSize(1);
//        verify(repository).findAll();
        then(repository).should().findAll();
    }

    @Test
    void findVisitsById() {
        // given
        Visit visit = new Visit();
        visit.setDescription("new visit");
        given(repository.findById(1L)).willReturn(Optional.of(visit));
        // when
        Visit found = service.findById(1L);
        // then
        assertThat(found).isNotNull();
        assertThat(found.getDescription()).isEqualTo("new visit");
        then(repository).should().findById(anyLong());
        then(repository).should(atMost(1)).findById(anyLong());
//        verify(repository).findById(anyLong());
//        verify(repository, atMost(1)).findById(anyLong());
    }

    @Test
    void saveVisit() {
        // given
        Visit visit = new Visit();
        visit.setDescription("new visit");
        given(service.save(any(Visit.class))).willReturn(visit);
        // when
        Visit found = service.save(visit);
        // then
        assertThat(found).isNotNull();
        assertThat(found.getDescription()).isEqualTo("new visit");
        then(repository).should().save(any(Visit.class));
        then(repository).shouldHaveNoMoreInteractions();
    }

    @Test
    void deleteVisit() {
        // when
        service.delete(new Visit());
        // then
        then(repository).should().delete(any(Visit.class));
        then(repository).should(atMost(1)).delete(any(Visit.class));
        then(repository).shouldHaveNoMoreInteractions();
    }

    @Test
    void deleteVisitById() {
        // when
        service.deleteById(1L);
        // then
        then(repository).should().deleteById(anyLong());
        then(repository).should(atMost(1)).deleteById(anyLong());
        then(repository).shouldHaveNoMoreInteractions();
    }
}