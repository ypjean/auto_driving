package com.autodriving.impl;

import com.autodriving.model.Car;

import java.util.List;

public interface CommandCentre {

    String setBoundary(String inputBoundary);
    List<Car> getCars();
    void addCar(String name, String position, String commands);
    List<Car> runCommands(List<Car> carList);
}
