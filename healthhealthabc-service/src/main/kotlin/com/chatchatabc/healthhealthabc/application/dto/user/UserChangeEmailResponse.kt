package com.chatchatabc.healthhealthabc.application.dto.user

import com.chatchatabc.healthhealthabc.application.dto.ErrorContent
import com.chatchatabc.healthhealthabc.domain.model.User

data class UserChangeEmailResponse(val user: User?, val errorContent: ErrorContent?) {
}