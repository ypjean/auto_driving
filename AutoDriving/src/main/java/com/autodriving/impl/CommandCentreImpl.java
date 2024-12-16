package com.autodriving.impl;

import com.autodriving.exception.ValidationException;
import com.autodriving.model.Boundary;
import com.autodriving.model.Car;
import com.autodriving.model.Command;
import com.autodriving.model.Direction;
import com.autodriving.repo.BoundaryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class CommandCentreImpl implements CommandCentre{

    private BoundaryRepository boundaryRepository;
    private CarManager carManager;

    @Autowired
    public CommandCentreImpl(BoundaryRepository boundaryRepository, CarManager carManager) {
        this.boundaryRepository = boundaryRepository;
        this.carManager = carManager;
    }

    @Override
    public String setBoundary(String inputBoundary) {
        String[] boundaryList = inputBoundary.split(" ");
        if (boundaryList.length != 2) {
            throw new ValidationException("Invalid input. Please enter the width and height of the simulation field in x y format:");
        }
        int x = Integer.parseInt(boundaryList[0]);
        int y = Integer.parseInt(boundaryList[1]);
        Boundary boundary = Boundary.builder()
                .x(x)
                .y(y)
                .build();

        boundaryRepository.deleteAll();
        boundaryRepository.save(boundary);
        return boundary.toString();
    }

    @Override
    public List<Car> getCars() {
        return carManager.findCarList();
    }

    @Override
    public void addCar(String name, String position, String commands) {
        List<Car> cars = getCars();
        nameIsUnique(name, cars);

        String[] list = position.split(" ");
        if (3 != list.length) {
            throw new ValidationException("Invalid input for position");
        }
        int x = Integer.parseInt(list[0]);
        int y = Integer.parseInt(list[1]);
        Direction direction = getDirection(list[2].toCharArray()[0]);

        validateCommands(commands);

        Car car = Car.builder()
                .name(name)
                .x(x)
                .y(y)
                .direction(direction)
                .commands(commands)
                .build();

        carManager.saveCar(car);
    }

    private boolean nameIsUnique(String name, List<Car> cars) {
        if (!cars.isEmpty()) {
            boolean isUnique = cars.stream().anyMatch(car -> !car.getName().equalsIgnoreCase(name));
            if (!isUnique) {
                throw new ValidationException("Name is not unique");
            }
        }
        return true;
    }

    private void validateCommands(String commands) {
        List<String> commandEnumList = Arrays.stream(Command.values()).map(Enum::toString).toList();
        List<String> commandList = Arrays.stream(commands.split("")).toList();
        for (String command : commandList) {
            if (commandEnumList.stream().noneMatch(s -> s.equalsIgnoreCase(command))) {
                throw new ValidationException("Invalid commands");
            }
        }
    }

    private Direction getDirection(char c) {
        return switch (c) {
            case 'N' -> Direction.N;
            case 'S' -> Direction.S;
            case 'E' -> Direction.E;
            case 'W' -> Direction.W;
            default -> throw new ValidationException("Invalid direction: " + c);
        };
    }

    @Override
    public List<Car> runCommands(List<Car> carList) {
        Boundary boundary = boundaryRepository.findAll().get(0);
        Optional<Integer> maxNumberOfCommandsOptional = carList.stream().map(car -> car.getCommands().split("").length).max(Comparator.naturalOrder());
        for (int i = 0; i < maxNumberOfCommandsOptional.get(); i++) {
            for (Car car: carList) {
                if (!car.isCollided()) {
                    carManager.checkForCollisions(car, i);
                    String[] commands = car.getCommands().split("");
                    processCommand(commands[i].toLowerCase(), car, getCars(), i, boundary);
                }
            }
        }
        return getCars();
    }

    private void processCommand(String command, Car car, List<Car> carList, int i, Boundary boundary) {
        switch (command) {
            case "f" -> carManager.moveForward(car, carList, i, boundary);
            case "r" -> carManager.rotateRight(car);
            case "l" -> carManager.rotateLeft(car);
            default -> throw new IllegalStateException("Unexpected value: " + command);
        }
    }
}
