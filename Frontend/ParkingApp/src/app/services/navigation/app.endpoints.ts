export const app = {
    frontURL: 'http://localhost:4200'
};

export const api = {
    parking: '/parking',
    statistics: '/statistics',

    login: '/login',
    auth: '/auth',
    social: '/auth/social',
    recaptcha: '/validate_captcha',
    // gitOAuthCallback: '/github_oauth_callback',
    gitOAuth: '/github_oauth',

    registration: '/registration',
    confirmReg: '/confirm_account',

    confirmReset: '/confirm_reset',
    forgotPass: '/forgot-password',
    resetPass: '/reset-password',

    reservation: '/reserve',
    cancelReservation: '/unreserve'
};

// tslint:disable-next-line:variable-name
export const appRoutes = {
    main: '',
    account: 'account',
    accountConfirm: 'confirm_account',
    accountLogin: '/account?action=login',
    registration: 'registration',
    login: 'login',
    logout: 'logout',
    gitOAuth: 'github_oauth_callback',
    reset: 'account_reset',
    accountConfirmReset: 'confirm_reset',
    statistics: 'statistics',
    layout: 'layout',
    notFound: '404',
    serverError: '500'
};

export const actions = {
    login: 'login',
    view: 'view',
    serverError: 'serverError',
    reset: 'reset'
};
