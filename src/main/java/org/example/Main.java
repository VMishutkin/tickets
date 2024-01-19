package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.model.TicketsList;
import org.example.service.TicketService;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main {
    private static final String JsonFileName = "tickets.json";

    public static void main(String[] args) throws IOException {

        var jacksonMapper = new ObjectMapper();
        URL jsonFile = Main.class.getResource(JsonFileName);
        TicketsList ticketList;
        ticketList = jacksonMapper.readValue(jsonFile, TicketsList.class);
        TicketService ticketService = new TicketService();

        ticketService.foundMinTimeFlight(ticketList, "VVO", "TLV");
        ticketService.foundPrices(ticketList, "VVO", "TLV");
    }


}
