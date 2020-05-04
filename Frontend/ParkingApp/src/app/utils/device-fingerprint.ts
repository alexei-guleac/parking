'use strict';
import { Injectable } from "@angular/core";
import * as Bowser from "bowser";
import * as Fingerprint2 from "fingerprintjs2";


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

/**
 * User device information
 */
class DeviceInfo {

    platformType: string;

    browser: string;

    os: string;

    timezone: string;

    language: string;

    fingerprint: string;
}

/**
 * Device information static storage
 */
@Injectable({
    providedIn: 'root'
})
export class DeviceInfoStorage {
    public static deviceInfo: DeviceInfo = new DeviceInfo();

    constructor() {
    }

    /**
     * Get device info storage
     */
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

/**
 * Get user device fingerprint
 * Includes (
 * Language
 * Resolution available
 * Color depth
 * Timezone
 * Installed plugins and their versions
 * Installed Fonts
 * Canvas
 * CPU)
 */
export function _getFingerprint() {
    return new Promise((resolve, reject) => {
        async function getHash() {
            try {
                const components = await Fingerprint2.getPromise(options);
                const values = components.map(component => component.value);
                const deviceFingerprint = String(Fingerprint2.x64hash128(values.join(''), 31));

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
        window.requestIdleCallback(async () => resolve(await callback()));
    } else {
        setTimeout(async () => resolve(await callback()), 500);
    }
}

/**
 * Get specific device information
 * @param keys - target device parameters
 */
export function _getDeviceInfo(...keys) {
    return new Promise((resolve, reject) => {
        async function getDeviceComponents() {
            try {
                const components = await Fingerprint2.getPromise(options);
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

/**
 * Get selected components device information
 * @param components - device components
 * @param keys - target device parameters
 */
function _getSelectedComponents(components: any, keys: any) {
    const selectedComponents: any = {};

    keys.forEach(key => {
        selectedComponents[key] = getComponentByKey(components, key);
    });

    return selectedComponents;
}

/**
 * Get component information by key
 * @param components - device components
 * @param key - device component key
 */
function getComponentByKey(components: any, key: any) {
    for (const k in components) {
        if (components[k].key === key) {
            return components[k].value;
        }
    }
}

/**
 * Get device browser name
 */
export function _getBrowser() {
    const result = Bowser.getParser(window.navigator.userAgent);
    const browser = result.getBrowserName() + ' ' + result.getBrowserVersion();
    DeviceInfoStorage.deviceInfo.browser = browser;

    return browser;
}

/**
 * Get device operational system name
 */
export function _getOs() {
    const result = Bowser.getParser(window.navigator.userAgent);
    const os = result.getOSName() + ' ' + result.getOSVersion();
    DeviceInfoStorage.deviceInfo.os = os;

    return os;
}

/**
 * Get device type
 */
export function _getDeviceType() {
    const result = Bowser.getParser(window.navigator.userAgent);
    const platform = result.getPlatformType();
    DeviceInfoStorage.deviceInfo.platformType = platform;

    return platform;
}
