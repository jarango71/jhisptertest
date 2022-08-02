package com.condominium.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CuotaMapperTest {

    private CuotaMapper cuotaMapper;

    @BeforeEach
    public void setUp() {
        cuotaMapper = new CuotaMapperImpl();
    }
}
