package com.autodriving;

import com.autodriving.model.Car;
import com.autodriving.model.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarTest {

    Car car;

    @BeforeEach
    public void setup() {
        car = Car.builder()
                .name("A")
                .x(1)
                .y(2)
                .direction(Direction.N)
                .commands("FFRFFFFRRL")
                .build();
    }

    @Test
    public void testMoveForward() {
        car.move();
        assertEquals(3, car.getY());
    }

    @Test
    public void testTurnLeft() {
        car.turnLeft();
        assertEquals(Direction.W, car.getDirection());
    }

    @Test
    public void testTurnRight() {
        car.turnRight();
        assertEquals(Direction.E, car.getDirection());
    }
}
