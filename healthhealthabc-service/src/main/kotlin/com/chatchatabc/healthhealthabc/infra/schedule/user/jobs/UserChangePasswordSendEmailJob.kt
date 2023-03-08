package com.chatchatabc.healthhealthabc.infra.schedule.user.jobs

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender

class UserChangePasswordSendEmailJob(
    private var emailSender: JavaMailSender,
    @Value("\${spring.mail.username}")
    private var senderEmail: String
) : Job {
    override fun execute(p0: JobExecutionContext?) {
        val dataMap = p0?.jobDetail?.jobDataMap
        val email = dataMap?.getString("email")
        val username = dataMap?.getString("username")

        // Send email
        val message = SimpleMailMessage()
        message.setTo(email)
        message.from = senderEmail
        message.subject = "HealthHealthABC Password Change Notification"
        message.text =
            "Dear $username,\n\nYou have changed your password. If you did not change your password, please contact us immediately.\n\nBest regards, \nHealthHealthABC Team"
        emailSender.send(message)
    }
}