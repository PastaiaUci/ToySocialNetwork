package com.example.toysocialnetworkgui.domain.validators;

import com.example.toysocialnetworkgui.domain.Friendship;

public class FriendshipValidator implements Validator<Friendship>{

    @Override
    public void validate(Friendship entity) throws ValidationException {
        String messageError = "";
        if(entity.getId() == null || entity.getId().getLeft() == null || entity.getId().getRight() == null)
            messageError += "ID error";
        if (messageError.length() > 0)
            throw new ValidationException(messageError);
    }
}
