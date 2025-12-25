package com.cupon.desafio.domain.usecase;

import com.cupon.desafio.adapters.dto.CuponRequestDto;
import com.cupon.desafio.domain.entity.Cupon;
import com.cupon.desafio.domain.entity.CuponStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.Normalizer;

@Component
public class CuponUsecase {

    public Cupon createCupon(CuponRequestDto requestDto) {
        return Cupon.builder()
                .code(this.removeSpecialCharactersCode(requestDto.getCode()))
                .description(requestDto.getDescription())
                .discountValue(requestDto.getDiscountValue().add(BigDecimal.valueOf(0.5)))
                .expirationDate(requestDto.getExpirationDate())
                .published(requestDto.isPublished())
                .status(CuponStatus.ACTIVE)
                .build();
    }

    public Cupon deleteCupon(Cupon cupon) {
        cupon.setStatus(CuponStatus.INACTIVE);
        return cupon;
    }

    public String removeSpecialCharactersCode(String code) {
        if (code == null || code.isEmpty()) {
            return code;
        }

        String textoNormalizado = Normalizer.normalize(code, Normalizer.Form.NFD);

        return textoNormalizado.replaceAll("[^a-zA-Z0-9]", "");
    }

}
