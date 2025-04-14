package com.kkr.elevator_simulation;

import com.kkr.elevator_simulation.model.Direction;
import com.kkr.elevator_simulation.model.Request;
import com.kkr.elevator_simulation.model.RequestStatus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class Elevator {
    private final int id;
    private final int capacity;
    private final Queue<Request> pickupRequests = new LinkedList<>();
    private final List<Request> passengers = new ArrayList<>();
    private int currentFloor = 1;
    private Direction direction = Direction.IDLE;

    public Elevator(int i, int capacity) {
        this.id = i;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public void assign(Request request) {
        pickupRequests.add(request);
    }

    public boolean isIdle() {
        return direction == Direction.IDLE;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getTotalQueueSize() {
        return pickupRequests.size() + passengers.size();
    }

    public boolean hasCapacity() {
        return passengers.size() < capacity;
    }

    public Direction getDirection() {
        return direction;
    }
    public void setDirection(Direction dir) {
        this.direction = dir;
    }

    public void move(int time) {
        //prioritize existing passengers over new pick-up requests
        if (!passengers.isEmpty()) {
            Request dest = passengers.get(0);
            moveTo(dest.destinationFloor);

            //drop-off or pick-up passengers getting down or getting up at current floor
            dropOffPassengers(time);
            pickupPassengers(time);
        } else if (!pickupRequests.isEmpty()) {
            Request next = pickupRequests.peek();
            pickupPassengers(time);
            if (passengers.isEmpty())
                moveTo(next.sourceFloor);
            else
                moveTo(passengers.get(0).destinationFloor);
        } else {
            direction = Direction.IDLE;
        }
    }

    private void pickupPassengers(int time) {
        List<Request> pickup = pickupRequests.stream()
                .filter(x -> x.sourceFloor == currentFloor && passengers.size() < capacity)
                .collect(Collectors.toList());
        for (Request r : pickup) {
            r.pickupTime = time;
            passengers.add(r);
            pickupRequests.remove(r);
        }
    }

    private void dropOffPassengers(int time) {
        List<Request> dropOffPassengers = passengers.stream().filter(x -> x.destinationFloor == currentFloor).collect(Collectors.toList());
        if (!dropOffPassengers.isEmpty()) {
            dropOffPassengers.forEach(s -> {
                s.dropOffTime = time;
                s.requestStatus = RequestStatus.COMPLETED;
            });
            passengers.removeAll(dropOffPassengers);
        }
    }

    private void moveTo(int targetFloor) {
        if (currentFloor < targetFloor) {
            currentFloor++;
            direction = Direction.UP;
        } else if (currentFloor > targetFloor) {
            currentFloor--;
            direction = Direction.DOWN;
        }
    }
}
