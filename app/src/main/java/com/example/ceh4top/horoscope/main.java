package com.example.ceh4top.horoscope;

import android.app.DownloadManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class main extends AppCompatActivity {

    String[] SignEng = new String[] {"Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"};

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView Description = (TextView) findViewById(R.id.description);
        requestQueue = Volley.newRequestQueue(this);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        Log.v("CEH4TOP","onCreate");
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Загруска с сайта
                final String url = "http://widgets.fabulously40.com/horoscope.json?sign=" + SignEng[position];

                Log.v("CEH4TOP", "URL " + url);
                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try
                                {
                                    JSONObject horoscope = response.getJSONObject("horoscope");
                                    String Desc = horoscope.getString("horoscope");

                                    Log.v("CEH4TOP", Desc);
                                    Description.setText("English:\n" + Desc);

                                    final String turl = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20170116T214216Z.0a7c5779ec19a3b5.392dba6533f264ffe523331fdc12e4b14ec68c80&text="+Desc+"&lang=en-ru";
                                    Log.v("CEH4TOP", "URL " + turl);

                                    final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, turl, null,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {

                                                    try
                                                    {
                                                        JSONArray text = response.getJSONArray("text");
                                                        String Desc = text.getString(0);

                                                        Log.v("CEH4TOP", Desc);
                                                        Description.append("\n\nПеревод на русский:\n" + Desc);

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }


                                                }
                                            },

                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Log.e("VOLLEY","ERROR: " + error.toString());
                                                }
                                            }
                                    );

                                    requestQueue.add(jsonObjectRequest);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("VOLLEY","ERROR: " + error.toString());
                            }
                        }
                );

                requestQueue.add(jsonObjectRequest);

                // Массив знаков задиака
                String[] choose = getResources().getStringArray(R.array.spiner_sign_rus);

                // Знак задиака
                TextView TextDate = (TextView) findViewById(R.id.sign);

                CharSequence DateText = choose[position];
                TextDate.setText(DateText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }
}
