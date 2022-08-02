package com.payment.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PagoMapperTest {

    private PagoMapper pagoMapper;

    @BeforeEach
    public void setUp() {
        pagoMapper = new PagoMapperImpl();
    }
}
