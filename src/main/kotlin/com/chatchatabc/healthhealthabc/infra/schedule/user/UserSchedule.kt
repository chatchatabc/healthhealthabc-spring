package com.chatchatabc.healthhealthabc.infra.schedule.user

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
}