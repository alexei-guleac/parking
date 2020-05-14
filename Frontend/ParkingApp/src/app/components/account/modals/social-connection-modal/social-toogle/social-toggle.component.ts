import { Component, EventEmitter, Input, Output } from '@angular/core';
import { socialProviderNames } from '@app/services/account/social/social-user-storage.service';


/**
 * Social provider toggle for modal window
 */
@Component({
    selector: 'app-social-toggle',
    templateUrl: './social-toggle.component.html',
    styleUrls: ['./social-toggle.component.scss']
})
export class SocialToggleComponent {

    @Input() socialName: string;

    @Input() socialImgSrc: string;

    @Input() socialProvider: string;

    @Output()
    socialConnectEvent: EventEmitter<any> = new EventEmitter();

    @Output()
    socialDisconnectEvent: EventEmitter<any> = new EventEmitter();

    checked;

    enabled = true;

    // prevent checkbox label double click
    clicked = false;

    constructor() {
    }

    /**
     * Method notify parent component about social toggle submitting event (connect or disconnect)
     * based on checkbox state
     * */
    private signIn(): void {

        // prevent checkbox label double click
        if (this.clicked) return;
        this.clicked = true;

        if (this.checked && this.enabled) {
            this.socialDisconnectEvent.emit(socialProviderNames[this.socialProvider]);
            return;
        }
        if (!this.checked && this.enabled) {
            this.socialConnectEvent.emit(socialProviderNames[this.socialProvider]);
            return;
        }
    }

}
