package com.quickstart.kitchensink.validators;

import com.quickstart.kitchensink.exception.ApplicationErrorCode;
import com.quickstart.kitchensink.exception.KitchenSinkException;
import com.quickstart.kitchensink.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserService userService;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email != null && userService.isMemberExistByEmailId(email)) {
            throw KitchenSinkException
                    .builder(ApplicationErrorCode.USER_EMAIL_EXISTS)
                    .referenceId(email)
                    .build();
        }
        return true;
    }
}
