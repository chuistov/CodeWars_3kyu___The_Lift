package chuistov;



// TODO:
//  1. Get rid of movingUp.
//  2. Refactor class to record (let toString be canonical).
//  3. After replacing the class with the record, replace getters with record access method invocations (in other classes).

public class Passenger {
  private final int startingFloor;
  private final int targetFloor;
  private final boolean movingUp;

  /**
   * @throws IllegalArgumentException if starting floor
   * and the target floor are the same.
   * Passengers that are fooling about and push button of the current floor.
   * This situation cannot be handled because it's implied that
   * waiting passengers push either up or down button first
   * to be put to upList or to downList, correspondingly.
   * But input parameter doesn't contain information
   * about direction (up or down button pressed).
   */
  public Passenger(int startingFloor, int targetFloor) {
    this.startingFloor = startingFloor;
    this.targetFloor = targetFloor;
    int floorDifference = targetFloor - startingFloor;
    if(floorDifference == 0)
      throw new IllegalArgumentException("Starting floor and target floor must be different");
    this.movingUp = (floorDifference > 0);
  }

  public boolean isMovingUp() {
    return movingUp;
  }

  public int getStartingFloor() {
    return startingFloor;
  }

  public int getTargetFloor() {
    return targetFloor;
  }

  @Override
  public String toString() {
    return ( movingUp ? "Up" : "Down" ) +
        " from " + startingFloor +
        " to " + targetFloor;
  }
}
