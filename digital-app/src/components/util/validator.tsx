export interface Validator {
    validate(value: any): void;
}

const EMAIL_VALIDATOR = /^([a-zA-Z0-9_.-])+@(([a-zA-Z0-9-])+.)+([a-zA-Z0-9]{2,4})+$/i;

export class EmptyValidator implements Validator {
    
    message: string;
    allowEmpty: boolean;

    constructor(message: string, allowEmpty: boolean = false) {
        this.message = message;
        this.allowEmpty = allowEmpty;
    }

    validate(value: any): void {
        if (value.trim().length === 0) {
            throw new Error('Field is required');
        }
    }
}

class EmailValidator implements Validator {

    validate(value: any): void {
        if(!value) return;
        if (!EMAIL_VALIDATOR.test(value)) {
            throw new Error('Invalid email address');
        }
    }
}

class ZeroAndGreaterValidator implements Validator {
    validate(val: any): void {
      if (val < 0) {
        throw new Error('Expect A Number Equals Or Greater Than 0');
      }
    }
  }

export const EMPTY_VALIDATOR: Validator = new EmptyValidator('Field Cannot Be Empty');
export const ZERO_AND_GREATER_VALIDATOR: Validator = new ZeroAndGreaterValidator();
export const MAIL_VALIDATOR: Validator = new EmailValidator();