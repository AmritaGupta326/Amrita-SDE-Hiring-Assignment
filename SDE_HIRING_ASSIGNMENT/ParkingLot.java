import java.util.*;

class Car {
    private String registrationNumber;
    private String color;

    public Car(String registrationNumber, String color) {
        this.registrationNumber = registrationNumber;
        this.color = color;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getColor() {
        return color;
    }
}

class ParkingSlot {
    private int slotNumber;
    private Car parkedCar;

    public ParkingSlot(int slotNumber) {
        this.slotNumber = slotNumber;
    }

    public boolean isOccupied() {
        return parkedCar != null;
    }

    public void parkCar(Car car) {
        parkedCar = car;
    }

    public void vacate() {
        parkedCar = null;
    }

    public Car getParkedCar() {
        return parkedCar;
    }

    public int getSlotNumber() {
        return slotNumber;
    }
}

public class ParkingLot {
    private int capacity;
    private List<ParkingSlot> slots;

    public ParkingLot(int capacity) {
        this.capacity = capacity;
        slots = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            slots.add(new ParkingSlot(i + 1));
        }
    }

    public int parkCar(Car car) {
        for (ParkingSlot slot : slots) {
            if (!slot.isOccupied()) {
                slot.parkCar(car);
                return slot.getSlotNumber();
            }
        }
        return -1; // Parking lot full
    }

    public boolean leave(int slotNumber) {
        if (slotNumber < 1 || slotNumber > capacity) {
            return false; // Invalid slot number
        }
        ParkingSlot slot = slots.get(slotNumber - 1);
        if (slot.isOccupied()) {
            slot.vacate();
            return true;
        }
        return false; // Slot already vacant
    }

    public List<Car> getCarsByColor(String color) {
        List<Car> cars = new ArrayList<>();
        for (ParkingSlot slot : slots) {
            if (slot.isOccupied() && slot.getParkedCar().getColor().equalsIgnoreCase(color)) {
                cars.add(slot.getParkedCar());
            }
        }
        return cars;
    }

    public int getSlotNumberForCar(String registrationNumber) {
        for (ParkingSlot slot : slots) {
            if (slot.isOccupied() && slot.getParkedCar().getRegistrationNumber().equalsIgnoreCase(registrationNumber)) {
                return slot.getSlotNumber();
            }
        }
        return -1; // Car not found
    }

    public List<Integer> getSlotsByColor(String color) {
        List<Integer> slotNumbers = new ArrayList<>();
        for (ParkingSlot slot : slots) {
            if (slot.isOccupied() && slot.getParkedCar().getColor().equalsIgnoreCase(color)) {
                slotNumbers.add(slot.getSlotNumber());
            }
        }
        return slotNumbers;
    }

    public void printStatus() {
        System.out.println("Slot No.\tRegistration No\tColour");
        for (ParkingSlot slot : slots) {
            if (slot.isOccupied()) {
                Car car = slot.getParkedCar();
                System.out.println(slot.getSlotNumber() + "\t\t" + car.getRegistrationNumber() + "\t" + car.getColor());
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ParkingLot parkingLot = null;

        while (true) {
            String command = scanner.nextLine();
            String[] tokens = command.split(" ");

            switch (tokens[0]) {
                case "create_parking_lot":
                    int capacity = Integer.parseInt(tokens[1]);
                    parkingLot = new ParkingLot(capacity);
                    System.out.println("Created a parking lot with " + capacity + " slots");
                    break;
                case "park":
                    if (parkingLot == null) {
                        System.out.println("Please create a parking lot first");
                        break;
                    }
                    String registrationNumber = tokens[1];
                    String color = tokens[2];
                    Car car = new Car(registrationNumber, color);
                    int slotNumber = parkingLot.parkCar(car);
                    if (slotNumber == -1) {
                        System.out.println("Sorry, parking lot is full");
                    } else {
                        System.out.println("Allocated slot number: " + slotNumber);
                    }
                    break;
                case "leave":
                    if (parkingLot == null) {
                        System.out.println("Please create a parking lot first");
                        break;
                    }
                    int slotToVacate = Integer.parseInt(tokens[1]);
                    if (parkingLot.leave(slotToVacate)) {
                        System.out.println("Slot number " + slotToVacate + " is free");
                    } else {
                        System.out.println("Slot number " + slotToVacate + " is already vacant");
                    }
                    break;
                case "status":
                    if (parkingLot == null) {
                        System.out.println("Please create a parking lot first");
                        break;
                    }
                    parkingLot.printStatus();
                    break;
                case "registration_numbers_for_cars_with_colour":
                    if (parkingLot == null) {
                        System.out.println("Please create a parking lot first");
                        break;
                    }
                    String searchColor = tokens[1];
                    List<Car> carsByColor = parkingLot.getCarsByColor(searchColor);
                    StringBuilder result = new StringBuilder();
                    for (Car c : carsByColor) {
                        result.append(c.getRegistrationNumber()).append(", ");
                    }
                    if (result.length() > 0) {
                        result.delete(result.length() - 2, result.length()); // Remove the trailing comma
                        System.out.println(result);
                    } else {
                        System.out.println("Not found");
                    }
                    break;
                case "slot_number_for_registration_number":
                    if (parkingLot == null) {
                        System.out.println("Please create a parking lot first");
                        break;
                    }
                    String regNumber = tokens[1];
                    int slotNumberForCar = parkingLot.getSlotNumberForCar(regNumber);
                    if (slotNumberForCar != -1) {
                        System.out.println(slotNumberForCar);
                    } else {
                        System.out.println("Not found");
                    }
                    break;
                case "slot_numbers_for_cars_with_colour":
                    if (parkingLot == null) {
                        System.out.println("Please create a parking lot first");
                        break;
                    }
                    String colorToSearch = tokens[1];
                    List<Integer> slotsByColor = parkingLot.getSlotsByColor(colorToSearch);
                    if (!slotsByColor.isEmpty()) {
                        for (int slot : slotsByColor) {
                            System.out.print(slot + " ");
                        }
                        System.out.println();
                    } else {
                        System.out.println("Not found");
                    }
                    break;
                case "exit":
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid command");
            }
        }
    }
}
