package barqsoft.footballscores.widget;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;

/**
 * Created by benjaminshockley on 10/5/15.
 * RemoteViewsService for creating a widget collection of games.
 */
public class TodayWidgetRemoteViewsService extends RemoteViewsService {
    public final String LOG_TAG = TodayWidgetRemoteViewsService.class.getSimpleName();
    private static final String[] MATCH_COLUMNS = {
            DatabaseContract.scores_table._ID,
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.DATE_COL,
            DatabaseContract.scores_table.TIME_COL
    };

    // Indices for the projection.

    private static final int INDEX_MATCH_ID = 0;
    private static final int INDEX_HOME_NAME = 1;
    private static final int INDEX_AWAY_NAME = 2;
    private static final int INDEX_HOME_GOALS = 3;
    private static final int INDEX_AWAY_GOALS = 4;
    private static final int INDEX_DATE = 5;
    private static final int INDEX_TIME = 6;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor todayData = null;


            @Override
            public void onCreate() {
                // Nothing here.
            }

            @Override
            public void onDataSetChanged() {
                if (todayData != null) {
                    todayData.close();
                }

                // Get the date.
                Calendar cal = Calendar.getInstance();
                // Can use this line to alter which day to show.  Add 0 for today.
                // Mostly for debugging purposes, but could be utilized as a user
                // option in the future.
                cal.add(Calendar.DATE, 0);
                Date removeDate = cal.getTime();
                SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
                String[] args = new String[] {mFormat.format(removeDate)};

                final Long identityToken = Binder.clearCallingIdentity();
                Uri matchesForTodayUri = DatabaseContract.scores_table.buildScoreWithDate();
                todayData = getContentResolver().query(matchesForTodayUri, MATCH_COLUMNS,
                        DatabaseContract.scores_table.DATE_COL, args, null);

                Binder.restoreCallingIdentity(identityToken);

            }

            @Override
            public void onDestroy() {

                // Let's clean up the cursor.
                if (todayData != null) {
                    todayData.close();
                    todayData = null;
                }
            }

            @Override
            public int getCount() {
                return todayData == null ? 0 : todayData.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_today_list_item);

                if (position == AdapterView.INVALID_POSITION ||
                        todayData == null || !todayData.moveToPosition(position)) {
                    return null;
                }

                // If there are matches today, set the empty textview to GONE(8).
                views.setViewVisibility(R.id.widget_empty, 8);

                //Log.v(LOG_TAG,"Inside getViewAt:" + todayData.getLong(INDEX_MATCH_ID));

                String homeName = todayData.getString(INDEX_HOME_NAME);
                String homeGoals = todayData.getString(INDEX_HOME_GOALS);
                String awayName = todayData.getString(INDEX_AWAY_NAME);
                String awayGoals = todayData.getString(INDEX_AWAY_GOALS);
                String time = todayData.getString(INDEX_TIME);

                views.setTextViewText(R.id.widget_home_name, homeName);
                views.setTextViewText(R.id.widget_away_name, awayName);
                views.setTextViewText(R.id.widget_data_textview, time);

                // If the match hasn't started or the score hasn't updated yet, leave blank.
                if (homeGoals.equals("-1")) {
                    views.setTextViewText(R.id.widget_score_textview, " - ");
                } else {
                    views.setTextViewText(R.id.widget_score_textview, homeGoals + " - " + awayGoals);
                }

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_today_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (todayData.moveToPosition(position))
                    return todayData.getLong(INDEX_MATCH_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }

}
