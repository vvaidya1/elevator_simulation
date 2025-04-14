# Elevator System Simulation (Java)

This is a Java-based simulation of a Type 2 elevator system — where passengers specify both source and destination floors upfront. The system supports multiple elevators, strategies for scheduling, capacity limits, and logs performance metrics.

---

## Features

- Multiple configurable elevators and building floors
- Time-based discrete simulation
- Implements Strategy Pattern for plug-and-play scheduling strategies
- Passenger capacity enforcement
- Logs elevator movement and generates summary stats
- Reads passenger requests from a CSV file
- Includes unit tests and stress tests

---

## Requirements

- Java 8 or above
- Terminal/IDE for compilation and execution
- CSV file with passenger request data

---

## ▶️ How to Run

1. **Clone the repository**

   ```bash
   git clone https://github.com/your-username/elevator-system-strategy.git
   cd elevator-system-strategy

2. Place your requests.csv file inside the /data folder

3. Compile
   ```bash
   javac -d target src/**/*.java

4. Run the simulation
   ```bash
   java -cp target ElevatorMain

## Input Format
Passenger requests are stored in a CSV file (src/main/resources) with the following structure:

```
time,id,source,dest
0,passenger1,1,5
0,passenger2,2,8
10,passenger3,4,1
```

time: Time (in ticks) at which the passenger makes the request\
id: Unique identifier\
source: Source floor\
dest: Destination floor

## Output Format
Logs elevator positions at each time unit and the final statistics

```
=== Simulation Summary ===
Strategy: SCAN
Time,E0,E1,E2,E3
0,1,1,1,1
........
49,10,10,8,5
Requests: 21
Wait Time - Min: 0, Max: 30, Avg: 6.05
Ride Time - Min: 1, Max: 13, Avg: 5.76
```

## Assumptions
- Elevators move 1 floor per time tick
- Time is discrete and simulated, not real-time
- Capacity is enforced per elevator (e.g., 4 passengers max)
- Each elevator starts at floor 1
- All request times are known only when they occur (no look-ahead)
- Passenger IDs are unique

## System Components
| Component          | Description                                                      |
| ------------------ | ---------------------------------------------------------------- |
| ElevatorMain       | Main Java class to invoke the simulator                          |
| ElevatorSimulator  | Manages the simulation loop, assigns requests                    |
| Elevator           | Handles individual elevator state (position, pickups, drops)     |
| Request            | Models each passenger’s trip                                     |
| CSVReader          | Loads the CSV input                                              |
| Strategies         | Each scheduling algorithm implementation                         |


## Strategy Comparison

| Strategy      | Description                                           | Pros                       | Cons                     |
| ------------- | ----------------------------------------------------- | -------------------------- | -----------------------  |
| FCFS          | Assigns all requests in order to the first elevator   | Simple and fair            | Doesn't scale or optimize|
| Nearest Car   | Chooses closest elevator to passenger                 | Fast response              | Doesn't balance load     |
| Load Balancing| Assigns to elevator with fewest pickups               | Avoids overloaded elevators| Ignores travel distance  |
| SCAN          | Elevators sweep floors in one direction, then reverse | Fair and efficient         | Sighltly more complex    |

## Unit Tests
- Request parsing
- Elevator capacity limit test
- Single request fully complete
- Elevator direction change
- Log file creation

## Stress Tests
- Simulates high-load scenarios with hundreds of passenger requests to evaluate strategy performance under pressure