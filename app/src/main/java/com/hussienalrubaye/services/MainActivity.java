package com.hussienalrubaye.services;



import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.R.attr.id;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void buGet(View view) {

        String url="https://api.thingspeak.com/channels/365075/feeds.json?results=2";

        new MyAsyncTaskgetNews().execute(url);

    }



    // get news from server
    public class MyAsyncTaskgetNews extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            //before works
        }
        @Override
        protected String  doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                String NewsData;
                //define the url we have to connect with
                URL url = new URL(params[0]);
                //make connect with url and send request
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //waiting for 7000ms for response
                urlConnection.setConnectTimeout(7000);//set timeout to 5 seconds

                try {
                    //getting the response data
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    //convert the stream to string
                    NewsData = ConvertInputToStringNoChange(in);
                    //send to display data
                    publishProgress(NewsData);
                } finally {
                    //end connection
                    urlConnection.disconnect();
                }

            }catch (Exception ex){}
            return null;
        }
        protected void onProgressUpdate(String... progress) {

            try {

                JSONObject json= new JSONObject(progress[0]);

                JSONArray jArray = json.getJSONArray("feeds");


                for(int i=0; i<jArray.length(); i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    Toast.makeText(getApplicationContext(),"Created-at:"+  json_data.getString("created_at")+"Temperature:"+  json_data.getString("field1") + "Humidity:"+ json_data.getString("field2"),Toast.LENGTH_LONG).show();

                }






            } catch (Exception ex) {

                Toast.makeText(getApplicationContext(),"Exception Ocurred:", Toast.LENGTH_LONG).show();

            }


        }

        protected void onPostExecute(String  result2){


        }




    }

    // this method convert any stream to string
    public static String ConvertInputToStringNoChange(InputStream inputStream) {

        BufferedReader bureader=new BufferedReader( new InputStreamReader(inputStream));
        String line ;
        String linereultcal="";

        try{
            while((line=bureader.readLine())!=null) {

                linereultcal+=line;

            }
            inputStream.close();


        }catch (Exception ex){}

        return linereultcal;
    }



}
