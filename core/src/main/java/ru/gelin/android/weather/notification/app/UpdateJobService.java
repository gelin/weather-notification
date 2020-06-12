package ru.gelin.android.weather.notification.app;

import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;

/**
 * A service which is called by the {@link JobScheduler}, starts {@link UpdateService} immediately.
 */
public class UpdateJobService extends JobService {

    @Override
    public boolean onStartJob(final JobParameters params) {
        WeatherUpdater updater = new WeatherUpdater(this);
        updater.update(false, false, new Runnable() {
            @Override
            public void run() {
                jobFinished(params, false);
            }
        });
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

}
