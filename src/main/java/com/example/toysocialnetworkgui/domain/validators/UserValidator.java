package com.example.toysocialnetworkgui.domain.validators;

import com.example.toysocialnetworkgui.domain.User;

import static com.example.toysocialnetworkgui.Utils.constants.ValidatorConstants.*;

public class UserValidator implements Validator<User> {

    @Override
    public void validate(User entity) throws ValidationException {
        String messageError = "";
        if (entity.getId() == null)
            messageError += "ID error";
        if (entity.getFirstName() == null || entity.getFirstName().matches(VALID_FIRST_NAME))
            messageError += "First name error";
        if (entity.getLastName() == null || entity.getLastName().matches(VALID_LAST_NAME))
            messageError += "Last name error";
        if (messageError.length() > 0)
            throw new ValidationException(messageError);
    }
}
