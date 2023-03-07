package com.chatchatabc.healthhealthabc.application.dto.user

import com.chatchatabc.healthhealthabc.application.dto.ErrorContent

data class UserProfileResponse (val user: UserDTO?, val errorContent: ErrorContent?) {
}