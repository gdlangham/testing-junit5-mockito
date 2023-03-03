package guru.springframework.sfgpetclinic.services.springdatajpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SpecialitySDJpaServiceTest {

    @Mock
    SpecialtyRepository repository;

    @InjectMocks
    SpecialitySDJpaService service;

    @Test
    void findByIdTest() {
        Speciality speciality = new Speciality();

        when(repository.findById(1L)).thenReturn(Optional.of(speciality));
        Speciality foundSpecialty = service.findById(1L);
        assertThat(foundSpecialty).isNotNull();
        verify(repository).findById(anyLong());
    }

    @Test
    void findByIdBDDTest() {
        // given
        Speciality speciality = new Speciality();
        given(repository.findById(1L)).willReturn(Optional.of(speciality));
        // when
        Speciality found = service.findById(1L);
        // then
        assertThat(found).isNotNull();
        then(repository).should().findById(anyLong());
        then(repository).should(times(1)).findById(anyLong());
        then(repository).shouldHaveNoMoreInteractions();
    }

    @Test
    void deleteByObj() {
        // given
        Speciality speciality = new Speciality();
        // when
        service.delete(speciality);
        // then
        verify(repository).delete(any(Speciality.class));
        then(repository).should().delete(any(Speciality.class));
    }

    @Test
    void deleteById() {
        // when
        service.deleteById(1L);
        service.deleteById(2L);
        // then
        then(repository).should(times(2)).deleteById(anyLong());
        verify(repository, times(2)).deleteById(anyLong());
    }

    @Test
    void deleteByIdAtLeastOnce() {
        // when
        service.deleteById(1L);
        service.deleteById(2L);
        // then
//        verify(repository, atLeastOnce()).deleteById(anyLong());
//        verify(repository, atMost(2)).deleteById(anyLong());
//        verify(repository, never()).deleteById(4L);
        then(repository).should(atLeastOnce()).deleteById(anyLong());
        then(repository).should(times(2)).deleteById(anyLong());
        then(repository).should(atMost(2)).deleteById(anyLong());
        then(repository).should(never()).deleteById(43L);
        then(repository).shouldHaveNoMoreInteractions();
    }

    @Test
    void delete() {
        service.delete(new Speciality());
        verify(repository).delete(any());
    }
}