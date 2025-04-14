package com.kkr.elevator_simulation.strategies;

import com.kkr.elevator_simulation.Elevator;
import com.kkr.elevator_simulation.model.Request;

import java.util.List;

public interface SchedulingStrategy {
    void assignRequests(List<Request> newRequests, List<Elevator> elevators, int time);

    String getName();

    default int randomElevator(int size) {
        return (int) (Math.random() * size);
    }
}
