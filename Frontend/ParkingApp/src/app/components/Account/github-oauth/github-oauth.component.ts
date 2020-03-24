import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {AuthenticationService} from '../../../services/account/auth.service';
import {GithubOauthService} from '../../../services/account/github-oauth.service';
import {SocialAuthService} from '../../../services/account/social-auth.service';


@Component({
    selector: 'app-github-oauth',
    templateUrl: './github-oauth.component.html',
    styleUrls: ['./github-oauth.component.css']
})
export class GithubOauthComponent implements OnInit {

    private code: string;

    constructor(private route: ActivatedRoute,
                private githubAuthService: GithubOauthService,
                private socialAuthService: SocialAuthService,
                private authenticationService: AuthenticationService) {
    }

    ngOnInit() {
    }
}
