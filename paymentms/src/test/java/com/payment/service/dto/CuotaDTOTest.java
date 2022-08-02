package com.payment.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.payment.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CuotaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CuotaDTO.class);
        CuotaDTO cuotaDTO1 = new CuotaDTO();
        cuotaDTO1.setId(1L);
        CuotaDTO cuotaDTO2 = new CuotaDTO();
        assertThat(cuotaDTO1).isNotEqualTo(cuotaDTO2);
        cuotaDTO2.setId(cuotaDTO1.getId());
        assertThat(cuotaDTO1).isEqualTo(cuotaDTO2);
        cuotaDTO2.setId(2L);
        assertThat(cuotaDTO1).isNotEqualTo(cuotaDTO2);
        cuotaDTO1.setId(null);
        assertThat(cuotaDTO1).isNotEqualTo(cuotaDTO2);
    }
}
