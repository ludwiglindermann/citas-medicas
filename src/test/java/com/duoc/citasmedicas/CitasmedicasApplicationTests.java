package com.duoc.citasmedicas;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:oracle:thin:@basefullstack_high?TNS_ADMIN=C:/OracleWallet",
    "spring.jpa.hibernate.ddl-auto=none"
})
class CitasmedicasApplicationTests {

    @Test
    void contextLoads() {
    }
}