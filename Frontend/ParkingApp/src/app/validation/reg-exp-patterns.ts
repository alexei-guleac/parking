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
    /*
     * General Email Regex (RFC 5322 Official Standard)
     */

    // tslint:disable-next-line:max-line-length
    public static emailPatternStr =
        '^(?:[a-z0-9!#$%&\'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&\'*+/=?^_`{|}~-]+)*|"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$';

    // public static emailPatternView = /^(?:[a-z0-9!#$%&\'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&\'*+/=?^_`{|}~-]+)*|"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$/;
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

    public static namePatternView = /'^(?=.{3,15}$)(?![_-])(?!.*[_-]{2})[a-zA-Z_-]+(?<![_-])$'/;

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
