package com.example.taskmanager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class PastFragment extends Fragment {

    private static final String TAG = "PastFragment";
    private LinearLayout pastTaskListLayout;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_past, container, false);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(getContext());

        // Initialize views
        pastTaskListLayout = view.findViewById(R.id.past_task_list_layout);

        // Load past tasks from the database
        loadPastTasksFromDatabase();

        return view;
    }

    private void loadPastTasksFromDatabase() {
        pastTaskListLayout.removeAllViews();

        long currentMillis = System.currentTimeMillis(); // 👈 Exact current time in milliseconds

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT title, description, datetime FROM tasks", null);

        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            long datetimeMillis = cursor.getLong(cursor.getColumnIndex("datetime")); // 👈 Stored task time

            if (datetimeMillis < currentMillis) { // 👈 If task datetime is before NOW
                // It's a past task

                LinearLayout taskLayout = new LinearLayout(getContext());
                taskLayout.setOrientation(LinearLayout.VERTICAL);
                taskLayout.setPadding(16, 16, 16, 16);
                taskLayout.setBackgroundResource(R.drawable.edit_text_background);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                int marginInDp = (int) (8 * getResources().getDisplayMetrics().density); // 8dp margin
                params.setMargins(0, marginInDp, 0, marginInDp);
                taskLayout.setLayoutParams(params);

                TextView titleView = new TextView(getContext());
                titleView.setText(title);
                titleView.setTextSize(18);
                titleView.setTextColor(getResources().getColor(R.color.purple_500, null));

                TextView descriptionView = new TextView(getContext());
                descriptionView.setText(description);
                descriptionView.setTextSize(14);
                descriptionView.setTextColor(getResources().getColor(R.color.purple_500, null));

                TextView dateTimeView = new TextView(getContext());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()); // 👈 Full date + time + seconds
                dateTimeView.setText(sdf.format(new Date(datetimeMillis)));
                dateTimeView.setTextSize(12);
                dateTimeView.setTextColor(getResources().getColor(R.color.purple_500, null));

                taskLayout.addView(titleView);
                taskLayout.addView(descriptionView);
                taskLayout.addView(dateTimeView);

                pastTaskListLayout.addView(taskLayout);
            }
        }

        cursor.close();
        db.close();
    }
}
