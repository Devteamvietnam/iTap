package com.devteam.digital.common.exception;

import com.devteam.digital.common.util.StringUtil;

public class EntityExistException extends RuntimeException {

    public EntityExistException(Class clazz, String field, String val) {
        super(EntityExistException.generateMessage(clazz.getSimpleName(), field, val));
    }

    private static String generateMessage(String entity, String field, String val) {
        return StringUtil.capitalize(entity)
                + " with " + field + " "+ val + " existed";
    }
}
