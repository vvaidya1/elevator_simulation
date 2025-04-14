package com.kkr.elevator_simulation.strategies;

import com.kkr.elevator_simulation.Elevator;
import com.kkr.elevator_simulation.model.Request;
import com.kkr.elevator_simulation.model.RequestStatus;

import java.util.List;

public class FCFSStrategy implements SchedulingStrategy {
    @Override
    public void assignRequests(List<Request> newRequests, List<Elevator> elevators, int time) {
        for (Request request : newRequests) {
            Elevator idle = null;
            for (Elevator elevator : elevators) {
                if (!elevator.hasCapacity()) continue;

                if (elevator.isIdle()) {
                    idle = elevator;
                    break;
                }
            }

            if (idle != null) {
                request.requestStatus = RequestStatus.ASSIGNED;
                idle.assign(request);
            }
        }
    }

    @Override
    public String getName() {
        return "FCFS (First Come First Serve)";
    }
}
