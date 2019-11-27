package com.example.voicepaper.network;

import android.content.ContentValues;
import android.os.AsyncTask;

public class VoiceListTask extends AsyncTask<Void,Void,Void> {
    private ContentValues values;
    private AsyncCallback callback;
    private Exception exception;
    private String url;

    public VoiceListTask(ContentValues values, AsyncCallback callback, Exception exception) {
        this.url = "";
        this.values = values;
        this.callback = callback;
        this.exception = exception;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
