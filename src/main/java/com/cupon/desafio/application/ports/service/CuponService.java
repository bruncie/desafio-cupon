package com.cupon.desafio.application.ports.service;

import com.cupon.desafio.adapters.adapters.CuponMapper;
import com.cupon.desafio.adapters.dto.CuponRequestDto;
import com.cupon.desafio.adapters.dto.CuponResponseDto;
import com.cupon.desafio.application.ports.repository.CuponRepository;
import com.cupon.desafio.domain.entity.Cupon;
import com.cupon.desafio.domain.usecase.CuponUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CuponService {

    private final CuponRepository cuponRepository;
    private final CuponUsecase cuponUsecase;
    private final CuponMapper mapper;

    @Transactional
    public CuponResponseDto create(CuponRequestDto requestDto) {

        var code = cuponUsecase.removeSpecialCharactersCode(requestDto.getCode());

        if (cuponRepository.existsByCode(code))
            throw new IllegalArgumentException("Já existe cupom com esse código");

        var cuponSaved =  cuponRepository.save(cuponUsecase.createCupon(requestDto));
        return mapper.toResponseDto(cuponSaved);
    }

    @Transactional(readOnly = true)
    public Cupon findByCode(String code) {
        return cuponRepository.findByCodeAndActive(code)
                .orElseThrow(() -> new IllegalArgumentException("Cupom não encontrado"));
    }

    @Transactional
    public void delete(String code) {
        var cuponDeleted = cuponUsecase.deleteCupon(this.findByCode(code));
        cuponRepository.save(cuponDeleted);
    }
}
