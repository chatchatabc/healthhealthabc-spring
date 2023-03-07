package com.chatchatabc.healthhealthabc.infra.schedule.user.jobs

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender

class UserChangeEmailEventJob(
    private var emailSender: JavaMailSender,
    @Value("\${spring.mail.username}")
    private var senderEmail: String
) : Job {
    override fun execute(context: JobExecutionContext?) {
        val dataMap = context?.jobDetail?.jobDataMap
        val email = dataMap?.getString("email")
        val username = dataMap?.getString("username")
        val emailConfirmationId = dataMap?.getString("emailConfirmationId")

        // Send email
        val message = SimpleMailMessage()
        message.setTo(email)
        message.from = senderEmail
        message.subject = "HealthHealthABC Email Change Notification"
        message.text =
            "Dear $username,\n\nYou have changed your email. Confirm your email by clicking this link: http://localhost:8080/api/auth/confirm-change-email/$emailConfirmationId\n" +
                    "\nIf you did not change your email, please contact us immediately.\n\nBest regards, \nHealthHealthABC Team"
        emailSender.send(message)
    }

}