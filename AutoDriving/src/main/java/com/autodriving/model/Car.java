package com.autodriving.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "x_axis", nullable = false)
    private int x;

    @Column(name = "y_axis", nullable = false)
    private int y;

    @Enumerated(EnumType.STRING)
    @Column(name = "direction", nullable = false)
    private Direction direction;

    @Column(name = "commands")
    private String commands;

    @Column(name = "isCollided", nullable = false, columnDefinition = "boolean default false")
    private boolean isCollided;

    @Column(name = "collidedCarNameAndStep")
    private String collidedCarNameAndStep;

    public void move() {
        switch (direction) {
            case N -> this.y++;
            case S -> this.y--;
            case E -> this.x++;
            case W -> this.x--;
        }
    }

    public void turnLeft() {
        switch (direction) {
            case N -> this.direction = Direction.W;
            case S -> this.direction = Direction.E;
            case E -> this.direction = Direction.N;
            case W -> this.direction = Direction.S;
        }
    }

    public void turnRight() {
        switch (direction) {
            case N -> this.direction = Direction.E;
            case S -> this.direction = Direction.W;
            case E -> this.direction = Direction.S;
            case W -> this.direction = Direction.N;
        }

    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name + ", ");
        sb.append(coordinatesDirection());
        return sb.toString();
    }
    public String toStringWithCommands() {
        StringBuilder sb = new StringBuilder();
        sb.append(name + ", ");
        sb.append(coordinatesDirection() + ", ");
        sb.append(commands);
        return sb.toString();
    }

    public String coordinates() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(x);
        sb.append(", ");
        sb.append(y);
        sb.append(")");
        return sb.toString();
    }

    public String coordinatesDirection() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(x);
        sb.append(", ");
        sb.append(y);
        sb.append(") ");
        sb.append(direction.toString());
        return sb.toString();
    }
}
