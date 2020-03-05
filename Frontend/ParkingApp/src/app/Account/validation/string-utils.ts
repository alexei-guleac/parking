
type nonEmptyString = never; // Cannot be implicitly cast to

export function isNonEmptyString(str: string): str is nonEmptyString {
    return str && str.length > 0 && str != null; // Or any other logic, removing whitespace, etc.
}
