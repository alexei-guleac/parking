package com.isd.parking.web.websocket.arduino;

import com.isd.parking.models.enums.ParkingLotStatus;
import com.isd.parking.models.subjects.ParkingLot;
import com.isd.parking.services.implementations.ParkingLotServiceImpl;
import com.isd.parking.services.implementations.StatisticsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Optional;

import static com.isd.parking.utilities.ColorConsoleOutput.*;


/**
 * Arduino Web Socket message handler
 * Used for processing messages from Arduino board via Web Socket connection
 * Contains methods for updating database stored parking lots from Arduino board
 */
@Slf4j
@Component
public class ArduinoWebSocketHandler extends TextWebSocketHandler {

    private final ParkingLotServiceImpl parkingLotService;

    private final StatisticsServiceImpl statisticsService;

    /* security token to verify Arduino board connection */
    private final String securityToken = "4a0a8679643673d083b23f52c21f27cac2b03fa2";           //{SHA1}arduino

    @Autowired
    public ArduinoWebSocketHandler(ParkingLotServiceImpl parkingLotService,
                                   StatisticsServiceImpl statisticsService) {
        this.parkingLotService = parkingLotService;
        this.statisticsService = statisticsService;
    }

    /**
     * Invoked after WebSocket negotiation has succeeded and the WebSocket connection is opened and ready for use.
     *
     * @param session - WebSocketSession
     */
    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) {
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
     *                * Message sample:
     *                * {"mBody":"Arduino data", "id":"10", "status":"FREE", "secure_key":"4a0a8679643673d083b23f52c21f27cac2b03fa2"};
     *                * {"mBody":"Arduino data", "id":"10", "status":"OCCUPIED", "secure_key":"4a0a8679643673d083b23f52c21f27cac2b03fa2"}
     */
    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, @NotNull TextMessage message) {

        log.info(grTxt("Session Id: ") + redTxt(session.getId()) + grTxt(", message body ") + blTxt(message.toString()));
        @NotNull JSONObject arduinoMsgPayload;
        String arduinoToken;
        String lotId = null;
        String lotNumber = null;

        try {
            arduinoMsgPayload = new JSONObject(message.getPayload());
        } catch (JSONException e) {
            log.info("malformed JSON string: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        arduinoToken = arduinoMsgPayload.getString("secure_key");
        if (arduinoToken != null && arduinoToken.equals(securityToken)) {
            try {
                lotId = arduinoMsgPayload.getString("id");
            } catch (JSONException e) {
                try {
                    lotNumber = arduinoMsgPayload.getString("number");
                } catch (JSONException d) {
                    log.info("invalid Arduino message: " + d.getMessage());
                    e.printStackTrace();
                    return;
                }
            }
            String parkingLotStatus = arduinoMsgPayload.getString("status");

            Optional<ParkingLot> parkingLotOptional = Optional.empty();
            if (lotId != null) {
                parkingLotOptional = parkingLotService.findById(Long.valueOf(lotId));
            } else {
                if (lotNumber != null) {
                    parkingLotOptional = parkingLotService.findByLotNumber(Integer.valueOf(lotNumber));
                }
            }

            parkingLotOptional.ifPresent(parkingLot -> {
                if (!parkingLotStatus.equals(String.valueOf(parkingLot.getStatus()))) {
                    parkingLot.setStatus(ParkingLotStatus.valueOf(parkingLotStatus));
                    parkingLot.setUpdatedNow();

                    parkingLotService.save(parkingLot);
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
    public void afterConnectionClosed(@NotNull WebSocketSession session,
                                      @NonNull CloseStatus status) {
        log.info(grTxt("Session Id: ") + redTxt(session.getId()) + grTxt(" changed status to " + status));
    }
}
