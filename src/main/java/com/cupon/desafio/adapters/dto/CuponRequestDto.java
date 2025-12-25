package com.cupon.desafio.adapters.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuponRequestDto {

    @NotBlank(message = "Valor em branco não é permitido para code")
    @Size(max = 6, message = "O código deve ter no máximo 6 caracteres")
    private String code;

    @NotBlank(message = "Valor em branco não é permitido para code")
    @Size(max = 200, message = "o description deve ter no máximo 200 caracteres")
    private String description;

    @NotNull(message = "discountValue não pode ser nulo")
    @PositiveOrZero(message = "discountValue deve ser maior ou igual a zero")
    private BigDecimal discountValue;

    @FutureOrPresent(message = "expirationDate deve ser uma data futura ou presente")
    private LocalDateTime expirationDate;

    private boolean published;
}
