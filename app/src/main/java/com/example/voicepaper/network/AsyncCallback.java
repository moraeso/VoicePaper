package com.example.voicepaper.network;

public interface AsyncCallback<T> {
    void onSuccess(T object);
    void onFailure(Exception e);
}
