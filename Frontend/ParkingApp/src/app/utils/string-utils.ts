type nonEmptyString = never; // Cannot be implicitly cast to

export function isNonEmptyString(str: string): str is nonEmptyString {
    return str && str.length > 0; // Or any other logic, removing whitespace, etc.
}

export function isNonEmptyStrings(...strings) {
    return strings.every(isNonEmptyStr); // Or any other logic, removing whitespace, etc.
}

function isNonEmptyStr(str, index, array) {
    return str && str.length > 0;
}

export function capitalize(str: string) {
    return str.charAt(0).toUpperCase() + str.slice(1);
}

export function containsString(referrer: string, someString: string) {
    return referrer.toLowerCase().includes(someString.toLowerCase());
}
