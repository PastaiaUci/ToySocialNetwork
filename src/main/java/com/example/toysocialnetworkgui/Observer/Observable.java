package com.example.toysocialnetworkgui.Observer;

public interface Observable{
    void notifyObservers(UpdateType type);
    void addObserver(Observer obs);
}
