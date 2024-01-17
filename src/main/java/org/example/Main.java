package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class Main {
    public static void main(String[] args) throws IOException {

        var jacksonMapper = new ObjectMapper();
        File ticketFile = new File("C:\\projects\\tickets\\src\\main\\resources\\tickets.json");
        TicketsList ticketList;
        ticketList = jacksonMapper.readValue(ticketFile, TicketsList.class);
        TicketService ticketService = new TicketService();
        foundMinTime(ticketList);
        foundPrices(ticketList);
    }

    private static void foundPrices(TicketsList ticketList) {
        OptionalDouble average = ticketList.getTickets()
                .stream()
                .filter(t -> t.getOrigin().equals("VVO") && t.getDestination().equals("TLV"))
                .mapToInt(t -> t.getPrice())
                .average();
        System.out.println(average);
       double median = ticketList.getTickets()
                .stream()
                .mapToInt(t -> t.getPrice())
                .sorted()
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        prices -> {
                            int count = prices.size();
                            if(count %2 == 0) {
                                int i = (prices.get(count / 2 - 1) + prices.get(count / 2)) / 2;
                                return i;

                            }else {
                                return prices.get(count /2);
                            }
                        }
                ));

    }

    private static void foundMinTime(TicketsList ticketList) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        LocalDateTime departureDateTime;
        LocalDateTime arrivalDateTime;
        long min = Long.MAX_VALUE;
        List<Long> times = new ArrayList<>();
        long price=0;
        for (Ticket ticket : ticketList.getTickets()) {
            if (ticket.getOrigin().equals("VVO") && ticket.getDestination().equals("TLV")) {

                if (ticket.getDepartureTime().length() == 4) {
                    departureDateTime = LocalDateTime.parse(ticket.getDepartureDate() + " 0" + ticket.getDepartureTime(), formatter);
                } else {
                    departureDateTime = LocalDateTime.parse(ticket.getDepartureDate() + " " + ticket.getDepartureTime(), formatter);
                }
                if (ticket.getArrivalTime().length() == 4) {
                    arrivalDateTime = LocalDateTime.parse(ticket.getArrivalDate() + " 0" + ticket.getArrivalTime(), formatter);
                } else {
                    arrivalDateTime = LocalDateTime.parse(ticket.getArrivalDate() + " " + ticket.getArrivalTime(), formatter);
                }
                long diff = departureDateTime.until(arrivalDateTime, ChronoUnit.SECONDS);
                times.add(diff);

                if (min > diff) {
                    min = diff;
                    price = ticket.getPrice();
                }


            }
        }

        System.out.println("__________________________");
        System.out.println(min + " " + price );
    }
}