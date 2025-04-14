package com.kkr.elevator_simulation.model;

public class Request {
    public final int time;
    public final String id;
    public final int sourceFloor;
    public final int destinationFloor;
    public int pickupTime = Integer.MIN_VALUE;
    public int dropOffTime = Integer.MIN_VALUE;
    public RequestStatus requestStatus;

    public Request(int time, String id, int sourceFloor, int destinationFloor) {
        this.time = time;
        this.id = id;
        this.sourceFloor = sourceFloor;
        this.destinationFloor = destinationFloor;
        this.requestStatus = RequestStatus.PENDING;
    }

    public boolean isComplete() {
        return dropOffTime != Integer.MIN_VALUE;
    }

    public boolean isPending() {
        return requestStatus == RequestStatus.PENDING;
    }
}
