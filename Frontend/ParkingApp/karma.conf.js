// Karma configuration file, see link for more information
// https://karma-runner.github.io/1.0/config/configuration-file.html

module.exports = function (config) {
    config.set({
        basePath: '',
        include: ['./src/**/*.ts', './tests/**/*.spec.ts'],
        tsconfig: './tsconfig.json',
        frameworks: ['jasmine', '@angular-devkit/build-angular'],
        plugins: [
            require('karma-jasmine'),
            require('karma-chrome-launcher'),
            require('karma-jasmine-html-reporter'),
            require('karma-coverage-istanbul-reporter'),
            require('@angular-devkit/build-angular/plugins/karma'),
        ],
        client: {
            clearContext: false, // leave Jasmine Spec Runner output visible in browser
        },
        coverageIstanbulReporter: {
            dir: require('path').join(__dirname, './coverage/ParkingApp'),
            reports: ['html', 'lcovonly', 'text-summary'],
            fixWebpackSourcePaths: true,
        },
        reporters: ['progress', 'kjhtml'],
        port: 9876,
        colors: true,
        logLevel: config.LOG_INFO,
        autoWatch: true,
        browsers: ['Chrome'],
        singleRun: false,
        restartOnFileChange: true,

        coverageOptions: {
            threshold: {
                global: {
                    statements: 60,
                    branches: 60,
                    functions: 60,
                    lines: 60,
                    excludes: [],
                },
                file: {
                    statements: 60,
                    branches: 60,
                    functions: 60,
                    lines: 60,
                    excludes: [],
                    overrides: {},
                },
            },
        },
    });
};
