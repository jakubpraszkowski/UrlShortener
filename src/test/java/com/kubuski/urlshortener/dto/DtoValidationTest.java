package com.kubuski.urlshortener.dto;

import com.kubuski.urlshortener.entity.Roles;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class to verify validation constraints on DTOs.
 */
public class DtoValidationTest {

    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testUrlRequestValidation() {
        UrlRequest validUrl = new UrlRequest("https://example.com");
        Set<ConstraintViolation<UrlRequest>> violations = validator.validate(validUrl);
        assertTrue(violations.isEmpty(), "Valid URL should have no violations");

        UrlRequest invalidUrl1 = new UrlRequest("");
        violations = validator.validate(invalidUrl1);
        assertFalse(violations.isEmpty(), "Empty URL should have violations");

        UrlRequest invalidUrl2 = new UrlRequest("not-a-url");
        violations = validator.validate(invalidUrl2);
        assertFalse(violations.isEmpty(), "Invalid URL format should have violations");

        UrlRequest invalidUrl3 = new UrlRequest("ftp://example.com");
        violations = validator.validate(invalidUrl3);
        assertFalse(violations.isEmpty(), "FTP URL should have violations");
    }

    @Test
    public void testUserRequestValidation() {
        UserRequest validUser = new UserRequest("user", "password", "user@example.com", Roles.USER);
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(validUser);
        assertTrue(violations.isEmpty(), "Valid user should have no violations");

        UserRequest invalidUser1 = new UserRequest("", "password", "user@example.com", Roles.USER);
        violations = validator.validate(invalidUser1);
        assertFalse(violations.isEmpty(), "Empty username should have violations");

        UserRequest invalidUser2 = new UserRequest("user", "password", "invalid-email", Roles.USER);
        violations = validator.validate(invalidUser2);
        assertFalse(violations.isEmpty(), "Invalid email should have violations");

        UserRequest invalidUser3 = new UserRequest("user", "password", "user@example.com", null);
        violations = validator.validate(invalidUser3);
        assertFalse(violations.isEmpty(), "Null role should have violations");
    }

    @Test
    public void testRegisterRequestValidation() {
        RegisterRequest validRegister = new RegisterRequest("user", "password", "user@example.com", Roles.USER);
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRegister);
        assertTrue(violations.isEmpty(), "Valid register request should have no violations");

        RegisterRequest invalidRegister1 = new RegisterRequest("", "password", "user@example.com", Roles.USER);
        violations = validator.validate(invalidRegister1);
        assertFalse(violations.isEmpty(), "Empty username should have violations");

        RegisterRequest invalidRegister2 = new RegisterRequest("user", "", "user@example.com", Roles.USER);
        violations = validator.validate(invalidRegister2);
        assertFalse(violations.isEmpty(), "Empty password should have violations");

        RegisterRequest invalidRegister3 = new RegisterRequest("user", "password", "", Roles.USER);
        violations = validator.validate(invalidRegister3);
        assertFalse(violations.isEmpty(), "Empty email should have violations");
    }

    @Test
    public void testAuthenticationRequestValidation() {
        AuthenticationRequest validAuth = new AuthenticationRequest("user", "password");
        Set<ConstraintViolation<AuthenticationRequest>> violations = validator.validate(validAuth);
        assertTrue(violations.isEmpty(), "Valid authentication request should have no violations");

        AuthenticationRequest invalidAuth1 = new AuthenticationRequest("", "password");
        violations = validator.validate(invalidAuth1);
        assertFalse(violations.isEmpty(), "Empty login should have violations");

        AuthenticationRequest invalidAuth2 = new AuthenticationRequest("user", "");
        violations = validator.validate(invalidAuth2);
        assertFalse(violations.isEmpty(), "Empty password should have violations");
    }
}
