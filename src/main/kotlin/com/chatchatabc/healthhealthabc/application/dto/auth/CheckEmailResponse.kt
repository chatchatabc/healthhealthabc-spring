package com.chatchatabc.healthhealthabc.application.dto.auth

import com.chatchatabc.healthhealthabc.application.dto.ErrorContent

data class CheckEmailResponse(val taken: Boolean, val errorContent: ErrorContent?) {
}