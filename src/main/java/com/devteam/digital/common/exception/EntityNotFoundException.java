package com.devteam.digital.common.exception;

import com.devteam.digital.common.util.StringUtil;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class clazz, String field, String val) {
        super(EntityNotFoundException.generateMessage(clazz.getSimpleName(), field, val));
    }

    private static String generateMessage(String entity, String field, String val) {
        return StringUtil.capitalize(entity)
                + " with " + field + " "+ val + " does not exist";
    }
}
