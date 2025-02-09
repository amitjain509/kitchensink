package com.quickstart.kitchensink.validators;

import com.quickstart.kitchensink.exception.EmailAlreadyExistsException;
import com.quickstart.kitchensink.service.MemberRegistrationService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final MemberRegistrationService memberRegistrationService;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email != null && memberRegistrationService.isMemberExistByEmailId(email)) {
            throw new EmailAlreadyExistsException(email); // Throw 409 Conflict
        }
        return true;
    }
}
