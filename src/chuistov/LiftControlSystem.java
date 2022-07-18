package chuistov;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LiftControlSystem {
  final Lift lift;
  final int maxFloor;
  final List<Passenger> passengers,
                  upList,   // passengers going up
                  downList; // passengers going down

  /**
   * Constucting the main object which controls over lift and passengers
   */
  public LiftControlSystem(Lift lift, List<Passenger> passengers, int maxFloor) {
    this.maxFloor = maxFloor;
    this.lift = lift;
    this.passengers = passengers;
    upList = new ArrayList<>();
    downList = new ArrayList<>();
    for(var passenger : passengers)
      if(passenger.targetFloor() > passenger.startingFloor()) upList.add(passenger);
      else downList.add(passenger);
//    Collections.reverse(downList); // reversing this list by the first member (startingFloor)
    System.out.println("up list: " + upList);
    System.out.println("down list: " + downList);
  }

  /**
   * Main loop from starting point and returning the lift to its starting point
   */
  public void deliverPassengers() {
    // Moving the lift up and down till all the passengers are delivered
    while (!upList.isEmpty() || !downList.isEmpty())
      moveLiftBetweenEndPoints();

    lift.move(0); // getting the lift to its default waiting point
  }

  /**
   * An iteration when the lift travels all way up or all way down,
   * gathering and releasing passengers.
   * @throws IllegalStateException if the lift is not empty
   * when starting its way up or down.
   */
  public void moveLiftBetweenEndPoints() {
    if(!lift.isEmpty())
      throw new IllegalStateException("The lift must be empty before " +
                                      "starting from the lowest or highest point");
    System.out.printf("Lift will deliver passengers waiting to go %s.%n",
                      lift.isMovingUp() ? "up" : "down");

    // Initial
    lift.takePassengers(lift.isMovingUp() ? upList : downList);

    while(mustHandlePassengersInThisDirection()) {
      int nextFloor = nextFloor();
      lift.move(nextFloor);
      System.out.printf("Passengers in lift: %d.%n",
                        lift.getPassengers().size());
      lift.dropPassengers();
      System.out.printf("Passengers in lift: %d.%n",
                        lift.getPassengers().size());
      lift.takePassengers(lift.isMovingUp() ? upList : downList);
      System.out.printf("Passengers waiting: up - %d, down - %d.%n",
                        upList.size(), downList.size());
    }

    // If direction is up, the lift will move
    // to the highest floor with a passenger waiting to go down.
    if(lift.isMovingUp() && !downList.isEmpty())
        lift.move(nextStartingFloor("down"));

    // If direction is down, the lift will move
    // to the lowest floor with a passenger waiting to go up.
    if(!lift.isMovingUp() && !upList.isEmpty())
      lift.move(nextStartingFloor("up"));

    lift.reverseDirection();
  }

  /**
   * The method defines floor where the lift must go after
   * handling all passengers at its current way up or down.
   * @throws IllegalArgumentException if the argument
   * is neither "up" nor "down".
   */
  public int nextStartingFloor(String direction) {
    if(!direction.equals("up") && !direction.equals("down"))
      throw new IllegalArgumentException("Direction must be either up or down.");

    int result;
    if(direction.equals("up")) {
      result = maxFloor;
      for(var passenger : upList)
        if(passenger.startingFloor() < result)
          result = passenger.startingFloor();
      return result;
    }

    result = 0;
    for(var passenger : downList)
      if(passenger.startingFloor() > result)
        result = passenger.startingFloor();
    return result;
  }

  /**
   * Checking if the lift should continue its movement in current direction
   */
  public boolean mustHandlePassengersInThisDirection() {
    // If the lift is not empty it has job to do in current direction.
    if(!lift.isEmpty()) return true;

    // If lift is going up and any passengers are waiting upstairs return true
    if(lift.isMovingUp() && upList.stream()
        .mapToInt(x -> x.startingFloor())
        .anyMatch(x -> x > lift.getCurrentFloor()))
      return true;

    // if lift is going down and any passengers are waiting downstairs return true
    if(!lift.isMovingUp() && downList.stream()
        .mapToInt(x -> x.startingFloor())
        .anyMatch(x -> x < lift.getCurrentFloor()))
      return true;

    return false;
  }

  /**
   * Defining next floor to go
   */
  public int nextFloor() {
    int nextFloorToLeave = -1; // TODO Is there any way to get rid of these "-1"-s?
    int nextFloorToEnter = -1;

    // Defining the closest floor where the lift should drop passengers
    if(!lift.getPassengers().isEmpty()) {
      List<Integer> floorsToLeave = lift.getPassengers().stream()
          .mapToInt(x -> x.targetFloor())
          // Considering only those passengers whose target floor is higher
          // or lower than the current floor, depending on current lift direction.
          .filter(x -> lift.isMovingUp() ? x > lift.getCurrentFloor() : x < lift.getCurrentFloor())
          .boxed()
          .collect(Collectors.toList());
      nextFloorToLeave = lift.isMovingUp()  ? Collections.min(floorsToLeave)
                                            : Collections.max(floorsToLeave);
    }

    // Defining the closest floor where the lift should take passengers
    if(lift.isMovingUp() && !upList.isEmpty())
      nextFloorToEnter = upList.stream()
          .mapToInt(x -> x.startingFloor())
          .filter(x -> lift.isFull() ?  x > lift.getCurrentFloor() :
                                        x >= lift.getCurrentFloor()
          )
          .min()
          .orElse(-1);

    if(!lift.isMovingUp() && !downList.isEmpty())
      nextFloorToEnter = downList.stream()
          .mapToInt(x -> x.startingFloor())
          .filter(x -> lift.isFull() ?  x < lift.getCurrentFloor() :
                                        x <= lift.getCurrentFloor()
          )
          .max()
          .orElse(-1);

    if(nextFloorToEnter == -1) return nextFloorToLeave;
    if(nextFloorToLeave == -1) return nextFloorToEnter;
    return lift.isMovingUp() ?
        Math.min(nextFloorToEnter, nextFloorToLeave) :
        Math.max(nextFloorToEnter, nextFloorToLeave);
  }
}
