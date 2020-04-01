'use strict';
import {Injectable} from '@angular/core';
import * as Bowser from 'bowser';
import * as Fingerprint2 from 'fingerprintjs2';


type RequestIdleCallbackHandle = any;

interface RequestIdleCallbackOptions {
    timeout: number;
}

interface RequestIdleCallbackDeadline {
    readonly didTimeout: boolean;
    timeRemaining: () => number;
}

declare global {
    interface Window {
        requestIdleCallback: (
            callback: (deadline: RequestIdleCallbackDeadline) => void,
            opts?: RequestIdleCallbackOptions
        ) => RequestIdleCallbackHandle;
        cancelIdleCallback: (handle: RequestIdleCallbackHandle) => void;
    }
}

const options = {
    excludes: {
        plugins: true,
        localStorage: true,
        adBlock: true,
        availableScreenResolution: true
    }
};

class DeviceInfo {

    platformType: string;

    browser: string;

    os: string;

    timezone: string;

    language: string;

    fingerprint: string;
}

@Injectable({
    providedIn: 'root'
})
export class DeviceInfoStorage {
    public static deviceInfo: DeviceInfo = new DeviceInfo();

    constructor() {
    }

    public static _getComponentsInfo() {
        _getOs();
        _getBrowser();
        _getDeviceType();
        _getDeviceInfo('timezone', 'language');
        _getFingerprint().then(() => {
            return true;
        });
    }
}

/*  User agent
Language
Resolution available
Color depth
Timezone
Installed plugins and their versions
Installed Fonts
Canvas
CPU */
export function _getFingerprint() {
    return new Promise((resolve, reject) => {
        async function getHash() {
            try {
                const components = await Fingerprint2.getPromise(options);
                const values = components.map(component => component.value);
                const deviceFingerprint = String(Fingerprint2.x64hash128(values.join(''), 31));
                console.log(
                    'fingerprint hash ',
                    deviceFingerprint
                );

                resolve(() => {
                    DeviceInfoStorage.deviceInfo.fingerprint = deviceFingerprint;
                    return deviceFingerprint;
                });
            } catch (e) {
                reject(e);
            }
        }

        processCallback(resolve, getHash);
    });
}

function processCallback(resolve, callback) {
    if (window.requestIdleCallback) {
        // console.log('requestIdleCallback');
        window.requestIdleCallback(async () => resolve(await callback()));
    } else {
        console.log('setTimeout');
        setTimeout(async () => resolve(await callback()), 500);
    }
}

export function _getDeviceInfo(...keys) {
    return new Promise((resolve, reject) => {
        async function getDeviceComponents() {
            try {
                const components = await Fingerprint2.getPromise(options);
                // console.log('fingerprint hash components', components);
                // console.log(navigator);

                if (keys.length) {
                    const selectedComponents = _getSelectedComponents(
                        components,
                        keys
                    );
                    DeviceInfoStorage.deviceInfo = {...DeviceInfoStorage.deviceInfo, ...selectedComponents}
                } else {
                    DeviceInfoStorage.deviceInfo = {...DeviceInfoStorage.deviceInfo, ...components}
                }

                resolve(components);
            } catch (e) {
                reject(e);
            }
        }

        processCallback(resolve, getDeviceComponents);
    });
}

function _getSelectedComponents(components: any, keys: any) {
    // console.log('_getSelectedComponents');
    // console.log('components ' + JSON.stringify(components));
    // console.log('keys ' + keys);

    const selectedComponents: any = {};
    keys.forEach(key => {
        // console.log('getSelectedComponents ' + key + ' ' + getComponentByKey(components, key));
        selectedComponents[key] = getComponentByKey(components, key);
    });

    return selectedComponents;
}

function getComponentByKey(components: any, key: any) {
    for (const k in components) {
        if (components[k].key === key) {
            return components[k].value;
        }
    }
}

export function _getBrowser() {
    const result = Bowser.getParser(window.navigator.userAgent);
    // console.log(result);
    console.log(
        'You are using ' +
        result.getBrowserName() +
        ' v' +
        result.getBrowserVersion() +
        ' on ' +
        result.getOSName()
    );

    const browser = result.getBrowserName() + ' ' + result.getBrowserVersion();
    DeviceInfoStorage.deviceInfo.browser = browser;
    return browser;
}

export function _getOs() {
    const result = Bowser.getParser(window.navigator.userAgent);
    const os = result.getOSName() + ' ' + result.getOSVersion();
    // console.log(result);
    console.log('You are using ' + os);
    DeviceInfoStorage.deviceInfo.os = os;
    return os;
}

export function _getDeviceType() {
    const result = Bowser.getParser(window.navigator.userAgent);
    const platform = result.getPlatformType();
    // console.log(result);
    console.log('You are using ' + platform);
    DeviceInfoStorage.deviceInfo.platformType = platform;
    return platform;
}
