package com.cupon.desafio.application.ports.repository;

import com.cupon.desafio.domain.entity.Cupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Repository
public interface CuponRepository extends JpaRepository<Cupon, Long> {

    @Query("select c from Cupon c where c.code = :code and c.status = com.cupon.desafio.domain.entity.CuponStatus.ACTIVE")
    Optional<Cupon> findByCodeAndActive(@Param("code") String code);

    Optional<Cupon> findByCode(String code);

    boolean existsByCode(String code);

}
