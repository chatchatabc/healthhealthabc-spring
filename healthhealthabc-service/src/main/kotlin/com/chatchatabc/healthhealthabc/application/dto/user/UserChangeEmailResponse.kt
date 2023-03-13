package com.chatchatabc.healthhealthabc.application.dto.user

import com.chatchatabc.api.application.dto.user.UserDTO
import com.chatchatabc.healthhealthabc.application.dto.ErrorContent

data class UserChangeEmailResponse(val user: UserDTO?, val errorContent: ErrorContent?) {
}