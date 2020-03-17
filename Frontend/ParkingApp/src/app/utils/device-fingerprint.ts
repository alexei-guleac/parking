'use strict';
import * as Fingerprint2 from 'fingerprintjs2';

type RequestIdleCallbackHandle = any;

interface RequestIdleCallbackOptions {
    timeout: number;
}

interface RequestIdleCallbackDeadline {
    readonly didTimeout: boolean;
    timeRemaining: (() => number);
}

declare global {
    interface Window {
        requestIdleCallback: ((
            callback: ((deadline: RequestIdleCallbackDeadline) => void),
            opts?: RequestIdleCallbackOptions,
        ) => RequestIdleCallbackHandle);
        cancelIdleCallback: ((handle: RequestIdleCallbackHandle) => void);
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
            const options = {
                excludes: {
                    plugins: true,
                    localStorage: true,
                    adBlock: true,
                    screenResolution: true,
                    availableScreenResolution: true,
                    enumerateDevices: true,
                    pixelRatio: true,
                    doNotTrack: true
                }
            };

            try {
                const components = await Fingerprint2.getPromise(options);
                const values = components.map(component => component.value);
                // console.log('fingerprint hash components', components);
                console.log('fingerprint hash ', String(Fingerprint2.x64hash128(values.join(''), 31)));

                return String(Fingerprint2.x64hash128(values.join(''), 31));
            } catch (e) {
                reject(e);
            }
        }

        if (window.requestIdleCallback) {
            console.log('requestIdleCallback');
            window.requestIdleCallback(async () => resolve(await getHash()));
        } else {
            console.log('setTimeout');
            setTimeout(async () => resolve(await getHash()), 500);
        }
    });
}
