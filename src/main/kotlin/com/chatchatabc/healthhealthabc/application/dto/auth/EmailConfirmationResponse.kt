package com.chatchatabc.healthhealthabc.application.dto.auth

import com.chatchatabc.healthhealthabc.application.dto.ErrorContent
import com.chatchatabc.healthhealthabc.domain.model.User

data class EmailConfirmationResponse(var user: User?, var errorContent: ErrorContent?) {
}