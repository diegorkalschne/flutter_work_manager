package kalschne.diego.flutter_work_manager;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneratedPluginRegistrant.registerWith(new FlutterEngine(this));

        workManager();
    }

    //Create and configure the WorkManager
    public void workManager() {
        try {
            //---Worker Constraints
            Constraints constraints = new Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .setRequiresCharging(false)
                    .setRequiresStorageNotLow(false)
                    .setRequiredNetworkType(NetworkType.NOT_ROAMING)
                    .build();

            //---Worker settings
            //For a single run
            OneTimeWorkRequest oneWorker = new OneTimeWorkRequest.Builder(WorkerService.class)
                    .setConstraints(constraints)
                    .build();

            //For a periodic run
            PeriodicWorkRequest periodicWorker = new PeriodicWorkRequest.Builder(WorkerService.class, 15, TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    //.setInitialDelay(4, TimeUnit.HOURS) // Delay on first run (Ex: 4 hours)
                    .build();

            //---Start WorkManager
            //For a single periodic run
            WorkManager.getInstance(getApplicationContext())
                    .enqueueUniquePeriodicWork("package_id", ExistingPeriodicWorkPolicy.REPLACE, periodicWorker);

            //Work chaining
            /*
            WorkManager.getInstance(getApplicationContext())
                    .beginWith(oneWorker)
                    .then(oneWorker)
                    .then(oneWorker)
                    .enqueue();
             */

        } catch (Exception ex) {
            System.out.println("WorkManager Failed " + ex.getMessage());
        }
    }
}
