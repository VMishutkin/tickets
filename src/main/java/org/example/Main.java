package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Ticket;
import org.example.model.TicketsList;
import org.example.service.TicketService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.util.stream.Collectors.*;

public class Main {
    public static void main(String[] args) throws IOException {
        var jacksonMapper = new ObjectMapper();
        String JsonFileName = "C:\\projects\\tickets\\src\\main\\resources\\tickets.json";
        File ticketFile = new File(JsonFileName);
        TicketsList ticketList;
        ticketList = jacksonMapper.readValue(ticketFile, TicketsList.class);
        TicketService ticketService = new TicketService();
        System.out.println(ticketService.foundMinTimeFlight(ticketList, "VVO", "TLV"));
        System.out.println(ticketService.foundPrices(ticketList, "VVO", "TLV"));
    }


}
