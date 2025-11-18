package com.example.gps29_degtinnikov;

import android.os.AsyncTask;
import android.widget.TextView;

import com.google.gson.GsonBuilder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class GetAddressByGPS extends AsyncTask<Void, Void, Void> {

    TextView textAddress;
    String coords;
    String token = "60236e8e-d380-4f5c-8e27-f8ed155cbe10";
    AddressResponse Response = null;

    public GetAddressByGPS(String coords, TextView TextAddress) {
        this.coords = coords;
        textAddress = TextAddress;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Document document = Jsoup.connect("https://geocode-maps.yandex.ru/1.x/?apikey=" + token + "&format=json&geocode=" + coords + "&results=1")
                    .ignoreContentType(true)
                    .get();
            GsonBuilder builder = new GsonBuilder();
            Response = builder.create().fromJson(document.text(), AddressResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void voids) {
        textAddress.setText(Response.response.GeoObjectCollection.featureMember.get(0).GeoObject.metaDataProperty.GeocoderMetaData.text);
    }
}
