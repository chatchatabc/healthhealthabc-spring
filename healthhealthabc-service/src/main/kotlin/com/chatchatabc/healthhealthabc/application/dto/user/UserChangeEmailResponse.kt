package com.chatchatabc.healthhealthabc.application.dto.user

import com.chatchatabc.api.domain.dto.user.UserDTO
import com.chatchatabc.healthhealthabc.application.dto.ErrorContent

data class UserChangeEmailResponse(val user: UserDTO?, val errorContent: ErrorContent?) {
}