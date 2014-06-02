package com.cwardcode.TranTracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

/**
 * A class to parse a JSON Array. Allows us to obtain vehicle IDs and add to
 * vehicle-selection dropdown.
 * 
 * @author Chris Ward
 * @version 12/25/2013
 */
public class ParseJson {
	/** GET constant. */
	public final static int GET = 1;
	/** POST constant. */
	public final static int POST = 2;

	/**
	 * Default Constructor, does nothing.
	 */
	public ParseJson() {
	}

	/**
	 * Makes call to specified url, and sets params to null.
	 * 
	 * @param url
	 *            url to make request
	 * @param method
	 *            http request method
	 * */
	public String makeServiceCall(String url, int method) {
		return this.makeServiceCall(url, method, null);
	}

	/**
	 * Overloaded makeServiceCall - calls server, returns JSON Array.
	 * 
	 * @param url
	 *            url to make request.
	 * @param method
	 *            http request method.
	 * @param params
	 *            http request params.
	 * @return response from server.
	 * */
	public String makeServiceCall(String url, int method,
			List<NameValuePair> params) {
		InputStream input = null;
		String response = null;
		try {
			// Setup http client
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpEntity httpEntity;
			HttpResponse httpResponse = null;

			// Checking http request method type
			if (method == POST) {
				HttpPost httpPost = new HttpPost(url);
				// Adding post params
				if (params != null) {
					httpPost.setEntity(new UrlEncodedFormEntity(params));
				}
				httpResponse = httpClient.execute(httpPost);
			} else if (method == GET) {
				// Appending params to url
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

		} catch (IOException e) {
			Log.e("ParseJSON", e.getMessage());
		}
		// Read from server, build into string to return.
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					input, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			response = sb.toString();
		} catch (IOException e) {
			Log.e("ParseJson", e.getMessage());
		}

		return response;
	}
}