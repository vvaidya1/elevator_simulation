package com.kkr.elevator_simulation.strategies;

import com.kkr.elevator_simulation.Elevator;
import com.kkr.elevator_simulation.model.Request;
import com.kkr.elevator_simulation.model.RequestStatus;

import java.util.List;

public class LoadBalancingStrategy implements SchedulingStrategy {
    @Override
    public void assignRequests(List<Request> newRequests, List<Elevator> elevators, int time) {
        for (Request request : newRequests) {
            Elevator leastLoaded = null;
            int queueSize = Integer.MAX_VALUE;
            for (Elevator elevator : elevators) {
                if (!elevator.hasCapacity()) continue;

                if (elevator.getTotalQueueSize() < queueSize) {
                    leastLoaded = elevator;
                    break;
                }
            }
            if (leastLoaded != null) {
                request.requestStatus = RequestStatus.ASSIGNED;
                leastLoaded.assign(request);
            }
        }
    }

    @Override
    public String getName() {
        return "LoadBalancing";
    }
}
