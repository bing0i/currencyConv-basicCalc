package com.example.currencyconverter;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetRatesTask {
    private Context context;

    public GetRatesTask(Context ct) {
        context = ct;
    }

    public void getJSON () {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://api.exchangeratesapi.io/latest";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        writeSDCard(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("FAILLL", "Fail to get JSON (exchangeRates.txt)");
                    }
                });

        queue.add(jsonObjectRequest);
    }

    private void writeSDCard(String s) {
        ArrayList<String> strings = new ArrayList<String>();
        strings.add(s);
        WriteSDCard writeSDCard = new WriteSDCard();
        writeSDCard.checkExternalMedia();
        writeSDCard.writeToSDFile(strings, " exchangeRates.txt", false);
    }

    public double getRate(String currency) {
        String jsonString = "";
        double result = 0.0;
        WriteSDCard readSDCard = new WriteSDCard();
        if (readSDCard.readFromSDFile(" exchangeRates.txt"))
            jsonString = WriteSDCard.strings.get(0);
        else
            return result;
        try {
            JSONObject ratesObj = new JSONObject(jsonString).getJSONObject("rates");;
            result = ratesObj.getDouble(currency);
        } catch (JSONException e) {
            Log.d("FAILLL", "Fail to parse JSON (exchangeRates.txt)");
        }
        return result;
    }
}
