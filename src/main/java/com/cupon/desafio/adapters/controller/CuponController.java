package com.cupon.desafio.adapters.controller;

import com.cupon.desafio.adapters.dto.CuponRequestDto;
import com.cupon.desafio.adapters.dto.CuponResponseDto;
import com.cupon.desafio.adapters.adapters.CuponMapper;
import com.cupon.desafio.application.ports.service.CuponService;
import com.cupon.desafio.domain.entity.Cupon;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/cupons")
@RequiredArgsConstructor
@Tag(name = "Cupons", description = "Operações para gerenciar cupons")
public class CuponController {

    private final CuponService cuponService;
    private final CuponMapper mapper;

    @Operation(summary = "Cria um novo cupon")
    @ApiResponse(responseCode = "201", description = "Cupon criado")
    @PostMapping
    public ResponseEntity<CuponResponseDto> create(@Valid @RequestBody CuponRequestDto requestDto) {
        CuponResponseDto created = cuponService.create(requestDto);
        // monta location com o código caso esteja disponível
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(created.getCode())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Busca um cupom por código (apenas ativos)")
    @ApiResponse(responseCode = "200", description = "Cupon encontrado",
            content = @Content(schema = @Schema(implementation = CuponResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Cupom não encontrado")
    @GetMapping("/{code}")
    public ResponseEntity<CuponResponseDto> getByCode(@PathVariable String code) {
        Cupon cupon = cuponService.findByCode(code);
        return ResponseEntity.ok(mapper.toResponseDto(cupon));
    }

    @Operation(summary = "Deleta um cupom pelo código")
    @ApiResponse(responseCode = "204", description = "Cupom deletado")
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> delete(@PathVariable String code) {
        cuponService.delete(code);
        return ResponseEntity.noContent().build();
    }
}
