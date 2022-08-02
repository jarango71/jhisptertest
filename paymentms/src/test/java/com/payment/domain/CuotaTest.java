package com.payment.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.payment.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CuotaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cuota.class);
        Cuota cuota1 = new Cuota();
        cuota1.setId(1L);
        Cuota cuota2 = new Cuota();
        cuota2.setId(cuota1.getId());
        assertThat(cuota1).isEqualTo(cuota2);
        cuota2.setId(2L);
        assertThat(cuota1).isNotEqualTo(cuota2);
        cuota1.setId(null);
        assertThat(cuota1).isNotEqualTo(cuota2);
    }
}
