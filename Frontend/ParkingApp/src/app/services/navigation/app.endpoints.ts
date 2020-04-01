export const app = {
    frontURL: 'http://localhost:4200'
};

export const api = {
    parking: '/parking',
    statistics: '/statistics',

    login: '/login',
    social: '/social',
    auth: '/auth',
    authSocial: '/auth' + '/social',
    gitOAuth: '/auth' + '/github_oauth',

    registration: '/register',
    regSocial: '/register' + '/social',
    recaptcha: '/validate_captcha',
    confirmReg: '/confirm_account',

    confirmReset: '/confirm_reset',
    forgotPass: '/forgot_password',
    resetPass: '/reset_password',

    reservation: '/reserve',
    cancelReservation: '/unreserve'
};

export const appRoutes = {
    main: '',
    account: 'account',
    accountGit: '/account',
    accountConfirm: 'confirm_account',
    accountLogin: '/account?action=login',
    registration: 'registration',
    login: 'login',
    logout: 'logout',
    reset: 'account_reset',
    accountConfirmReset: 'confirm_reset',
    statistics: 'statistics',
    layout: 'layout',
    notFound: '404',
    serverError: '500'
};

export const actions = {
    login: 'login',
    registration: 'registration',
    view: 'view',
    serverError: 'serverError',
    reset: 'reset'
};
