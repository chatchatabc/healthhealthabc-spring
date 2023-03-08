package com.chatchatabc.healthhealthabc.infra.schedule.user.jobs

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender

class UserForgotPasswordSendEmailJob(
    private var emailSender: JavaMailSender,
    @Value("\${spring.mail.username}")
    private var senderEmail: String
) : Job {

    /**
     * Execute email sending job.
     */
    override fun execute(context: JobExecutionContext?) {
        val dataMap = context?.jobDetail?.jobDataMap
        val email = dataMap?.getString("email")
        val username = dataMap?.getString("username")
        val recoveryCode = dataMap?.getString("recoveryCode")

        // Send email
        val message = SimpleMailMessage()
        message.setTo(email)
        message.from = senderEmail
        message.subject = "HealthHealthABC Password Recovery"
        message.text =
            "Dear $username,\n\nYou have requested to reset your password. Here's your recovery code: $recoveryCode Please click this link to reset your password: http://localhost:8080/api/auth/reset-password\n\nBest regards, \nHealthHealthABC Team"
        emailSender.send(message)
    }
}