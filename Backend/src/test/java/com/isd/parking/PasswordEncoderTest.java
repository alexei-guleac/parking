package com.isd.parking;

import com.isd.parking.security.PasswordEncoding;
import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class PasswordEncoderTest {

    @Test
    public void testPassEncoding() {
        log.info(new PasswordEncoding.CustomPasswordEncoder(new ColorConsoleOutput()).getH("aRduin1$"));
    }
}
