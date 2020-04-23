package com.isd.parking.controller.arduino;

import com.isd.parking.models.ParkingLot;
import com.isd.parking.models.enums.ParkingLotStatus;
import com.isd.parking.service.implementations.ParkingLotDBServiceImpl;
import com.isd.parking.service.implementations.ParkingLotLocalServiceImpl;
import com.isd.parking.service.implementations.StatisticsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
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

    // This dependency may need to record data directly to SQL database
    private final ParkingLotDBServiceImpl parkingLotDBService;

    private final ParkingLotLocalServiceImpl parkingLotLocalService;

    private final StatisticsServiceImpl statisticsService;

    /* security token to verify Arduino board connection */
    private final String securityToken = "4a0a8679643673d083b23f52c21f27cac2b03fa2";           //{SHA1}arduino

    @Autowired
    public ArduinoWebSocketHandler(ParkingLotDBServiceImpl parkingLotDBService,
                                   ParkingLotLocalServiceImpl parkingLotLocalService,
                                   StatisticsServiceImpl statisticsService) {
        this.parkingLotDBService = parkingLotDBService;
        this.parkingLotLocalService = parkingLotLocalService;
        this.statisticsService = statisticsService;
    }

    /**
     * Invoked after WebSocket negotiation has succeeded and the WebSocket connection is opened and ready for use.
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
     *
     * Message sample:
     * {"mBody":"Arduino data", "id":"1", "status":"FREE", "token":"4a0a8679643673d083b23f52c21f27cac2b03fa2"};
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {

        log.info(grTxt("Session Id: ") + redTxt(session.getId()) + grTxt(", message body ") + blTxt(message.toString()));

        JSONObject arduinoMsgPayload = new JSONObject(message.getPayload());
        String arduinoToken = new JSONObject(message.getPayload()).getString("secure_key");
        if (arduinoToken.equals(securityToken)) {
            String lotId = arduinoMsgPayload.getString("id");
            String parkingLotStatus = arduinoMsgPayload.getString("status");

            Optional<ParkingLot> parkingLotOptional = parkingLotLocalService.findById(Long.valueOf(lotId));
            parkingLotOptional.ifPresent(parkingLot -> {
                if (!parkingLotStatus.equals(String.valueOf(parkingLot.getStatus()))) {
                    parkingLot.setStatus(ParkingLotStatus.valueOf(parkingLotStatus));
                    parkingLot.setUpdatedNow();

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
    public void afterConnectionClosed(WebSocketSession session,
                                      @NonNull CloseStatus status) {
        log.info(grTxt("Session Id: ") + redTxt(session.getId()) + grTxt(" changed status to " + status));
    }
}
