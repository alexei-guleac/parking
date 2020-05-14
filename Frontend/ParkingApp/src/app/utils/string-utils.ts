/*
* Application String utilities
* */

type nonEmptyString = never; // Cannot be implicitly cast to

/**
 * Check is string is non empty
 * @param str - target string
 */
export function isNonEmptyString(str: string): str is nonEmptyString {
    return str && str.length > 0; // Or any other logic, removing whitespace, etc.
}

/**
 * Check is every string in string array is non empty
 * @param strings - target array of strings
 */
export function isNonEmptyStrings(...strings) {
    return strings.every(isNonEmptyString); // Or any other logic, removing whitespace, etc.
}

/**
 * Capitalize the first letter ща the string
 * @param str - target string field
 */
export function capitalize(str: string) {
    return str.charAt(0).toUpperCase() + str.slice(1);
}

/**
 * Check if target string contains other string
 * @param referrer - target verified string
 * @param someString - some other string
 */
export function containsString(referrer: string, someString: string) {
    return referrer.toLowerCase().includes(someString.toLowerCase());
}

/**
 * Trim given char characters
 * @param char - char for trimming
 * @param targetStr - target string
 */
export function trimChars(char, targetStr) {
    const re = new RegExp('^[' + char + ']+|[' + char + ']+$', 'g');
    return targetStr.replace(re, '');
}


