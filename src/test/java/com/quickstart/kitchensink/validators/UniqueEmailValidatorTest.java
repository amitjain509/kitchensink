package com.quickstart.kitchensink.validators;

import com.quickstart.kitchensink.exception.EmailAlreadyExistsException;
import com.quickstart.kitchensink.service.UserService;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UniqueEmailValidatorTest {

    @Mock
    private UserService userService;

    @Mock
    private ConstraintValidatorContext context;

    @InjectMocks
    private UniqueEmailValidator uniqueEmailValidator;

    @Test
    void isValid_ShouldReturnTrue_WhenEmailIsUnique() {
        String email = "unique@example.com";
        when(userService.isMemberExistByEmailId(email)).thenReturn(false);

        boolean result = uniqueEmailValidator.isValid(email, context);

        assertTrue(result);
        verify(userService, times(1)).isMemberExistByEmailId(email);
    }

    @Test
    void isValid_ShouldThrowException_WhenEmailAlreadyExists() {
        String email = "existing@example.com";
        when(userService.isMemberExistByEmailId(email)).thenReturn(true);

        EmailAlreadyExistsException exception = assertThrows(
                EmailAlreadyExistsException.class,
                () -> uniqueEmailValidator.isValid(email, context)
        );

        assertTrue(exception.getMessage().contains(email));
        verify(userService, times(1)).isMemberExistByEmailId(email);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenEmailIsNull() {
        boolean result = uniqueEmailValidator.isValid(null, context);
        assertTrue(result);
        verifyNoInteractions(userService);
    }
}