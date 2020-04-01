package com.isd.parking.controller.web;


import com.isd.parking.ParkingApplication;
import com.isd.parking.models.ParkingLot;
import com.isd.parking.models.enums.ParkingLotStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;

import static com.isd.parking.controller.ApiEndpoints.parking;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for parking lot controller
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ParkingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ParkingLotControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private final String parkingPoint = parking + "/";

    /**
     * Get environment root url
     */
    private String getRootUrl() {
        return "http://localhost:" + port;
    }

    @Test
    public void contextLoads() {

    }

    /**
     * Test for get all parking lots by api request
     * Asserts that response body is not null and contains parking lots
     */
    @Test
    public void testGetAllParkingLots() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + parking,
                HttpMethod.GET, entity, String.class);
        assertNotNull(response.getBody());
    }

    /**
     * Test for get parking lot by id api request
     * Asserts that response body is not null and contains parking lot specified by id
     */
    @Test
    public void testGetParkingLotById() {
        ParkingLot parkingLot = restTemplate.getForObject(getRootUrl() + parking + "/1", ParkingLot.class);
        System.out.println(parkingLot.getNumber());
        assertNotNull(parkingLot);
    }

    /**
     * Test for update parking lot by id
     * Asserts that updated parking lot is not null
     */
    @Test
    public void testUpdateParkingLot() {
        long id = 1L;
        ParkingLot parkingLot = restTemplate.getForObject(getRootUrl() + parkingPoint + id, ParkingLot.class);

        parkingLot.setStatus(ParkingLotStatus.FREE);
        parkingLot.setUpdatedAt(new Date());

        restTemplate.put(getRootUrl() + parkingPoint + id, parkingLot);
        ParkingLot updatedParkingLot = restTemplate.getForObject(getRootUrl() + parkingPoint + id, ParkingLot.class);
        assertNotNull(updatedParkingLot);
    }

    //TODO: tests creating and deleting parking lots

    @Test
    public void testCreateParkingLot() {
        ParkingLot parkingLot = ParkingLot.builder()
                .id(1L)
                .number(1)
                .status(ParkingLotStatus.FREE)
                .updatedAt(new Date()).build();

        ResponseEntity<ParkingLot> postResponse = restTemplate.postForEntity(getRootUrl() + parking, parkingLot, ParkingLot.class);
        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());
    }

    /**
     * Test for deleting parking lot by id from database
     * Asserts that can't access parking lot by request
     */
    @Test
    public void testDeleteParkingLot() {
        long id = 2L;
        ParkingLot parkingLot = restTemplate.getForObject(getRootUrl() + parkingPoint + id, ParkingLot.class);
        assertNotNull(parkingLot);
        restTemplate.delete(getRootUrl() + parkingPoint + id);

        try {
            parkingLot = restTemplate.getForObject(getRootUrl() + parkingPoint + id, ParkingLot.class);
        } catch (final HttpClientErrorException e) {
            assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }
    }
}
