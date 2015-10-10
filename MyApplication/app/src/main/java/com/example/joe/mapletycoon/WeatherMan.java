package com.example.joe.mapletycoon;

import android.os.AsyncTask;
import android.util.JsonWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by user on 10/9/2015.
 */

public class WeatherMan extends AsyncTask<Integer, Void, Integer> {

    private String _endpoint = "http://www.ncdc.noaa.gov/cdo-web/api/v2/";
    private String _dataSetId = "GHCND";
    private String _stationId = "GHCND:USC00435733";
    private String _token = "FAfgCdtvBoVIrHwZdHDBNqEzrcWrHIZz";
    private HttpURLConnection _client;
    public Integer _result;

    // http://www.ncdc.noaa.gov/cdo-web/api/v2/data?datasetid=GHCND&datatypeid=TMAX&datatypeid=TMIN&stationid=GHCND:USC00435733&startdate=1888-02-01&enddate=1888-04-20&limit=200

    @Override
    protected Integer doInBackground(Integer... params) {
        int count = params.length;
        for(int i = 0; i < count; i++)
        {
            Integer toRet = getClimateScore(params[i]);
            return toRet;
        }
        return null;
    }

    public int getClimateScore(int inYear)
    {
        int score = 0;
        int year = inYear;

        String startDate = String.valueOf(inYear) + "-02-15";
        String endDate = String.valueOf(inYear) + "-05-20";

        // get temp data
        JSONObject data = getTempJson(startDate, endDate);
        if(data == null)
        {
            return -1;
        }
        // use temp data to calculate # of good/norm/poor days
        int[] results = sortDays(data);

        // use dayrray to calculate gallons of sap
        score = calculateScore(results);

        return score;
    }

    public int[] sortDays(JSONObject temps)
    {
        int dgood = 0;
        int dfair = 0;
        int dbad = 0;

        // Max should be in 0, Min in 1
        int[] days = new int[3];

        try {
            JSONArray results = temps.getJSONArray("results");
            double[] day = new double[2];
            String lastDay = "";
            String curDay;
            for(int i = 0; i < results.length(); i++)
            {
                JSONObject o = results.getJSONObject(i);
                curDay = o.getString("date");

                if(curDay.equals(lastDay))
                {
                    if(o.getString("datatype").equals("TMAX"))
                        {    day[0] = o.getInt("value"); }
                    else
                        {    day[1] = o.getInt("value"); }
                    int quality = determineDay(day);
                    switch(quality)
                    {
                        case 0: dbad++;
                            System.out.println("Bad day!");
                            break;
                        case 1: dfair++;
                            System.out.println("Fair day!");
                            break;
                        case 2: dgood++;
                            System.out.println("Good day!");
                            break;
                    }
                }
                else
                {
                    System.out.println(o.getString("date"));
                    day = new double[2];
                    if(o.getString("datatype").equals("TMAX"))
                        {    day[0] = o.getDouble("value"); }
                    else
                        {    day[1] = o.getDouble("value"); }
                }

                lastDay = curDay;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        days[0] = dbad;
        days[1] = dfair;
        days[2] = dgood;

        return days;
    }

    public int calculateScore(int[] days)
    {
        int score = 0;

        System.out.println("Good days: " + days[2]);
        System.out.println("Fair days: " + days[1]);
        System.out.println("Bad days: " + days[0]);
        score += days[1];
        score += (days[2]*2);

        return score;
    }

    public int determineDay(double[] day)
    {
        int quality = 1;

        double max = day[0];
        double min = day[1];
        max = max/10;
        min = min/10;

        if(max > 2 && min < -1)
        {
            quality = 2;
        }
        if(max < 0 || min > 0)
        {
            System.out.println("Bad day - max: " + max + " min: " + min);
            quality = 0;
        }

        return quality;
    }
    public JSONObject getTempJson(String sDate, String eDate)
    {
        JSONObject toRet = null;
        String startDate = sDate;
        String endDate = eDate;

        StringBuilder urlString = new StringBuilder();
        urlString.append(_endpoint);
        urlString.append("data?");
        urlString.append("datasetid=").append(_dataSetId);
        urlString.append("&datatypeid=").append("TMAX");
        urlString.append("&datatypeid=").append("TMIN");
        urlString.append("&stationid=").append(_stationId);
        urlString.append("&startdate=").append(startDate);
        urlString.append("&enddate=").append(endDate);
        urlString.append("&limit=500");

        try {
            URL url = new URL(urlString.toString());
            _client = (HttpURLConnection) url.openConnection();
            _client.setRequestMethod("GET");
            _client.addRequestProperty("token", _token);

            BufferedReader br = new BufferedReader(new InputStreamReader(_client.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();

            System.out.println(sb.toString());
            toRet = new JSONObject(sb.toString());
            if((toRet == null) | (sb.toString() == ""))
            {
                return toRet;
            }

            return toRet;

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            _client.disconnect();
        }

        return toRet;
    }
}
