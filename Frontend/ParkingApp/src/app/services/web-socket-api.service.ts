import { Injectable } from '@angular/core';
import { api } from '@app/services/navigation/app.endpoints';
import { environment } from '@env';
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';


@Injectable({
    providedIn: 'root'
})
export class WebSocketApiService {

    webSocketEndPoint = environment.restUrl + api.wsBackConnectionEndpoint;

    topic = api.wsBackTopic;

    stompClient: any;

    appComponent: WebSocketMessageConsumer;

    constructor(appComponent: WebSocketMessageConsumer) {
        this.appComponent = appComponent;
    }

    connect() {
        // console.log('Initialize WebSocket Connection');
        const ws = new SockJS(this.webSocketEndPoint);
        this.stompClient = Stomp.over(ws);

        this.stompClient.connect({}, frame => {
            // console.log('Connected: ' + frame);
            this.stompClient.subscribe(this.topic, notification => {
                this.onMessageReceived(notification);
            });
            this.stompClient.reconnect_delay = 2000;
        }, this.errorCallBack);
    };

    disconnect() {
        if (this.stompClient !== null) {
            this.stompClient.disconnect();
        }
        // console.log('Disconnected');
    }

    // on error, schedule a reconnection attempt
    errorCallBack(error) {
        // console.log('errorCallBack -> ' + error);
        setTimeout(() => {
            this.connect();
        }, 5000);
    }

    /**
     * Send message to sever via web socket
     * @param {*} message
     */
    send(message) {
        // console.log('calling api via web socket');
        this.stompClient.send('/api/front-message', {}, JSON.stringify(message));
    }

    onMessageReceived(message) {
        // console.log('Message Received from Server :: ' + message);
        this.appComponent.handleMessage(message.body);
    }
}

export interface WebSocketMessageConsumer {
    handleMessage(message: any);
}
