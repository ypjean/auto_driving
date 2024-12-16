package com.autodriving;

import com.autodriving.exception.ValidationException;
import com.autodriving.impl.CommandCentre;
import com.autodriving.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

@SpringBootApplication
@ComponentScan(basePackages = "com.autodriving")
public class AutoDrivingApplication {

	private static Scanner scanner = new Scanner(in);
	private static CommandCentre commandCentre;

	@Autowired
	public AutoDrivingApplication(CommandCentre commandCentre) {
		this.commandCentre = commandCentre;
	}

	public static void main(String[] args) {
		SpringApplication.run(AutoDrivingApplication.class, args);
		startSimulation();
	}
	private static void startSimulation() {
		out.println("\nWelcome to Auto Driving Car Simulation!" +
				"\nPlease enter the width and height of the simulation field in x y format:");

		while (true) {
			try {
				String inputBoundary = scanner.nextLine().strip();
				String boundary = commandCentre.setBoundary(inputBoundary);
				out.println("\nYou have created a field of " + boundary);
				break;
			} catch (NumberFormatException ex) {
				out.println("Invalid input. Please enter the width and height of the simulation field in x y format:");
			} catch (ValidationException ex) {
				out.println(ex.getMessage());
			}
		}
		while (true) {
			try {
				printOptions();
				String selection = scanner.nextLine().strip();
				executeSelection(selection);
				break;
			} catch (ValidationException ex) {
				out.println("Invalid selection. Please try again");
			}
		}
	}

	private static void executeSelection(String selection) {
		switch (selection) {
			case "1" -> addCar();
			case "2" -> runSimulation();
			default -> throw new ValidationException("Invalid selection");
		}
	}

	private static void addCar() {
		while (true) {
			try {
				out.println("\nPlease enter the name of the car:");
				String name = scanner.nextLine().strip();

				out.println("\nPlease enter initial position of car " + name + " in x y Direction format:");
				String position = scanner.nextLine().strip();

				out.println("\nPlease enter the commands for car " + name + ":");
				String commands = scanner.nextLine().strip().toUpperCase();

				commandCentre.addCar(name, position, commands);
				List<Car> carList = commandCentre.getCars();
				printListOfCars(carList);
				break;
			} catch (ValidationException ex) {
				out.println("\nInvalid input. " + ex.getMessage() + ". Please try again.");
			} catch (NumberFormatException ex) {
				out.println("\nInvalid input for position. Please enter initial position of car in x y Direction format.");
			}
		}

		while (true) {
			try {
				printOptions();
				String option = scanner.nextLine().strip();
				executeOption(option);
				break;
			} catch (ValidationException ex) {
				out.println("\n Invalid selection. Please try again");
			}
		}
	}

	private static void printListOfCars(List<Car> carList) {
		out.println("\nYour current list of cars are:");
		for (Car c : carList) {
			out.println(c.toStringWithCommands());
		}
	}

	private static void runSimulation() {
		List<Car> carList = commandCentre.getCars();
		printListOfCars(carList);

		List<Car> carListAfterSimulation = commandCentre.runCommands(carList);
		printListOfCarsAfterSimulation(carListAfterSimulation);

		while (true) {
			try {
				printOptionsAfterSimulation();
				String option = scanner.nextLine().strip();
				executeOptionAfterSimulation(option);
				break;

			} catch (ValidationException ex) {
				out.println("\n Invalid selection. Please try again");
			}
		}
	}

	private static void printListOfCarsAfterSimulation(List<Car> carList) {
		out.println("\nAfter simulation, the result is:");
		for (Car car : carList) {
			if (car.isCollided()) {
				String[] collidedCar = car.getCollidedCarNameAndStep().split(":");
				out.println("- " + car.getName() + ", collides with " + collidedCar[0] + " at " + car.coordinates() + " at step " + collidedCar[1]);
			} else {
				out.println(car);
			}
		}
	}
	private static void executeOption(String option) {
		switch (option) {
			case "1" -> addCar();
			case "2" -> runSimulation();
			default -> throw new ValidationException("Invalid selection");
		}
	}

	private static void printOptions() {
		out.println("\nPlease choose from the following options:\n" +
				"[1] Add a car to field\n" +
				"[2] Run simulation");
	}

	private static void executeOptionAfterSimulation(String option) {
		switch (option) {
			case "1" -> startSimulation();
			case "2" -> {
				out.println("\nThank you for running the simulation. Goodbye!");
				System.exit(0);
			}
			default -> throw new ValidationException("Invalid selection");
		}
	}

	private static void printOptionsAfterSimulation() {
		out.println("\nPlease choose from the following options:\n" +
				"[1] Start over\n" +
				"[2] Exit");
	}

}
