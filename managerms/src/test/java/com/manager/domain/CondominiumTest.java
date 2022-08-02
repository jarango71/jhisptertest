package com.manager.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.manager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CondominiumTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Condominium.class);
        Condominium condominium1 = new Condominium();
        condominium1.setId(1L);
        Condominium condominium2 = new Condominium();
        condominium2.setId(condominium1.getId());
        assertThat(condominium1).isEqualTo(condominium2);
        condominium2.setId(2L);
        assertThat(condominium1).isNotEqualTo(condominium2);
        condominium1.setId(null);
        assertThat(condominium1).isNotEqualTo(condominium2);
    }
}
