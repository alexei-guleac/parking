package com.isd.parking.controller.arduino;

import com.isd.parking.models.ParkingLot;
import com.isd.parking.models.enums.ParkingLotStatus;
import com.isd.parking.service.ParkingLotDBService;
import com.isd.parking.service.ParkingLotLocalService;
import com.isd.parking.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Optional;

import static com.isd.parking.utils.ColorConsoleOutput.*;


/**
 * Arduino Web Socket message handler
 * Used for processing messages from Arduino board via Web Socket connection
 * Contains methods for updating database stored parking lots from Arduino board
 */
@Slf4j
@Component
public class ArduinoWebSocketHandler extends TextWebSocketHandler {

    private final ParkingLotDBService parkingLotDBService;

    private final ParkingLotLocalService parkingLotLocalService;

    private final StatisticsService statisticsService;

    /* security token to verify Arduino board connection */
    private final String securityToken = "4a0a8679643673d083b23f52c21f27cac2b03fa2";           //{SHA1}arduino

    @Autowired
    public ArduinoWebSocketHandler(ParkingLotDBService parkingLotDBService, ParkingLotLocalService parkingLotLocalService, StatisticsService statisticsService) {
        this.parkingLotDBService = parkingLotDBService;
        this.parkingLotLocalService = parkingLotLocalService;
        this.statisticsService = statisticsService;
    }

    /**
     * Invoked after WebSocket negotiation has succeeded and the WebSocket connection is opened and ready for use.
     *
     * @param session - WebSocketSession
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info(grTxt("A user with session Id:" + redTxt(session.getId()) + grTxt(" created a session")));
    }

    /**
     * Arduino Web Socket message handler method
     * Used for processing messages from Arduino board
     * Invoked when a new WebSocket message arrives.
     *
     * @param session - WebSocketSession
     * @param message - text message from Arduino
     *                <p>
     *                Message sample:
     *                <p>
     *                {"mBody":"Arduino data", "id":"1", "status":"FREE", "token":"4a0a8679643673d083b23f52c21f27cac2b03fa2"};
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {

        log.info(grTxt("Session Id: ") + redTxt(session.getId()) + grTxt(", message body ") + blTxt(message.toString()));
        System.out.println(message.getPayload());

        JSONObject msgObject = new JSONObject(message.getPayload());
        String arduinoToken = msgObject.getString("secure_key");

        if (arduinoToken.equals(securityToken)) {

            String lotId = msgObject.getString("id");
            String parkingLotStatus = msgObject.getString("status");

            Optional<ParkingLot> parkingLotOptional = parkingLotLocalService.findById(Long.valueOf(lotId));

            parkingLotOptional.ifPresent(parkingLot -> {

                if (!parkingLotStatus.equals(String.valueOf(parkingLot.getStatus()))) {
                    parkingLot.setStatus(ParkingLotStatus.valueOf(parkingLotStatus));
                    parkingLot.setUpdatedNow();

                    //parkingLotDBService.save(parkingLot);
                    parkingLotLocalService.save(parkingLot);
                    statisticsService.save(parkingLot);
                }
            });
        }
    }

    /**
     * Invoked after the WebSocket connection has been closed by either side, or after a transport error has occurred.
     *
     * @param session - WebSocketSession
     * @param status  - session status received
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info(grTxt("Session Id: ") + redTxt(session.getId()) + grTxt( " changed status to " + status));
    }
}
