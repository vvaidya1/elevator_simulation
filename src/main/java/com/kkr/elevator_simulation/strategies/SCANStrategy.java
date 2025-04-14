package com.kkr.elevator_simulation.strategies;

import com.kkr.elevator_simulation.Elevator;
import com.kkr.elevator_simulation.model.Direction;
import com.kkr.elevator_simulation.model.Request;
import com.kkr.elevator_simulation.model.RequestStatus;

import java.util.Comparator;
import java.util.List;

public class SCANStrategy implements SchedulingStrategy {
    @Override
    public void assignRequests(List<Request> newRequests, List<Elevator> elevators, int time) {
        for (Request request : newRequests) {
            Elevator best = null;
            int minDistance = Integer.MAX_VALUE;

            for (Elevator elevator : elevators) {
                if (!elevator.hasCapacity()) continue;

                int currentFloor = elevator.getCurrentFloor();
                int distance = Math.abs(currentFloor - request.sourceFloor);

                boolean sameDirection = (elevator.getDirection() == Direction.UP
                        && request.sourceFloor >= currentFloor
                        && request.sourceFloor < request.destinationFloor)
                        || (elevator.getDirection() == Direction.DOWN
                        && request.sourceFloor <= currentFloor
                        && request.destinationFloor < request.sourceFloor);

                if ((sameDirection || elevator.isIdle()) && distance < minDistance) {
                    best = elevator;
                    minDistance = distance;
                }
            }

            if (best == null) {
                best = elevators.stream()
                        .filter(Elevator::hasCapacity)
                        .min(Comparator.comparingInt(elevator -> Math.abs(elevator.getCurrentFloor() - request.sourceFloor)))
                        .orElse(elevators.get(randomElevator(elevators.size())));
            }

            if (best != null) {
                request.requestStatus = RequestStatus.ASSIGNED;
                best.assign(request);

                if (best.isIdle())
                    best.setDirection((request.sourceFloor > best.getCurrentFloor() || request.destinationFloor > request.sourceFloor)
                            ? Direction.UP
                            : Direction.DOWN);
            }
        }
    }

    @Override
    public String getName() {
        return "SCAN";
    }
}
