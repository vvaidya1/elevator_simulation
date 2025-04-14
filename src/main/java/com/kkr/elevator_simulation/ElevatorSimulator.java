package com.kkr.elevator_simulation;

import com.kkr.elevator_simulation.model.Request;
import com.kkr.elevator_simulation.strategies.SchedulingStrategy;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ElevatorSimulator {
    private final List<Elevator> elevators;
    private final SchedulingStrategy strategy;
    private final List<Request> pendingRequests;
    private final List<Request> allRequests;
    private int totalFloors;
    private int time = 0;

    public ElevatorSimulator(int numElevators, int totalFloors, int capacity, SchedulingStrategy strategy, List<Request> requests) {
        this.totalFloors = totalFloors;
        this.strategy = strategy;
        this.allRequests = new ArrayList<>(requests);
        this.pendingRequests = new ArrayList<>(requests);
        this.elevators = new ArrayList<>();
        for (int i = 0; i < numElevators; i++) {
            elevators.add(new Elevator(i, capacity));
        }
    }

    public void run() throws IOException {
        List<String> log = new ArrayList<>();
        log.add("=== Simulation Summary ===");
        log.add("Strategy: " + strategy.getName());
        log.add("Time," + getElevatorHeader());
        List<Request> unassignedRequests = new ArrayList<>();
        while (!allDone()) {
            List<Request> newRequests = extractRequestsAtTime(time);
            newRequests.addAll(unassignedRequests);
            if (!newRequests.isEmpty())
                strategy.assignRequests(newRequests, elevators, time);

            moveElevators();

            unassignedRequests.removeIf(x -> !x.isPending());
            unassignedRequests.addAll(newRequests.stream().filter(Request::isPending).collect(Collectors.toList()));
            log.add(time + "," + getElevatorStates());
            time++;
        }

        calculateStats(log);
        for (String s : log) {
            System.out.println(s);
        }
        writeLog(log, "elevator_log_" + strategy.getName() + ".csv");
    }

    private void moveElevators() {
        for (Elevator elevator : elevators) {
            elevator.move(time);
        }
    }

    private List<Request> extractRequestsAtTime(int t) {
        List<Request> now = new ArrayList<>();
        Iterator<Request> it = pendingRequests.iterator();
        while (it.hasNext()) {
            Request r = it.next();
            if (r.time == t) {
                now.add(r);
                it.remove();
            }
        }
        return now;
    }

    private boolean allDone() {
        return allRequests.stream().allMatch(Request::isComplete);
    }

    private String getElevatorHeader() {
        StringBuilder sb = new StringBuilder();
        for (Elevator elevator : elevators)
            sb.append("E").append(elevator.getId()).append(",");
        return sb.substring(0, sb.length() - 1);
    }

    private String getElevatorStates() {
        StringBuilder sb = new StringBuilder();
        for (Elevator e : elevators) {
            sb.append(e.getCurrentFloor()).append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }

    private void writeLog(List<String> lines, String fileName) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            for (String line : lines) pw.println(line);
        }
    }

    private void calculateStats(List<String> log) {
        List<Integer> waitTimes = new ArrayList<>();
        List<Integer> rideTimes = new ArrayList<>();
        for (Request r : allRequests) {
            waitTimes.add(r.pickupTime - r.time);
            rideTimes.add(r.dropOffTime - r.pickupTime);
        }

        log.add("Requests: " + allRequests.size());
        log.add(String.format("Wait Time - Min: %d, Max: %d, Avg: %.2f",
                Collections.min(waitTimes), Collections.max(waitTimes), avg(waitTimes)));
        log.add(String.format("Ride Time - Min: %d, Max: %d, Avg: %.2f",
                Collections.min(rideTimes), Collections.max(rideTimes), avg(rideTimes)));
        log.add("----------------------------------------------------------------------");
    }

    private double avg(List<Integer> list) {
        return list.stream().mapToInt(i -> i).average().orElse(0);
    }
}
