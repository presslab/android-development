package com.presslab.android.jsonparsetest;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;


public class MainActivity extends Activity {

	private String mUrl = "http://54.225.235.96/apps,1,110/api-getissue-mob1?id=264&h=394729342.0xaf1691b1c75dcab4485fd6b4f474c195861963f752511689107f6df73e344466";
	private TextView mTextField;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		String readJson = readJson();
		mTextField = (TextView)findViewById(R.id.text_title);
		try {
			JSONObject object = (JSONObject) new JSONTokener(readJson).nextValue();
			
			JSONArray paginas = object.getJSONArray("Paginas");
			JSONObject pagina = paginas.getJSONObject(0);
			JSONArray materias = pagina.getJSONArray("materias");
			JSONObject conteudo = materias.getJSONObject(0);
			
			mTextField.setText(conteudo.getString("materia").toString());
		} catch (Exception e) {
			mTextField.setText("error parsing json");
		}
	}

	public String readJson() {

		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(mUrl);
		httpGet.addHeader("Cache-Control", "no-cache");

        try {
        	HttpResponse response = client.execute(httpGet);
        	response.addHeader("Cache-Control", "no-cache");
        	
        	StatusLine statusLine = response.getStatusLine();
        	int statusCode = statusLine.getStatusCode();

        	if (statusCode == 200) {
        		HttpEntity entity = response.getEntity();
		        InputStream content = entity.getContent();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
		        String line;
		        while ((line = reader.readLine()) != null) {
		          builder.append(line);
		        }
        	} else {
		        Log.e(MainActivity.class.toString(), "Failed to download file");
        	}

        } catch (ClientProtocolException e) {
        	e.printStackTrace();
        	return "error2";
        } catch (IOException e) {
        	return e.toString();
        }

        return builder.toString();
	}
	

}
