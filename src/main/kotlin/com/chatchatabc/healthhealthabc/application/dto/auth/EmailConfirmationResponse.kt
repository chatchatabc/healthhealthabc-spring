package com.chatchatabc.healthhealthabc.application.dto.auth

import com.chatchatabc.healthhealthabc.application.dto.ErrorContent

data class EmailConfirmationResponse(var username: String?, var email: String?, var errorContent: ErrorContent?) {
}