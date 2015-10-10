package com.example.joe.mapletycoon;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 10/9/2015.
 */
class SeasonDay {
    String date;
    Double tmax;
    Double tmin;
}

class Season {
    int year;
    int climateMod;
    int[] dayQuality;
    List<SeasonDay> days;
}

public class WeatherMan {

    private Context _context;
    private Map<Integer, Season> _tempMap;

    public WeatherMan(Context context)
    {
        _context = context;
    }
    // http://www.ncdc.noaa.gov/cdo-web/api/v2/data?datasetid=GHCND&datatypeid=TMAX&datatypeid=TMIN&stationid=GHCND:USC00435733&startdate=1888-02-01&enddate=1888-04-20&limit=200

    public int computeScore(int year) throws XmlPullParserException, IOException {
        int score = 0;

        if(_tempMap == null)
        {
            Resources res = _context.getResources();
            XmlPullParser xpp = res.getXml(R.xml.tempdata);
            xpp.next();
            xpp.next();
            Map<Integer, Season> smap = readFeed(xpp);
            _tempMap = smap;
        }

        int gday = 0;
        int fday = 0;
        int bday = 0;

        Season curSeason = _tempMap.get(year);
        if(curSeason == null)
        {
            System.out.println("Nothing in map");
            return -1;
        }
        for(SeasonDay d : curSeason.days)
        {
            double[] ds = new double[2];
            ds[0] = d.tmax;
            ds[1] = d.tmin;

            int q = determineDay(ds);
            switch(q)
            {
                case 0:
                    bday++;
                    break;
                case 1:
                    fday++;
                    break;
                case 2:
                    gday++;
                    break;
            }
        }

        int[] dq = new int[3];
        dq[0] = bday;
        dq[1] = fday;
        dq[2] = gday;

        curSeason.dayQuality = dq;
        score = crunchDays(dq);
        curSeason.climateMod = score;

        return score;
    }

    public Map<Integer, Season> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        Map<Integer, Season> seasons = new HashMap<Integer, Season>();

        parser.require(XmlPullParser.START_TAG, null, "seasons");
        while(parser.next() != XmlPullParser.END_TAG)
        {
            String name = parser.getName();
            if(name.equals("season"))
            {
                Season s = readSeason(parser);
                seasons.put(s.year, s);
            }
        }

        return seasons;
    }

    private Season readSeason(XmlPullParser parser) throws IOException, XmlPullParserException {
        Season toRet = new Season();
        int year;
        List<SeasonDay> days = new ArrayList<SeasonDay>();
        parser.require(XmlPullParser.START_TAG, null, "season");

        year = Integer.valueOf(parser.getAttributeValue(0));
        toRet.year = year;

        while(parser.next() == XmlPullParser.START_TAG)
        {
            days.add(readDay(parser));
            parser.nextTag();
        }

        toRet.days = days;
        return toRet;
    }

    private SeasonDay readDay(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, null, "d");
        String date = parser.getAttributeValue(0);
        Double tmax = Double.valueOf(parser.getAttributeValue(1));
        Double tmin = Double.valueOf(parser.getAttributeValue(2));

        SeasonDay toRet = new SeasonDay();
        toRet.date = date;
        toRet.tmax = tmax;
        toRet.tmin = tmin;

        return toRet;
    }


    public int crunchDays(int[] days)
    {
        int score = 0;

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
            quality = 0;
        }

        return quality;
    }


}
