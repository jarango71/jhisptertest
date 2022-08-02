package com.condominium.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.condominium.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CondominiumDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CondominiumDTO.class);
        CondominiumDTO condominiumDTO1 = new CondominiumDTO();
        condominiumDTO1.setId(1L);
        CondominiumDTO condominiumDTO2 = new CondominiumDTO();
        assertThat(condominiumDTO1).isNotEqualTo(condominiumDTO2);
        condominiumDTO2.setId(condominiumDTO1.getId());
        assertThat(condominiumDTO1).isEqualTo(condominiumDTO2);
        condominiumDTO2.setId(2L);
        assertThat(condominiumDTO1).isNotEqualTo(condominiumDTO2);
        condominiumDTO1.setId(null);
        assertThat(condominiumDTO1).isNotEqualTo(condominiumDTO2);
    }
}
