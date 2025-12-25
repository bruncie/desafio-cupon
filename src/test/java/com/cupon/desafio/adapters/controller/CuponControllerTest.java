package com.cupon.desafio.adapters.controller;

import com.cupon.desafio.adapters.adapters.CuponMapper;
import com.cupon.desafio.adapters.dto.CuponRequestDto;
import com.cupon.desafio.adapters.dto.CuponResponseDto;
import com.cupon.desafio.application.ports.service.CuponService;
import com.cupon.desafio.domain.entity.Cupon;
import com.cupon.desafio.domain.entity.CuponStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CuponControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CuponService service;

    @Mock
    private CuponMapper mapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private AutoCloseable mocks;

    @BeforeEach
    void setup() {
        mocks = MockitoAnnotations.openMocks(this);
        objectMapper.findAndRegisterModules();
        var controller = new CuponController(service, mapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) mocks.close();
    }

    @Test
    void create_shouldReturn201AndLocationHeader_whenValid() throws Exception {
        var request = CuponRequestDto.builder()
                .code("ABC")
                .description("desc")
                .discountValue(BigDecimal.ONE)
                .expirationDate(LocalDateTime.now().plusDays(1))
                .published(true)
                .build();

        CuponResponseDto resp = CuponResponseDto.builder().code("ABC").build();
        when(service.create(any())).thenReturn(resp);

        mockMvc.perform(post("/api/cupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/cupons/ABC"))
                .andExpect(jsonPath("$.code").value("ABC"));

        verify(service).create(any());
    }

    @Test
    void create_shouldReturn400_whenValidationFails() throws Exception {
        var request = CuponRequestDto.builder()
                .code("") // invalid: NotBlank
                .description("")
                .discountValue(null)
                .expirationDate(LocalDateTime.now().minusDays(1))
                .published(true)
                .build();

        mockMvc.perform(post("/api/cupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void getByCode_shouldReturn200AndBody() throws Exception {
        // prepare a Cupon entity that service should return
        Cupon cupon = Cupon.builder()
                .id(5L)
                .code("Z")
                .description("d")
                .discountValue(BigDecimal.ONE)
                .expirationDate(LocalDateTime.now().plusDays(2))
                .published(true)
                .status(CuponStatus.ACTIVE)
                .redeemed(false)
                .build();

        CuponResponseDto dto = CuponResponseDto.builder()
                .code("Z")
                .description("d")
                .discountValue(BigDecimal.ONE)
                .expirationDate(cupon.getExpirationDate())
                .published(true)
                .status(CuponStatus.ACTIVE)
                .redeemed(false)
                .build();

        when(service.findByCode("Z")).thenReturn(cupon);
        when(mapper.toResponseDto(cupon)).thenReturn(dto);

        mockMvc.perform(get("/api/cupons/Z"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("Z"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(service).findByCode("Z");
        verify(mapper).toResponseDto(cupon);
    }

    @Test
    void delete_shouldReturnNoContent() throws Exception {
        doNothing().when(service).delete("C1");

        mockMvc.perform(delete("/api/cupons/C1"))
                .andExpect(status().isNoContent());

        verify(service).delete("C1");
    }
}
