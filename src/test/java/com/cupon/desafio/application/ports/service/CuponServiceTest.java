package com.cupon.desafio.application.ports.service;

import com.cupon.desafio.adapters.adapters.CuponMapper;
import com.cupon.desafio.adapters.dto.CuponRequestDto;
import com.cupon.desafio.adapters.dto.CuponResponseDto;
import com.cupon.desafio.application.ports.repository.CuponRepository;
import com.cupon.desafio.domain.entity.Cupon;
import com.cupon.desafio.domain.entity.CuponStatus;
import com.cupon.desafio.domain.usecase.CuponUsecase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CuponServiceTest {

    @Mock
    private CuponRepository repository;

    @Mock
    private CuponUsecase usecase;

    @Mock
    private CuponMapper mapper;

    @InjectMocks
    private CuponService service;

    @Captor
    private ArgumentCaptor<Cupon> cuponCaptor;

    private AutoCloseable mocks;

    @BeforeEach
    void setup() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) mocks.close();
    }

    @Test
    void create_shouldSaveAndReturnResponse() {
        var request = CuponRequestDto.builder()
                .code("ABC")
                .description("desc")
                .discountValue(BigDecimal.ONE)
                .expirationDate(LocalDateTime.now().plusDays(1))
                .published(true)
                .build();

        when(usecase.removeSpecialCharactersCode("ABC")).thenReturn("ABC");
        when(repository.existsByCode("ABC")).thenReturn(false);

        Cupon cuponToSave = Cupon.builder().code("ABC").build();
        when(usecase.createCupon(request)).thenReturn(cuponToSave);
        Cupon saved = Cupon.builder().id(1L).code("ABC").build();
        when(repository.save(cuponToSave)).thenReturn(saved);

        CuponResponseDto responseDto = CuponResponseDto.builder().code("ABC").build();
        when(mapper.toResponseDto(saved)).thenReturn(responseDto);

        CuponResponseDto result = service.create(request);

        assertNotNull(result);
        assertEquals("ABC", result.getCode());
        verify(repository).save(cuponCaptor.capture());
        assertEquals("ABC", cuponCaptor.getValue().getCode());
    }

    @Test
    void create_whenCodeExists_shouldThrow() {
        var request = CuponRequestDto.builder().code("EXIST").build();
        when(usecase.removeSpecialCharactersCode("EXIST")).thenReturn("EXIST");
        when(repository.existsByCode("EXIST")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.create(request));
        verify(repository, never()).save(any());
    }

    @Test
    void findByCode_shouldReturnWhenPresent() {
        Cupon c = Cupon.builder().code("X").build();
        when(repository.findByCodeAndActive("X")).thenReturn(Optional.of(c));

        Cupon res = service.findByCode("X");
        assertEquals("X", res.getCode());
    }

    @Test
    void findByCode_whenNotFound_shouldThrow() {
        when(repository.findByCodeAndActive("Y")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.findByCode("Y"));
    }

    @Test
    void delete_shouldMarkInactiveAndSave() {
        Cupon c = Cupon.builder().code("D").status(CuponStatus.ACTIVE).build();
        when(repository.findByCodeAndActive("D")).thenReturn(Optional.of(c));
        Cupon after = Cupon.builder().code("D").status(CuponStatus.INACTIVE).build();
        when(usecase.deleteCupon(c)).thenReturn(after);

        service.delete("D");

        verify(repository).save(after);
    }
}
