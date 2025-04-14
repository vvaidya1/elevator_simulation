package com.kkr.elevator_simulation;

import com.kkr.elevator_simulation.model.Request;
import com.kkr.elevator_simulation.strategies.*;
import com.kkr.elevator_simulation.util.CSVReader;

import java.util.ArrayList;
import java.util.List;

public class ElevatorMain {
    public static void main(String[] args) throws Exception {
        List<Request> requests = CSVReader.readRequests("src/main/resources/requests.csv");
        List<SchedulingStrategy> strategies = List.of(
                new FCFSStrategy(),
                new NearestCarStrategy(),
                new LoadBalancingStrategy(),
                new SCANStrategy()
        );

        for (SchedulingStrategy strategy : strategies) {
            ElevatorSimulator simulator = new ElevatorSimulator(4, 10, 4, strategy, cloneRequests(requests));
            simulator.run();
        }
    }

    private static List<Request> cloneRequests(List<Request> original) {
        List<Request> copy = new ArrayList<>();
        for (Request r : original) {
            copy.add(new Request(r.time, r.id, r.sourceFloor, r.destinationFloor));
        }
        return copy;
    }
}
