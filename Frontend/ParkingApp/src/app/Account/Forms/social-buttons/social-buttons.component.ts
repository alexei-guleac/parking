import {Component, EventEmitter, OnInit, Output} from '@angular/core';

@Component({
    selector: 'app-social-buttons',
    templateUrl: './social-buttons.component.html',
    styleUrls: ['./social-buttons.component.css']
})
export class SocialButtonsComponent implements OnInit {

    showMore = false;
    @Output('fbLogin') fbLoginLocal: EventEmitter<any> = new EventEmitter();

    constructor() {
    }

    ngOnInit() {
    }

    // Show/hide more social buttons
    toogleShowMoreSocial() {
        this.showMore = !this.showMore;

        setTimeout(() => {
            this.showMore = false;
            console.log(this.showMore);
        }, 60000);
    }


    signInWithFB(): void {
        this.fbLoginLocal.emit();
    }
}
