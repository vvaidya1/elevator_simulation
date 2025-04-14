package com.kkr.elevator_simulation.strategies;

import com.kkr.elevator_simulation.Elevator;
import com.kkr.elevator_simulation.model.Request;
import com.kkr.elevator_simulation.model.RequestStatus;

import java.util.List;

public class NearestCarStrategy implements SchedulingStrategy {
    @Override
    public void assignRequests(List<Request> newRequests, List<Elevator> elevators, int time) {
        for (Request request : newRequests) {
            Elevator nearestCar = null;
            int distance = Integer.MAX_VALUE;
            for (Elevator elevator : elevators) {
                if (!elevator.hasCapacity()) continue;

                if (Math.abs(elevator.getCurrentFloor() - request.destinationFloor) < distance) {
                    nearestCar = elevator;
                    break;
                }
            }
            if (nearestCar != null) {
                request.requestStatus = RequestStatus.ASSIGNED;
                nearestCar.assign(request);
            }
        }
    }

    @Override
    public String getName() {
        return "NearestCar";
    }
}
