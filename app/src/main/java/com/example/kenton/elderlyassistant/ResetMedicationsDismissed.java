package com.example.kenton.elderlyassistant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.ArrayList;

/**
 * Created by Kenton on 2016-03-28.
 */
public class ResetMedicationsDismissed extends BroadcastReceiver {

    public static String DATABASE = "database";
    ArrayList<MedicationReminders> reminders;

    public void onReceive(Context context, Intent intent) {

        DatabaseHelper db = new DatabaseHelper(context);

        reminders.addAll(db.getAllReminders());

        for (MedicationReminders reminder : reminders)
        {
            reminder.setDismissed(0);
            db.updateReminder(reminder);
        }
    }
}
