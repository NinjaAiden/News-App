package com.example.android.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aiden on 13/05/2018.
 */

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();
    private static final String RESPONSE = "response";
    private static final String RESULTS = "results";
    private static final String TITLE = "webTitle";
    private static final String DATE = "webPublicationDate";
    private static final String URL = "webUrl";
    private static final String SECTION = "sectionName";
    private static final String TAGS = "tags";
    private static final String AUTHOR = "webTitle";

    private QueryUtils() {
        //required empty constructor
    }

    private static List<News> extractFeaturesFromJson(String jsonResponse) {

        //create empty arraylist
        List<News> news = new ArrayList<>();

        try {
            //create JSON object
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);

            //access response node
            JSONObject response = baseJsonResponse.getJSONObject(RESPONSE);

            //Extract JSONArray associated with "results" key
            JSONArray results = response.getJSONArray(RESULTS);

            for (int i = 0; i < results.length(); i++) {
                //get single article at position i within list
                JSONObject currentNewsStory = results.getJSONObject(i);
                String title = currentNewsStory.getString(TITLE);
                String date = currentNewsStory.getString(DATE);
                String author = currentNewsStory.getJSONArray(TAGS).getJSONObject(0)
                        .getString(AUTHOR);
                String url = currentNewsStory.getString(URL);
                String section = currentNewsStory.getString(SECTION);

                //create new {@link News} object with title, date, section and url properties
                News newsStory = new News(title, date, url, section, author);
                news.add(newsStory);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing JSON results", e);
        }
        return news;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building URL", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //if url empty, return early
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /*milliseconds */);
            urlConnection.setConnectTimeout(15000 /*milliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //if successful (connect codee 200)
            //read input stream and parse response
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving JSON results", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        if (inputStream != null) {
            inputStream.close();
        }
        return jsonResponse;
    }

    public static List<News> fetchNewsData(String requestUrl){
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e (LOG_TAG, "Problem making HTTP request.", e);
        }

        List<News> news = extractFeaturesFromJson(jsonResponse);
        return news;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}