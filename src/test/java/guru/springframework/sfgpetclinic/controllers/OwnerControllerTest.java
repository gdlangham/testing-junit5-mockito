package guru.springframework.sfgpetclinic.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    @Mock
    OwnerService service;

    @Mock
    BindingResult bindingResult;

    @InjectMocks
    OwnerController controller;

    @Test
    void processCreationFormWithValidationError() {
        // given
        given(bindingResult.hasErrors()).willReturn(true);
        // when
        String result = controller.processCreationForm(new Owner(1L, "",""), bindingResult);
        // then
        assertThat(result).isEqualTo("owners/createOrUpdateOwnerForm");
        then(service).should(never()).save(any(Owner.class));
    }

    @Test
    void processCreationFormWithNoValidationError() {
        // given
        Owner owner = new Owner(5l, "Bugs", "Bunny");
        given(service.save(any(Owner.class))).willReturn(owner);
        given(bindingResult.hasErrors()).willReturn(false);
        // when
        String result = controller.processCreationForm(owner, bindingResult);
        // then
        assertThat(result).isEqualTo("redirect:/owners/5");
        then(service).should().save(any(Owner.class));
    }
}