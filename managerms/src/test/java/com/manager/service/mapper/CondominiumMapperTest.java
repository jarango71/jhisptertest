package com.manager.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CondominiumMapperTest {

    private CondominiumMapper condominiumMapper;

    @BeforeEach
    public void setUp() {
        condominiumMapper = new CondominiumMapperImpl();
    }
}
