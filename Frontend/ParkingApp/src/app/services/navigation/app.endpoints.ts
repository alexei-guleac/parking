export const api = {
    parking: '/parking',
    statistics: '/statistics',
    statisticsByLot: '/lot_statistics',         // other route for security reasons

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

    profile: '/profile',
    profileUpdate: '/profile' + '/update',
    profileDelete: '/profile' + '/delete',
    users: '/users',

    reservation: '/reserve',
    cancelReservation: '/unreserve',
    connectSocial: '/social_connect',
    disconnectSocial: '/social_disconnect'
};

export const appRoutes = {
    main: '',
    statistics: 'statistics',
    layout: 'layout',

    account: 'account',
    accountGit: '/account',
    accountConfirm: 'confirm_account',
    accountLogin: '/account?action=login',
    registration: 'registration',
    login: 'login',
    logout: 'logout',
    reset: 'account_reset',
    accountConfirmReset: 'confirm_reset',
    profile: 'profile',

    notFound: 'not-found',
    serverError: 'server-error',
    accessDenied: 'access-denied'
};

export const actions = {
    login: 'login',
    registration: 'registration',
    connectSocial: 'connect',

    view: 'view',
    show: 'show',
    reset: 'reset',

    serverError: 'server-error',
    unauthorized: 'unauthorized',
    forbidden: 'forbidden'
};

export const storageKeys = {
    gitCode: 'git_oauth_code',
    action: 'action'
};
