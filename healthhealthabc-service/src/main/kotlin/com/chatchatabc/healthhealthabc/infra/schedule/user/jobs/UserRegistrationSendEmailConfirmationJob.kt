package com.chatchatabc.healthhealthabc.infra.schedule.user.jobs

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender

class UserRegistrationSendEmailConfirmationJob(
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
        val emailConfirmationId = dataMap?.getString("emailConfirmationId")

        // Send email
        val message = SimpleMailMessage()
        message.setTo(email)
        message.from = senderEmail
        message.subject = "HealthHealthABC Registration Confirmation"
        message.text =
            "Dear $username,\n\nThank you for registering with HealthHealthABC. Confirm your email by clicking this link: http://localhost:8080/api/auth/confirm-email/$emailConfirmationId\n\nBest regards, \nHealthHealthABC Team"
        emailSender.send(message)
    }
}