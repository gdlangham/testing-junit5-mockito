package guru.springframework.sfgpetclinic.services.springdatajpa;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import guru.springframework.sfgpetclinic.repositories.VetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VetSDJpaServiceTest {

    @Mock
    VetRepository repository;

    @InjectMocks
    VetSDJpaService service;

    @Test
    void deleteById() {
        service.deleteById(1L);
        service.deleteById(1L);
        verify(repository, times(2)).deleteById(anyLong());
        verify(repository, never()).deleteById(4L);
        verify(repository, atMost(2)).deleteById(anyLong());
    }
}