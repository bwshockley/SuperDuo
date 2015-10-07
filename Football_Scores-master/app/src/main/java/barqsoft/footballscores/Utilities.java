package barqsoft.footballscores;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import barqsoft.footballscores.service.myFetchService;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilities
{
    public static final int BUNDESLIGA1 = 394;
    public static final int BUNDESLIGA2 = 395;
    public static final int LIGUE1 = 396;
    public static final int LIGUE2 = 397;
    public static final int PREMIER_LEAGUE = 398;
    public static final int PRIMERA_DIVISION = 399;
    public static final int SEGUNDA_DIVISION = 400;
    public static final int SERIE_A = 401;
    public static final int PRIMERA_LIGA = 402;
    public static final int BUNDESLIGA3 = 403;
    public static final int EREDIVISIE = 404;
    public static final int CHAMPIONS_LEAGUE = 405;

    //Moved into Utilities
    public static void updateScores(Context context)
    {
        Intent service_start = new Intent(context, myFetchService.class);
        context.startService(service_start);
    }

    public static String getLeague(int league_num, Context context)
    {
        switch (league_num)
        {
            case BUNDESLIGA1 : return context.getString(R.string.bundesliga_1);
            case BUNDESLIGA2 : return context.getString(R.string.bundesliga_2);
            case LIGUE1 : return context.getString(R.string.ligue_1);
            case LIGUE2 : return context.getString(R.string.ligue_2);
            case PREMIER_LEAGUE : return context.getString(R.string.premierleague);
            case PRIMERA_DIVISION : return context.getString(R.string.primera_divison);
            case SEGUNDA_DIVISION : return context.getString(R.string.segunda_divison);
            case SERIE_A : return context.getString(R.string.serie_a);
            case PRIMERA_LIGA : return context.getString(R.string.primeira_liga);
            case BUNDESLIGA3 : return context.getString(R.string.bundesliga_3);
            case EREDIVISIE : return context.getString(R.string.eredivisie);
            case CHAMPIONS_LEAGUE : return context.getString(R.string.champions_league);
            default: return context.getString(R.string.no_league);
        }
    }
    public static String getMatchDay(int match_day,int league_num, Context context)
    {
        // Added context to reference strings instead of hardcoding here.
        if(league_num == CHAMPIONS_LEAGUE)
        {
            if (match_day <= 6)
            {
                return context.getString(R.string.group_stage_text);
            }
            else if(match_day == 7 || match_day == 8)
            {
                return context.getString(R.string.first_knockout_round);
            }
            else if(match_day == 9 || match_day == 10)
            {
                return context.getString(R.string.quarter_final);
            }
            else if(match_day == 11 || match_day == 12)
            {
                return context.getString(R.string.semi_final);
            }
            else
            {
                return context.getString(R.string.final_text);
            }
        }
        else
        {
            return context.getString(R.string.matchday_text) + String.valueOf(match_day);
        }
    }

    public static String getScores(int home_goals,int awaygoals)
    {
        if(home_goals < 0 || awaygoals < 0)
        {
            return " - ";
        }
        else
        {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    public static int getTeamCrestByTeamName (String teamname)
    {
        if (teamname==null){return R.drawable.no_icon;}
        switch (teamname)
        { //This is the set of icons that are currently in the app. Feel free to find and add more
            //as you go.
            case "Arsenal London FC" : return R.drawable.arsenal;
            case "Manchester United FC" : return R.drawable.manchester_united;
            case "Swansea City" : return R.drawable.swansea_city_afc;
            case "Leicester City" : return R.drawable.leicester_city_fc_hd_logo;
            case "Everton FC" : return R.drawable.everton_fc_logo1;
            case "West Ham United FC" : return R.drawable.west_ham;
            case "Tottenham Hotspur FC" : return R.drawable.tottenham_hotspur;
            case "West Bromwich Albion" : return R.drawable.west_bromwich_albion_hd_logo;
            case "Sunderland AFC" : return R.drawable.sunderland;
            case "Stoke City FC" : return R.drawable.stoke_city;
            default: return R.drawable.no_icon;
        }
    }

    /**
     * Returns true if the network is available or about to become available.
     *
     * @param c Context used to get the ConnectivityManager
     * @return true if the network is available
     */
    static public boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
