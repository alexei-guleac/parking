import { Component, EventEmitter, HostListener, OnInit, Output, ViewChild } from '@angular/core';
import { fadeInOut } from '@app/components/animations/animations';
import { SocialUserStorageService } from '@app/services/account/social/social-user-storage.service';
import { NgxLoginWithAmazonButtonComponent } from 'ngx-login-with-amazon-button';


/**
 * Social service login/registration buttons
 */
@Component({
    selector: 'app-social-buttons',
    animations: [fadeInOut],
    templateUrl: './social-buttons.component.html',
    styleUrls: ['./social-buttons.component.scss']
})
export class SocialButtonsComponent implements OnInit {
    @Output()
    fbEvent: EventEmitter<any> = new EventEmitter();

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

    @ViewChild(NgxLoginWithAmazonButtonComponent, { static: false })
    amazonBtn: NgxLoginWithAmazonButtonComponent;

    public innerWidth: any;

    private showMore = false;

    constructor(private socialUserStorageService: SocialUserStorageService) {
    }

    /**
     * Initialize the directive/component after Angular first displays the data-bound properties
     * and sets the directive/component's input properties.
     * Called once, after the first ngOnChanges()
     */
    ngOnInit() {
        this.innerWidth = window.innerWidth;
        this.resizeAmazonBtnBackground();
    }

    /**
     * Decorator that declares a DOM event to listen for,
     * and provides a handler method to run when that resize event occurs
     */
    @HostListener('window:resize', ['$event'])
    onResize() {
        this.innerWidth = window.innerWidth;
        this.resizeAmazonBtnBackground();
    }

    /**
     * Set corresponding Amazon sign in button background depends on screen size
     */
    resizeAmazonBtnBackground() {
        const screenWidth = this.innerWidth;
        const path = '../../../../../assets/img/amazon-btn/';

        if (this.amazonBtn) {
            this.amazonBtn.height = 48;
            if (
                screenWidth < 480 ||
                (screenWidth > 992 && screenWidth < 1200)
            ) {
                this.amazonBtn.src =
                    path + 'amazon-btn-480px-less_and_992-1200px.svg';
                this.amazonBtn.width = 345;
            }
            if (screenWidth > 480 && screenWidth < 770) {
                this.amazonBtn.src = path + 'amazon-btn-480-770px.svg';
                this.amazonBtn.width = 156;
            }
            if (screenWidth > 770 && screenWidth < 992) {
                this.amazonBtn.src = path + 'amazon-btn-770-992px.svg';
                this.amazonBtn.width = 280;
            }
            if (screenWidth > 1200) {
                this.amazonBtn.src = path + 'amazon-btn-1200px_high.svg';
                this.amazonBtn.width = 150;
            }
        }
    }

    /**
     * Perform Amazon user request
     * @param event - Amazon Oauth flow event
     */
    amazonAuthorize(event: any | AccessTokenRequest | CodeRequest) {
        if (event.token_type === 'bearer') {
            amazon.Login.retrieveProfile(
                event.access_token,
                (response: any) => {
                    if (window.console && window.console.log)
                    // window.console.log(response);
                        this.socialUserStorageService.socialUser.next(response.profile);
                }
            );
        }
    }

    /**
     * Notify the parent component about Amazon login event
     */
    signInWithAmazon() {
        this.amazonEvent.emit();
    }

    /**
     * Show/hide more social buttons
     */
    private toogleShowMoreSocial() {
        this.showMore = !this.showMore;

        setTimeout(() => {
            this.resizeAmazonBtnBackground();
        }, 100);

        // hide social buttons
        setTimeout(() => {
            this.showMore = false;
            console.log(this.showMore);
        }, 60000);
    }


    /*
    * Methods below notify parent form component about social button submitting event
    * */

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
