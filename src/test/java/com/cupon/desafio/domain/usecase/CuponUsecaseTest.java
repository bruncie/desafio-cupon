package com.cupon.desafio.domain.usecase;

import com.cupon.desafio.adapters.dto.CuponRequestDto;
import com.cupon.desafio.domain.entity.Cupon;
import com.cupon.desafio.domain.entity.CuponStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CuponUsecaseTest {

    private final CuponUsecase usecase = new CuponUsecase();

    @Test
    void createCupon_shouldMapFieldsAndAddHalfUnitToDiscount() {
        var request = CuponRequestDto.builder()
                .code("Cóp#-1")
                .description("desc")
                .discountValue(BigDecimal.valueOf(1.0))
                .expirationDate(LocalDateTime.now().plusDays(1))
                .published(true)
                .build();

        Cupon cupon = usecase.createCupon(request);

        assertNotNull(cupon);
        // code must have removed special chars and accents
        assertEquals("Cop1", cupon.getCode());
        assertEquals("desc", cupon.getDescription());
        // original 1.0 + 0.5
        assertEquals(0, cupon.getDiscountValue().compareTo(BigDecimal.valueOf(1.5)));
        assertEquals(request.getExpirationDate(), cupon.getExpirationDate());
        assertTrue(cupon.isPublished());
        assertEquals(CuponStatus.ACTIVE, cupon.getStatus());
    }

    @Test
    void deleteCupon_shouldSetStatusInactive() {
        Cupon cupon = Cupon.builder()
                .code("ABC")
                .status(CuponStatus.ACTIVE)
                .build();

        Cupon deleted = usecase.deleteCupon(cupon);

        assertEquals(CuponStatus.INACTIVE, deleted.getStatus());
    }

    @Test
    void removeSpecialCharactersCode_shouldHandleNullAndEmpty() {
        assertNull(usecase.removeSpecialCharactersCode(null));
        assertEquals("", usecase.removeSpecialCharactersCode(""));
    }

    @Test
    void removeSpecialCharactersCode_shouldRemoveAccentsAndNonAlphanumerics() {
        String input = "áéíóúãõç-# 123";
        String out = usecase.removeSpecialCharactersCode(input);
        // accents removed and non alnum removed
        assertEquals("aeiouao c123".replace(" ", ""), out);
    }
}

