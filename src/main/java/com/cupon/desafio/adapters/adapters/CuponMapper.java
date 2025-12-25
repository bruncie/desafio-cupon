package com.cupon.desafio.adapters.adapters;

import com.cupon.desafio.adapters.dto.CuponResponseDto;
import com.cupon.desafio.domain.entity.Cupon;
import org.springframework.stereotype.Component;

@Component
public class CuponMapper {
    public CuponResponseDto toResponseDto(Cupon cupon) {
        return CuponResponseDto.builder()
                .code(cupon.getCode())
                .description(cupon.getDescription())
                .discountValue(cupon.getDiscountValue())
                .expirationDate(cupon.getExpirationDate())
                .published(cupon.isPublished())
                .createdAt(cupon.getCreatedAt())
                .status(cupon.getStatus())
                .redeemed(cupon.isRedeemed())
                .build();
    }
}
