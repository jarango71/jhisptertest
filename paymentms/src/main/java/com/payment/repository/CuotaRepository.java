package com.payment.repository;

import com.payment.domain.Cuota;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Cuota entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CuotaRepository extends JpaRepository<Cuota, Long> {}
