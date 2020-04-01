import {Component, EventEmitter, HostListener, OnInit, Output, ViewChild} from '@angular/core';
import {NgxLoginWithAmazonButtonComponent} from 'ngx-login-with-amazon-button';
import {SocialUserService} from '../../../../services/account/social/social-user.service';
import {fadeInOut} from '../../../animations/animations';


@Component({
    selector: 'app-social-buttons',
    animations: [fadeInOut],
    templateUrl: './social-buttons.component.html',
    styleUrls: ['./social-buttons.component.css']
})
export class SocialButtonsComponent implements OnInit {

    @Output()
    fbEvent: EventEmitter<any> = new EventEmitter();

    private showMore = false;

    @Output()
    gEvent: EventEmitter<any> = new EventEmitter();

    @Output()
    vkEvent: EventEmitter<any> = new EventEmitter();

    @Output()
    linEvent: EventEmitter<any> = new EventEmitter();

    @Output()
    gitEvent: EventEmitter<any> = new EventEmitter();

    @Output()
    msEvent: EventEmitter<any> = new EventEmitter();

    @Output()
    amazonEvent: EventEmitter<any> = new EventEmitter();

    public innerWidth: any;

    @ViewChild
    (NgxLoginWithAmazonButtonComponent, {static: false}) amazonBtn: NgxLoginWithAmazonButtonComponent;

    constructor(private socialUserService: SocialUserService) {
    }

    ngOnInit() {
        this.innerWidth = window.innerWidth;
        this.resizeAmazonBtn();
    }

    @HostListener('window:resize', ['$event'])
    onResize(event) {
        this.innerWidth = window.innerWidth;
        this.resizeAmazonBtn();
    }

    resizeAmazonBtn() {
        const screenWidth = this.innerWidth;
        console.log('screenWidth wwwww ' + screenWidth);
        console.log('amazonBtn wwwww ' + this.amazonBtn);

        if (this.amazonBtn) {
            this.amazonBtn.height = 48;
            if (screenWidth < 480 || (screenWidth > 992 && screenWidth < 1200)) {
                this.amazonBtn.src = '../../../../../assets/img/amazon-btn/amazon-btn-480px-less_and_992-1200px.svg';
                this.amazonBtn.width = 345;
            }
            if (screenWidth > 480 && screenWidth < 770) {
                this.amazonBtn.src = '../../../../../assets/img/amazon-btn/amazon-btn-480-770px.svg';
                this.amazonBtn.width = 156;
            }
            if (screenWidth > 770 && screenWidth < 992) {
                this.amazonBtn.src = '../../../../../assets/img/amazon-btn/amazon-btn-770-992px.svg';
                this.amazonBtn.width = 280;
            }
            if (screenWidth > 1200) {
                this.amazonBtn.src = '../../../../../assets/img/amazon-btn/amazon-btn-1200px_high.svg';
                this.amazonBtn.width = 150;
            }
            /*else {
                this.amazonBtn.src = 'src/assets/img/amazon-btn-488px-less_and_992-1200px.png';
            }*/
        }
    }

    amazonAuthorize(event: any | AccessTokenRequest | CodeRequest) {
        console.log('AMAZOONN' + JSON.stringify(event));

        if (event.token_type === 'bearer') {
            amazon.Login.retrieveProfile(
                event.access_token,
                (response: any) => {
                    if (window.console && window.console.log)
                        window.console.log(response);

                    this.socialUserService.socialUser.next(response.profile);
                }
            );
        }
    }

    signInWithAmazon() {
        this.amazonEvent.emit();
    }

    // Show/hide more social buttons
    private toogleShowMoreSocial() {

        this.showMore = !this.showMore;
        setTimeout(() => {
            this.resizeAmazonBtn();
        }, 100);

        setTimeout(() => {
            this.showMore = false;
            console.log(this.showMore);
        }, 60000);
    }

    private signInWithFB(): void {
        this.fbEvent.emit();
    }

    private signInWithG() {
        this.gEvent.emit();
    }

    private signInWithVK(): void {
        this.vkEvent.emit();
    }

    private signInWithLin() {
        this.linEvent.emit();
    }

    private signInWithGit() {
        this.gitEvent.emit();
    }

    private signInWithMs() {
        this.msEvent.emit();
    }
}
