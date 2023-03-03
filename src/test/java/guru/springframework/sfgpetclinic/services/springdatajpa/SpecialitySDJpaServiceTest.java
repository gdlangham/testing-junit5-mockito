package guru.springframework.sfgpetclinic.services.springdatajpa;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    void deleteById() {
        service.deleteById(1L);
        service.deleteById(2L);
        verify(repository, times(2)).deleteById(anyLong());
    }

    @Test
    void deleteByIdAtLeastOnce() {
        service.deleteById(1L);
        service.deleteById(2L);
        verify(repository, atLeastOnce()).deleteById(anyLong());
        verify(repository, atMost(2)).deleteById(anyLong());
        verify(repository, never()).deleteById(4L);
    }

    @Test
    void delete() {
        service.delete(new Speciality());
        verify(repository).delete(any());
    }
}