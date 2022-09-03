package kalschne.diego.flutter_work_manager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class NotificationService {

    private Context context;
    private NotificationManager notification;

    public NotificationService(Context context) {
        this.context = context;
    }

    public NotificationManager createNotificationChannel() {
        notification = (NotificationManager)
                this.context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create a Notification Channel
            NotificationChannel channel = new NotificationChannel(Consts.CHANNEL_ID,
                    "Background Service", NotificationManager
                    .IMPORTANCE_HIGH);

            channel.setDescription("Notification Channel used by background services");

            //Create a notification channel
            notification.createNotificationChannel(channel);
        }

        return notification;
    }

    public static NotificationCompat.Builder notificationBuilder(Context context, String title, String content) {
        //Open the app
        Intent notificationIntent = new Intent(context, MainActivity.class);

        PendingIntent intent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);

        return new NotificationCompat.Builder(context, Consts.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_notification)
                .setChannelId(Consts.CHANNEL_ID)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(content))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(intent);
    }
}
