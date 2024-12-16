package com.autodriving.impl;

import com.autodriving.model.Boundary;
import com.autodriving.model.Car;
import com.autodriving.repo.CarRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class CarManagerImpl implements CarManager {

    private CarRepository carRepository;

    @Autowired
    public CarManagerImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public void moveForward(Car car, List<Car> carList, int i, Boundary boundary) {
        boolean withinBoundary = checkWithinBoundary(boundary, car);
        if (!withinBoundary) return;
        else {
            car.move();
            boolean isCollided = checkForCollisions(car, i);
            if (!isCollided) {
                carRepository.save(car);
            }
        }
    }

    public boolean checkWithinBoundary(Boundary boundary, Car car) {
        int x = car.getX();
        int y = car.getY();
        switch (car.getDirection()) {
            case N -> y++;
            case S -> y--;
            case E -> x++;
            case W -> x--;
        }
        if (x <= 0 || x >= boundary.getX() || y >= boundary.getY() || y <= 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkForCollisions(Car car, int i) {
        List<Car> carList = findCarList();
        int step;
        Optional<Car> collidedCarOptional = carList.stream()
                .filter(c -> car.getId() != c.getId())
                .filter(c -> car.getX() == c.getX() && car.getY() == c.getY()).findAny();
        if (collidedCarOptional.isPresent()) {
            if (collidedCarOptional.get().isCollided()) {
                step = Integer.parseInt(collidedCarOptional.get().getCollidedCarNameAndStep().split(":")[1]);
            } else {
                step = i + 1;
            }
            car.setCollided(true);
            car.setCollidedCarNameAndStep(collidedCarOptional.get().getName() + ":" + step);
            saveCar(car);
            return true;
        }
        return false;
    }

    @Override
    public void rotateLeft(Car car) {
        car.turnLeft();
        saveCar(car);
    }

    @Override
    public void rotateRight(Car car) {
        car.turnRight();
        saveCar(car);
    }

    @Override
    public List<Car> findCarList() {
        return carRepository.findAll();
    }

    @Override
    public void saveCar(Car car) {
        carRepository.save(car);
    }
}
