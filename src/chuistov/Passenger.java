package chuistov;

record Passenger (int startingFloor, int targetFloor) {

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
    if(targetFloor == startingFloor)
      throw new IllegalArgumentException("Starting floor and target floor must be different");
  }

  @Override
  public String toString() {
    return ( targetFloor > startingFloor ? "Up" : "Down" ) +
        " from " + startingFloor +
        " to " + targetFloor;
  }
}
