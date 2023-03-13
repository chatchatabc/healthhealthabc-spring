package com.chatchatabc.api.domain.service;

import com.chatchatabc.api.application.dto.auth.AuthLoginRequest;
import com.chatchatabc.api.application.dto.auth.AuthRegisterRequest;
import com.chatchatabc.api.application.dto.user.UserDTO;

public interface UserService {

    String test(String value);

    /**
     * Register a new user
     */
    UserDTO register(AuthRegisterRequest userDTO, String roleName);

    /**
     * Login a user
     */
    String login(AuthLoginRequest authLoginRequest, String ipAddress);

    /**
     * Confirm registration
     */
    UserDTO confirmRegistration(String emailConfirmationId);

    /**
     * Generate recovery code for user to use
     */
    UserDTO forgotPassword(String email);

    /**
     * Reset password
     */
    UserDTO resetPassword(String email, String password, String recoveryCode);

    /**
     * Change password
     */
    void changePassword(String id, String oldPassword, String newPassword);

    /**
     * Update user profile
     */
    UserDTO updateProfile(String id, UserDTO userDTO);

    /**
     * Change email
     */
    UserDTO changeEmail(String id, String newEmail);

    /**
     * Confirm email change
     */
    UserDTO confirmEmailChange(String emailConfirmationId);
}
