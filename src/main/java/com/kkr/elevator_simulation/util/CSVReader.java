package com.kkr.elevator_simulation.util;

import com.kkr.elevator_simulation.model.Request;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    public static List<Request> readRequests(String fileName) throws IOException {
        List<Request> requests = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        reader.readLine();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            requests.add(new Request(
                    Integer.parseInt(parts[0]),
                    parts[1],
                    Integer.parseInt(parts[2]),
                    Integer.parseInt(parts[3])
            ));
        }
        reader.close();
        return requests;
    }
}
