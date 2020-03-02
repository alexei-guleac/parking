package com.isd.parking;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@TestPropertySource(properties = {"classpath:application.properties"})
public class SpringBootPropertyInjectionIntegrationTest {

    @Value("${spring.ldap.base=ou=people}")
    private String test;

    @Test
    public void whenSpringBootPropertyProvided_thenProperlyInjected() {
        assertEquals(test,"ou=people");
    }
}
