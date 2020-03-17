// Remove the no JS class so that the button will show
document.documentElement.classList.remove('no-js');

const STORAGE_KEY = 'user-color-scheme';
const COLOR_MODE_KEY = '--color-mode';

const modeToggleButton = document.querySelector('.js-mode-toggle');
console.log('mdtb ' + modeToggleButton);
const modeToggleText = document.querySelector('.js-mode-toggle-text');
const modeStatusElement = document.querySelector('.js-mode-status');

/**
 * Pass in an element and its CSS Custom Property that you want the value of.
 * Optionally, you can determine what datatype you get back.
 *
 * @param {String} propKey
 * @param {HTMLElement} element=document.documentElement
 * @param {String} castAs='string'
 * @returns {*}
 */
const getCSSCustomProp = (propKey, element = document.documentElement, castAs = 'string') => {
    let response = getComputedStyle(element).getPropertyValue(propKey);

    // Tidy up the string if there's something to work with
    if (response.length) {
        response = response.replace(/\'|"/g, '').trim();
    }

    // Convert the response into a whatever type we wanted
    switch (castAs) {
        case 'number':
        case 'int':
            return parseInt(response, 10);
        case 'float':
            return parseFloat(response);
        case 'boolean':
        case 'bool':
            return response === 'true' || response === '1';
    }

    // Return the string response by default
    return response;
};

/**
 * Takes either a passed settings ('light'|'dark') or grabs that from localStorage.
 * If it can’t find the setting in either, it tries to load the CSS color mode,
 * controlled by the media query
 */
const applySetting = passedSetting => {
    console.log('test const applySetting');
    let currentSetting = passedSetting || localStorage.getItem(STORAGE_KEY);

    if (currentSetting) {
        document.documentElement.setAttribute('data-user-color-scheme', currentSetting);
        setButtonLabelAndStatus(currentSetting);
    } else {
        setButtonLabelAndStatus(getCSSCustomProp(COLOR_MODE_KEY));
    }
}

/**
 * Get’s the current setting > reverses it > stores it
 */
const toggleSetting = () => {
    console.log('test const toggleSetting');
    let currentSetting = localStorage.getItem(STORAGE_KEY);

    switch (currentSetting) {
        case null:
            currentSetting = getCSSCustomProp(COLOR_MODE_KEY) === 'dark' ? 'light' : 'dark';
            break;
        case 'light':
            currentSetting = 'dark';
            break;
        case 'dark':
            currentSetting = 'light';
            break;
    }

    localStorage.setItem(STORAGE_KEY, currentSetting);

    return currentSetting;
}

/**
 * A shared method for setting the button text label and visually hidden status element
 */
const setButtonLabelAndStatus = currentSetting => {
    modeToggleText.innerText = `Enable ${currentSetting === 'dark' ? 'light' : 'dark'} mode`;
    modeStatusElement.innerText = `Color mode is now "${currentSetting}"`;
}

/**
 * Clicking the button runs the apply setting method which grabs its parameter
 * from the toggle setting method.
 */
modeToggleButton.addEventListener('click', evt => {
    evt.preventDefault();

    applySetting(toggleSetting());
});

applySetting();
