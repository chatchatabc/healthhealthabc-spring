package com.chatchatabc.api.domain.service;

import com.chatchatabc.api.application.dto.user.UserDTO;
import com.chatchatabc.api.application.dto.auth.AuthRegisterRequest;

public interface UserService {

    String test(String value);

    /**
     * Register a new user
     * @param userDTO
     * @param roleName
     * @return
     */
    UserDTO register(AuthRegisterRequest userDTO, String roleName);

    /**
     * Confirm registration
     * @param emailConfirmationId
     * @return
     */
    UserDTO confirmRegistration(String emailConfirmationId);

    /**
     * Generate recovery code for user to use
     * @param email
     * @return
     */
    UserDTO forgotPassword(String email);

    /**
     * Reset password
     * @param email
     * @param password
     * @param recoveryCode
     * @return
     */
    UserDTO resetPassword(String email, String password, String recoveryCode);

    /**
     * Change password
     * @param id
     * @param oldPassword
     * @param newPassword
     * @return
     */
    void changePassword(String id, String oldPassword, String newPassword);

    /**
     * Update user profile
     * @param id
     * @param userDTO
     * @return
     */
    UserDTO updateProfile(String id, UserDTO userDTO);

    /**
     * Change email
     * @param id
     * @param newEmail
     * @return
     */
    UserDTO changeEmail(String id, String newEmail);

    /**
     * Confirm email change
     * @param emailConfirmationId
     * @return
     */
    UserDTO confirmEmailChange(String emailConfirmationId);
}
