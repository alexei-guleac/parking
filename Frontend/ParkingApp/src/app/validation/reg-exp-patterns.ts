/**
 * Form regular expressions
 */
export class RegularExpressions {
    // Username pattern
    /*
    * ^(?=.{5,15}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$
       └─────┬────┘└───┬──┘└─────┬─────┘└─────┬─────┘ └───┬───┘
             │         │         │            │           no _ or . at the end
             │         │         │            allowed characters
             │         │         no __ or _. or ._ or .. inside
             │         no _ or . at the beginning
             username is 5-15 characters long
    */

    public static usernamePatternStr =
        '^(?=.{5,15}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$';

    public static usernamePatternView = /^(?=.{5,15}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$/;

    public static usernamePattern: RegExp = new RegExp(
        RegularExpressions.usernamePatternStr,
        'i'
    );

    // Email pattern
    // tslint:disable-next-line:max-line-length
    public static emailSimplePatternStr =
        '^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$';

    public static emailAngBuiltInPatternStr =
        '^(?=.{1,254}$)(?=.{1,64}@)[-!#$%&\'*+/0-9=?A-Z^_`a-z{|}~]+' +
        '(\.[-!#$%&\'*+/0-9=?A-Z^_`a-z{|}~]+)*' +
        '@[A-Za-z0-9]([A-Za-z0-9-]{0,61}[A-Za-z0-9])?' +
        '(\.[A-Za-z0-9]([A-Za-z0-9-]{0,61}[A-Za-z0-9])?)*$';

    /*
     * General Email Regex (RFC 5322 Official Standard)
     * Unicode symbols for validating cyrillic characters emails
     */
    public static emailPatternStr =
        '^([-a-z\d!#$%&\'*+/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+' +
        '(\.[-a-z\d!#$%&\'*+/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+)*' +
        '|(\x22((([\x20\x09]*\x0d\x0a)?[\x20\x09]+)?([\x01-\x08\x0b\x0c\x0e-\x1f\x7f\x21\x23-\x5b\x5d-\x7e\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]' +
        '|\\[\x01-\x09\x0b\x0c\x0d-\x7f\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))*' +
        '(([\x20\x09]*\x0d\x0a)?[\x20\x09]+)?\x22))' +
        '@(([a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]' +
        '|([a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][-a-z\d._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*' +
        '[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))\.)+' +
        '([a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|' +
        '([a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][-a-z\d._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*' +
        '[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))$';

    public static emailPatternView = /^([-a-z\d!#$%&'*+/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+(\.[-a-z\d!#$%&'*+/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+)*|(\x22((([\x20\x09]*\x0d\x0a)?[\x20\x09]+)?([\x01-\x08\x0b\x0c\x0e-\x1f\x7f\x21\x23-\x5b\x5d-\x7e\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|\\[\x01-\x09\x0b\x0c\x0d-\x7f\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))*(([\x20\x09]*\x0d\x0a)?[\x20\x09]+)?\x22))@(([a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|([a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][-a-z\d._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))\.)+([a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|([a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][-a-z\d._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))$/;

    public static emailAngBuiltInPatternView = /^(?=.{1,254}$)(?=.{1,64}@)[-!#$%&'*+/0-9=?A-Z^_`a-z{|}~]+(\.[-!#$%&'*+/0-9=?A-Z^_`a-z{|}~]+)*@[A-Za-z0-9]([A-Za-z0-9-]{0,61}[A-Za-z0-9])?(\.[A-Za-z0-9]([A-Za-z0-9-]{0,61}[A-Za-z0-9])?)*$';/;

    public static emailPattern: RegExp = new RegExp(
        RegularExpressions.emailPatternStr,
        'i'
    );

    // Name pattern
    /*
    * ^(?=.{3,15}$)(?![_-])(?!.*[_-]{2})[a-zA-Z_-]+(?<![_-])$
       └─────┬────┘└───┬──┘└─────┬─────┘└─────┬──┘ └───┬───┘
             │         │         │            │        no _ or - at the end
             │         │         │            allowed characters
             │         │         no __ or _- or -_ or -- inside
             │         no _ or - at the beginning
             name is 3-15 characters long
    */

    public static namePatternStr =
        '^(?=.{3,15}$)(?![_-])(?!.*[_-]{2})[a-zA-Z_-]+(?<![_-])$';

    public static namePatternView = /^(?=.{3,15}$)(?![_-])(?!.*[_-]{2})[a-zA-Z_-]+(?<![_-])$/;

    public static namePattern: RegExp = new RegExp(
        RegularExpressions.namePatternStr,
        'i'
    );

    // Password pattern
    /*
    Minimum 6 characters, at least one uppercase letter, one lowercase letter, one number and one special character

    * "^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!"@#$%^&'*()_+,./])[A-Za-z\d!"@#$%^&'*()_+,./]{6,}$"
    */

    public static passwordPatternStr =
        '^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!"@#$%^&\'*()_+,./])[A-Za-z\\d!"@#$%^&\'*()_+,./]{6,}$';

    public static passwordPatternView = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!"@#$%^&'*()_+,./])[A-Za-z\\d!"@#$%^&'*()_+,./]{6,}$/;

    public static passwordPattern: RegExp = new RegExp(
        RegularExpressions.passwordPatternStr,
        'i'
    );
}
