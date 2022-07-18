package chuistov;

import java.util.ArrayList;
import java.util.List;

public class Lift {
  private final int capacity;
  private int currentLoad;
  private boolean isFull;
  private int currentFloor;
  private boolean movingUp;
  private final List<Passenger> passengers;
  private final List<Integer> movementHistory;

  // TODO inject passengers from client code
  public Lift(int capacity) {
    currentFloor = 0;
    this.capacity = capacity;
    currentLoad = 0;
    isFull = false;
    movingUp = true;
    passengers = new ArrayList<>();
    movementHistory = new ArrayList<>();
    movementHistory.add(0); // The lift always starts from the 0 floor.
  }

  // TODO Delete the method if it's not used from outside.
  public boolean isFull() {
    return isFull;
  }

  public boolean isEmpty() {
    return passengers.isEmpty();
  }

  // TODO Delete the method if it's not used from outside.
  public int getCurrentLoad() {
    return currentLoad;
  }

  public List<Passenger> getPassengers() {
    return passengers;
  }

  public int getCurrentFloor() {
    return currentFloor;
  }

  // TODO Delete the method if it's not used from outside.
  public int getCapacity() {
    return capacity;
  }

  public boolean isMovingUp() {
    return movingUp;
  }

  public List<Integer> getMovementHistory() {
    return movementHistory;
  }

  /**
   * @throws NullPointerException if a null reference comes.
   * @throws ArrayIndexOutOfBoundsException if the lift is full.
   */

  public void takePassenger(Passenger passenger) {
    if(passenger == null) throw new NullPointerException();
    if(isFull) throw new ArrayIndexOutOfBoundsException("The lift is full and " +
                                                  "cannot load any more passengers.");
    this.passengers.add(passenger);
    currentLoad++;
    if(currentLoad == capacity) isFull = true;
    System.out.printf("Passenger entered at %dth floor.%n", passenger.startingFloor());
  }

  /**
   * @throws NullPointerException if a null reference comes.
   * @throws ArrayIndexOutOfBoundsException if the lift is already empty.
   */
  public void dropPassenger(Passenger passenger) {
    if(passenger == null) throw new NullPointerException();
    if(isEmpty()) throw new ArrayIndexOutOfBoundsException("The lift is already empty.");
    passengers.remove(passenger);
    currentLoad--;
    isFull = false;
    System.out.printf("Passenger left at %dth floor.%n", passenger.targetFloor());
  }

  /**
   * @throws NullPointerException if a null reference comes.
   */
  public void takePassengers(List<Passenger> passengers) {
    if(passengers == null) throw new NullPointerException();
    if(isFull) return;
    List<Passenger> copy = new ArrayList<>(passengers);
    for(Passenger passenger : copy)
      if(!isFull && passenger.startingFloor() == currentFloor) {
        passengers.remove(passenger);
        takePassenger(passenger);
      }
  }

  public void dropPassengers() {
    List<Passenger> copy = new ArrayList<>(passengers);
    for(Passenger passenger : copy)
      if(passenger.targetFloor() == currentFloor) {
        dropPassenger(passenger);
      }
  }

  public void move(int nextFloor) {
    // The lift can "move without moving" after dropping last passenger
    // and changing direction to get another passenger at the same floor.
    if(nextFloor == currentFloor) return;

    currentFloor = nextFloor;
    movementHistory.add(nextFloor);
    System.out.printf("Lift moved to the %dth floor.%n", nextFloor);
  }

  public void reverseDirection() {
    movingUp = !movingUp;
  }
}
