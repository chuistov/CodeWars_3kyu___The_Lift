package chuistov;

import java.util.ArrayList;
import java.util.List;

public class Main {

    /**
     * Getting lift movement history from lift's log
     */
    public static int[] history(List<Integer> movementHistory) {
        int[] history = new int[movementHistory.size()];
        for(int i = 0; i < movementHistory.size(); i++)
            history[i] = movementHistory.get(i);
        return history;
    }

    /**
     * Initiating all waiting passengers
     */
    public static List<Passenger> createPassengers(final int[][] queues) {
        List<Passenger> passengers = new ArrayList<>();
        for(int currentFloor = 0; currentFloor < queues.length; currentFloor++)
            for(int nextPassenger = 0; nextPassenger < queues[currentFloor].length; nextPassenger++)
                passengers.add(new Passenger(currentFloor, queues[currentFloor][nextPassenger]));

        return passengers;
    }

    public static int maxFloor(final int[][] queues) {
        return queues.length;
    }

    /**
     * A starting point invoked by CodeWars validator
     */
    public static int[] theLift(final int[][] queues, final int capacity) {

        var lift = new Lift(capacity);
        var controlSystem = new LiftControlSystem(lift, createPassengers(queues), maxFloor(queues));

        // if no passengers are waiting, returning simple history consisting of the lift starting point
        if(controlSystem.passengers.isEmpty()) {
            return new int[]{0};
        }

        controlSystem.deliverPassengers();

        return history(lift.getMovementHistory());
    }

    /**
     * Main method to check the application and visualize lift movement
     */
    public static void main(String[] args) {

        final int[][] queues1 = {
            new int[]{1}, // G
            new int[]{2}, // 1
            new int[0], // 2
            new int[0], // 3
            new int[]{5}, // 4
            new int[0], // 5
        };
        final int[][] queues = {
            new int[]{3}, // G
            new int[]{4, 4, 4, 4, 4, 2}, // 1
            new int[0], // 2
            new int[0], // 3
            new int[]{2}, // 4
            new int[]{3, 3, 3, 3, 3, 4}, // 5
            new int[0], // 6
        };
        final int[] result = Main.theLift(queues,5);
        for (int i = 0; i < result.length; i++) System.out.println(result[i]);
    }
}
