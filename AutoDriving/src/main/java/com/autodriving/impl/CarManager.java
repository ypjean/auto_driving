package com.autodriving.impl;

import com.autodriving.model.Boundary;
import com.autodriving.model.Car;

import java.util.List;

public interface CarManager {

    List<Car> findCarList();
    void saveCar(Car car);
    void moveForward(Car car, List<Car> carList, int i, Boundary boundary);
    void rotateLeft(Car car);
    void rotateRight(Car car);
    boolean checkForCollisions(Car car, int i);
}
