package com.cupon.desafio.adapters.dto;

import com.cupon.desafio.domain.entity.CuponStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuponResponseDto {

    private String code;
    private String description;
    private BigDecimal discountValue;
    private LocalDateTime expirationDate;
    private boolean published;
    private LocalDateTime createdAt;
    private CuponStatus status;
    private boolean redeemed;
}
