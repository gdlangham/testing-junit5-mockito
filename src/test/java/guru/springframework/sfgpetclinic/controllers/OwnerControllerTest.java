package guru.springframework.sfgpetclinic.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import java.util.ArrayList;
import java.util.List;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.fauxspring.Model;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    @Mock
    OwnerService service;

    @Mock
    Model model;

    @Mock
    BindingResult bindingResult;

    @InjectMocks
    OwnerController controller;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void setUp() {
        given(service.findAllByLastNameLike(stringArgumentCaptor.capture())).willAnswer(invocationOnMock -> {
            List<Owner> owners = new ArrayList<>();
            String name = invocationOnMock.getArgument(0);
            if (name.equals("%Bunny%")) {
                owners.add(new Owner(1l, "Bugs", "Bunny"));
                return owners;
            } else if (name.equals("%NotFound%")) {
                return owners;
            } else if (name.equals("%FindAll%")) {
                owners.add(new Owner(5l, "Bugs", "Found"));
                owners.add(new Owner(4l, "Daffy", "Duck"));
                return owners;
            }
            throw new RuntimeException("Invalid Argument");
        });
    }
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

    @Test
    void processFindFormWildcardTest() {
        // given
        Owner owner = new Owner(5l, "Bugs", "Bunny");
        List<Owner> ownerList = new ArrayList<>();
        final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        given(service.findAllByLastNameLike(captor.capture())).willReturn(ownerList);

        // when
        String viewName = controller.processFindForm(owner, bindingResult, null);
        // then
        assertThat("%Bunny%").isEqualToIgnoringCase(captor.getValue());
    }

    @Test
    void processFindFormWildcardStringAnnotationTest() {
        // given
        Owner owner = new Owner(5l, "Bugs", "Bunny");
        // when
        String viewName = controller.processFindForm(owner, bindingResult, null);
        // then
        assertThat("%Bunny%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
        assertThat("redirect:/owners/1").isEqualTo(viewName);
    }

    @Test
    void processFindFormWildcardNotFound() {
        // given
        Owner owner = new Owner(5l, "Bugs", "NotFound");
        // when
        String viewName = controller.processFindForm(owner, bindingResult, model);
        // then
        assertThat("%NotFound%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
        assertThat("owners/findOwners").isEqualTo(viewName);
    }

    @Test
    void processFindFormWildcardFound() {
        // given
        Owner owner = new Owner(5l, "Bugs", "FindAll");
        InOrder inOrder = Mockito.inOrder(model, service);
        // when
        String viewName = controller.processFindForm(owner, bindingResult, model);
        // then
        assertThat("%FindAll%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
        assertThat("owners/ownersList").isEqualTo(viewName);

        inOrder.verify(service).findAllByLastNameLike(anyString());
        inOrder.verify(model).addAttribute(anyString(), anyList());
    }
}