package com.devteam.digital.common.util;

import cn.hutool.core.util.ObjectUtil;
import com.devteam.digital.common.exception.BadRequestException;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;

public class ValidationUtil{

    public static void isNull(Object obj, String entity, String parameter , Object value){
        if(ObjectUtil.isNull(obj)){
            String msg = entity + " does not exist: "+ parameter +" is "+ value;
            throw new BadRequestException(msg);
        }
    }

    public static boolean isEmail(String email) {
        return new EmailValidator().isValid(email, null);
    }
}
