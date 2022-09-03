package kalschne.diego.flutter_work_manager;

import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WorkerService extends Worker {
    private NotificationManager notification;

    public WorkerService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        //Used for notification only
        notification = new NotificationService(context).createNotificationChannel();
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            //Show a notification
            NotificationCompat.Builder notifyBuilder = NotificationService.notificationBuilder(getApplicationContext(), "Background Service", "Background Service is running");
            notification.cancelAll();
            notification.notify(Consts.NOTIFICATION_ID, notifyBuilder.build());

            //Do something

            String response = request();

            System.out.println(response);

        } catch (Exception ex) {
            //return Result.failure();

            return Result.retry();
        }

        notification.cancelAll();

        Result.failure()

        return Result.success();
    }


    private String request() throws IOException {
        URL url = new URL("https://pokeapi.co/api/v2/");

        //Make a request
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("accept", "application/json");

        // Response
        InputStream responseStream = connection.getInputStream();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(responseStream));

        StringBuffer content = new StringBuffer();

        String input;

        while ((input = in.readLine()) != null) {
            content.append(input);
        }

        //Close buffer
        in.close();

        //Close connection
        connection.disconnect();

        return content.toString();
    }
}
