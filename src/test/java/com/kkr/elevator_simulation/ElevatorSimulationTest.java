package com.kkr.elevator_simulation;

import com.kkr.elevator_simulation.model.Request;
import com.kkr.elevator_simulation.strategies.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ElevatorSimulationTest {

    @Test
    public void testSingleRequestGetsPickedAndDropped() throws IOException {
        List<Request> requests = List.of(new Request(0, "p1", 1, 5));
        ElevatorSimulator sim = new ElevatorSimulator(1, 10, 2, new FCFSStrategy(), requests);
        sim.run();

        Request r = requests.get(0);
        assertTrue(r.pickupTime >= 0, "Pickup time should be set");
        assertTrue(r.dropOffTime >= 0, "Drop-off time should be set");
        assertTrue(r.dropOffTime > r.pickupTime, "Drop-off should happen after pickup");
    }

    @Test
    public void testElevatorCapacityLimit() throws IOException {
        List<Request> requests = List.of(
                new Request(0, "p1", 1, 5),
                new Request(0, "p2", 1, 6),
                new Request(0, "p3", 1, 7)
        );

        ElevatorSimulator sim = new ElevatorSimulator(1, 10, 2, new FCFSStrategy(), requests);
        sim.run();

        int picked = 0;
        for (Request r : requests) {
            if (r.pickupTime >= 0) picked++;
        }
        assertEquals(3, picked, "All requests should be eventually picked");
        assertTrue(requests.get(2).pickupTime > requests.get(0).pickupTime, "3rd passenger should wait");
    }

    @Test
    public void testElevatorDirectionChange() throws IOException {
        List<Request> requests = List.of(
                new Request(0, "p1", 1, 10),
                new Request(2, "p2", 5, 2)
        );

        ElevatorSimulator sim = new ElevatorSimulator(1, 10, 2, new FCFSStrategy(), requests);
        sim.run();

        Request r1 = requests.get(0);
        Request r2 = requests.get(1);

        assertTrue(r1.dropOffTime > 0);
        assertTrue(r2.dropOffTime > r2.pickupTime);
    }

    @Test
    public void testElevatorLogsFileCreation() throws IOException {
        List<Request> requests = List.of(new Request(0, "p1", 1, 5));
        ElevatorSimulator sim = new ElevatorSimulator(1, 10, 2, new FCFSStrategy(), requests);
        sim.run();

        File log = new File("elevator_log_FCFS (First Come First Serve).csv");
        assertTrue(log.exists(), "Log file should be created");
        assertTrue(log.length() > 0, "Log file should not be empty");
    }

    @Test
    public void stressTestLargeRequestVolume() throws IOException {
        Random rand = new Random(42);
        List<Request> bulkRequests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int time = rand.nextInt(100);
            int source = rand.nextInt(10) + 1;
            int dest;
            do {
                dest = rand.nextInt(10) + 1;
            } while (dest == source);
            bulkRequests.add(new Request(time, "user" + i, source, dest));
        }

        for (SchedulingStrategy strategy : List.of(new FCFSStrategy(), new NearestCarStrategy(), new LoadBalancingStrategy(), new SCANStrategy())) {
            List<Request> clonedRequests = cloneRequests(bulkRequests);
            ElevatorSimulator sim = new ElevatorSimulator(5, 10, 4, strategy, clonedRequests);
            sim.run();

            long incomplete = clonedRequests.stream().filter(r -> !r.isComplete()).count();
            assertEquals(0, incomplete, "All requests should be served");
        }
    }

    @AfterEach
    public void cleanup() {
        new File("elevator_log.csv").delete();
    }

    private List<Request> cloneRequests(List<Request> original) {
        List<Request> copy = new ArrayList<>();
        for (Request r : original) {
            copy.add(new Request(r.time, r.id, r.sourceFloor, r.destinationFloor));
        }
        return copy;
    }
}
