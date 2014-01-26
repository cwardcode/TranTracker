package com.cwardcode.TranTracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import android.widget.Toast;

/**
 * @author Chris Ward
 * @version 12/25/2013
 *
 * A class to parse JSON arrays
 */
public class ParseJson {

    /** InputStream from remote server. */
    static InputStream input = null;

    /** Response from Server. */
    static String response = null;

    /** GET constant. */
    public final static int GET = 1;

    /** POST constant. */
    public final static int POST = 2;

    /**
     * Default Constructor, does nothing.
     */
    public ParseJson() {}

    /**
     * Makes call to specified url, and sets method to null.
     * @param url url to make request
     * @param method http request method
     * */
    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }

    /**
     * Making service call to server.
     * @param url url to make request
     * @param method http request method
     * @param params http request params
     * */
    public String makeServiceCall(String url, int method,
                                  List<NameValuePair> params) {
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity;
            HttpResponse httpResponse = null;

            // Checking http request method type
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                // adding post params
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
                httpResponse = httpClient.execute(httpPost);
            } else if (method == GET) {
                // appending params to url
                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);

                httpResponse = httpClient.execute(httpGet);

            }
            httpEntity = httpResponse.getEntity();
            input = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
        	Toast.makeText(TranTracker.getAppContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (ClientProtocolException e) {
        	Toast.makeText(TranTracker.getAppContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
        	Toast.makeText(TranTracker.getAppContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    input, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            input.close();
            response = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error: " + e.toString());
        }

        return response;
    }
}