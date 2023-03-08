package com.chatchatabc.healthhealthabc.infra.schedule.user

import com.chatchatabc.healthhealthabc.infra.schedule.user.jobs.UserChangeEmailEventJob
import com.chatchatabc.healthhealthabc.infra.schedule.user.jobs.UserChangePasswordSendEmailJob
import com.chatchatabc.healthhealthabc.infra.schedule.user.jobs.UserForgotPasswordSendEmailJob
import com.chatchatabc.healthhealthabc.infra.schedule.user.jobs.UserRegistrationSendEmailConfirmationJob
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.Scheduler
import org.quartz.TriggerBuilder
import org.springframework.stereotype.Component

@Component
class UserSchedule(var scheduler: Scheduler) {

    /**
     * Schedule a job to send email confirmation to user.
     */
    fun onRegisterSendEmailConfirmation(email: String, username: String, emailConfirmationId: String) {
        val job: JobDetail = JobBuilder.newJob(UserRegistrationSendEmailConfirmationJob::class.java)
            .withIdentity("onRegisterSendEmailConfirmation", "userGroup")
            .usingJobData("email", email)
            .usingJobData("username", username)
            .usingJobData("emailConfirmationId", emailConfirmationId)
            .build()
        val trigger = TriggerBuilder.newTrigger().startNow().build()
        scheduler.scheduleJob(job, trigger)
    }

    /**
     * Schedule a job to send email to user to reset password.
     */
    fun onForgotPasswordSendEmail(email: String, username: String, recoveryCode: String) {
        val job: JobDetail = JobBuilder.newJob(UserForgotPasswordSendEmailJob::class.java)
            .withIdentity("onForgotPasswordSendEmail", "userGroup")
            .usingJobData("email", email)
            .usingJobData("username", username)
            .usingJobData("recoveryCode", recoveryCode)
            .build()
        val trigger = TriggerBuilder.newTrigger().startNow().build()
        scheduler.scheduleJob(job, trigger)
    }

    /**
     * Schedule a job to send email to user to notify password change.
     */
    fun onChangePasswordSendEmail(email: String, username: String) {
        val job: JobDetail = JobBuilder.newJob(UserChangePasswordSendEmailJob::class.java)
            .withIdentity("onChangePasswordSendEmail", "userGroup")
            .usingJobData("email", email)
            .usingJobData("username", username)
            .build()
        val trigger = TriggerBuilder.newTrigger().startNow().build()
        scheduler.scheduleJob(job, trigger)
    }

    /**
     * Schedule a job to send email to user to notify email change.
     */
    fun onChangeEmailSendEmail(email: String, username: String, emailConfirmationId: String) {
        val job: JobDetail = JobBuilder.newJob(UserChangeEmailEventJob::class.java)
            .withIdentity("onChangeEmailSendEmail", "userGroup")
            .usingJobData("email", email)
            .usingJobData("username", username)
            .usingJobData("emailConfirmationId", emailConfirmationId)
            .build()
        val trigger = TriggerBuilder.newTrigger().startNow().build()
        scheduler.scheduleJob(job, trigger)
    }
}