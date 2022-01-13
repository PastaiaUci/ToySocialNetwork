package com.example.toysocialnetworkgui.domain.validators;

import com.example.toysocialnetworkgui.domain.Event;
import com.example.toysocialnetworkgui.domain.Friendship;

public class EventValidator implements Validator<Event>{

    @Override
    public void validate(Event entity) throws ValidationException {
        String messageError = "";
        if(entity.getDate() == null || entity.getDescriere() == null || entity.getName() == null)
            messageError += "ID error";
        if (messageError.length() > 0)
            throw new ValidationException(messageError);
    }
}
