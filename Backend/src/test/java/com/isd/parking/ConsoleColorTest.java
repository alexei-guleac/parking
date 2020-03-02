package com.isd.parking;

import com.isd.parking.utils.ReflectionMethods;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.isd.parking.utils.ColorConsoleOutput.*;

@SpringBootTest
@Slf4j
public class ConsoleColorTest {

    @Test
    public void testConsoleOutput() {
        log.info(RED + "This text is red!" + RESET);
        log.info(ywTxt("This text is y!"));
        log.info(GREEN_BACKGROUND + "This text has a green background but default text!" + RESET);
        log.info(RED + "This text has red text but a default background!" + RESET);
        log.info(RED + GREEN_BACKGROUND + "This text has a green background and red text!" + RESET);
    }

    @Test// colors test
    public void testColors() {
        String[] f = new ReflectionMethods().getFields();
    }
}
