package com.chatchatabc.healthhealthabc.application.dto.auth

import com.chatchatabc.healthhealthabc.application.dto.ErrorContent

data class ResetPasswordResponse (val email : String?, val username: String?, val errorContent: ErrorContent?) {
}