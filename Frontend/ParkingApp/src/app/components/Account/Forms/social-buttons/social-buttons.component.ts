import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {fadeInOut} from '../../../animations/animations';


@Component({
    selector: 'app-social-buttons',
    animations: [
        fadeInOut
    ],
    templateUrl: './social-buttons.component.html',
    styleUrls: ['./social-buttons.component.css']
})
export class SocialButtonsComponent implements OnInit {

    private showMore = false;

    @Output()
    fbLogin: EventEmitter<any> = new EventEmitter();

    @Output()
    gLogin: EventEmitter<any> = new EventEmitter();

    @Output()
    gitLogin: EventEmitter<any> = new EventEmitter();

    @Output()
    msLogin: EventEmitter<any> = new EventEmitter();

    constructor() {
    }

    ngOnInit() {
    }

    // Show/hide more social buttons
    private toogleShowMoreSocial() {
        this.showMore = !this.showMore;

        setTimeout(() => {
            this.showMore = false;
            console.log(this.showMore);
        }, 60000);
    }


    private signInWithFB(): void {
        this.fbLogin.emit();
    }

    private signInWithG() {
        this.gLogin.emit();
    }

    private signInWithGit() {
        this.gitLogin.emit();
    }

    private signInWithMs() {
        this.msLogin.emit();
    }
}
