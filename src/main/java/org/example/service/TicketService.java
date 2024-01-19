package org.example.service;

import org.example.model.Ticket;
import org.example.model.TicketsList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TicketService {
    /**
     * Метод считает минимальное время полета
     * @param ticketList список билетов в формате из файла tickets.json
     * @param origin код аэропорта вылета
     * @param destination код аэропорта назначения
     */
    public void foundMinTimeFlight(TicketsList ticketList, String origin, String destination) {

        addZeroInHours(ticketList);
        List<String> carriers = ticketList.getTickets()
                .stream()
                .map(Ticket::getCarrier)
                .distinct()
                .toList();
        for (String carrier: carriers) {
            List<Long> flightTimes = countFlightTimes(ticketList, origin, destination, carrier);
            long min = flightTimes.stream().mapToLong(v -> v).min().orElseThrow(NoSuchMethodError::new);
            String minFlightTime = getMinFlightTimeAsString(min);
            System.out.printf("Минимальное время полета из аэропорта %s в аэропорт %s перевозчиком %s- %s\n", origin, destination, carrier, minFlightTime);
        }

    }

    /**
     * Преобразовывает разницу в секундах в строку, содержащую часы минуты и секунды
     * @param min - разница в полетах в секундах
     * @return строка содержащая часы, минуты и секунды
     */
    private String getMinFlightTimeAsString(long min) {
        long hour = min / 3600,
                minute = min / 60 % 60,
                sec = min / 1 % 60;
        return hour + " часов, " + minute + " минут и " + sec + " секунд.";
    }

    /**
     * Добавляет 0 в строке где часы одной цифрой, должно быть везде 2
     * @param ticketList список билетов из файла json
     */
    private void addZeroInHours(TicketsList ticketList) {
        for (Ticket ticket : ticketList.getTickets()) {
            if (ticket.getDepartureTime().length() == 4) {
                ticket.setDepartureTime("0" + ticket.getDepartureTime());
            }
            if (ticket.getArrivalTime().length() == 4) {
                ticket.setArrivalTime(" 0" + ticket.getArrivalTime());
            }
        }
    }

    /**
     * метод из списка билетов фильтрует по нужным аэропортам и возвращает времени полета
     *
     * @param ticketList  список билетов из файла json
     * @param origin      код аэропорта вылета
     * @param destination код аэропорта назначения
     * @param carrier
     * @return список времени полета из аэропорта origin в destination
     */
    private List<Long> countFlightTimes(TicketsList ticketList, String origin, String destination, String carrier) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        LocalDateTime departureDateTime;
        LocalDateTime arrivalDateTime;
        List<Long> flightTimes = new ArrayList<>();
        for (Ticket ticket : ticketList.getTickets()) {
            departureDateTime = LocalDateTime.parse(ticket.getDepartureDate() + " " + ticket.getDepartureTime(), formatter);
            arrivalDateTime = LocalDateTime.parse(ticket.getArrivalDate() + " " + ticket.getArrivalTime(), formatter);
            if (ticket.getOrigin().equals(origin) && ticket.getDestination().equals(destination) && ticket.getCarrier().equals(carrier)) {
                long diff = departureDateTime.until(arrivalDateTime, ChronoUnit.SECONDS);
                flightTimes.add(diff);
            }
        }
        return flightTimes;
    }

    /**
     * Выводит разницу между средней ценой и медианой
     * @param ticketsList список билетов из файла json
     * @param origin код аэропорта вылета
     * @param destination код аэропорта назначения
     */
    public void foundDiffBetweenAverageAndMedianPrices(TicketsList ticketsList, String origin, String destination) {

        double average = countAveragePrice(ticketsList.getTickets(), origin, destination);
        double median = countMedianPrice(ticketsList.getTickets(), origin, destination);
        String resultString = average >= median ?
                "Средняя цена больше медианной на " + (average - median) + " рублей" :
                "Средняя цена меньше медианной на " + (median - average) + " рублей";
        System.out.println(resultString);


    }

    /**
     * Считает медианную цену
     * @param tickets список билетов
     * @param origin код аэропорта вылета
     * @param destination код аэропорта назначения
     * @return
     */
    private double countMedianPrice(List<Ticket> tickets, String origin, String destination) {
        List<Integer> prices = tickets
                .stream()
                .filter(t -> t.getOrigin().equals(origin) && t.getDestination().equals(destination))
                .mapToInt(Ticket::getPrice)
                .boxed()
                .sorted()
                .collect(toList());
        return prices.size() % 2 == 0 ?
                (double) (prices.get(prices.size() / 2 - 1) + prices.get(prices.size() / 2)) / 2 :
                prices.get(prices.size() / 2);
    }

    /**
     * Считает среднюю цену
     * @param tickets список билетов из файла json
     * @param origin код аэропорта вылета
     * @param destination код аэропорта назначения
     * @return
     */
    private double countAveragePrice(List<Ticket> tickets, String origin, String destination) {
        return tickets
                .stream()
                .filter(t -> t.getOrigin().equals(origin) && t.getDestination().equals(destination))
                .mapToInt(Ticket::getPrice)
                .average().getAsDouble();
    }

}
