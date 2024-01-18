package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.model.Ticket;

import java.util.List;
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketsList {
    @JsonProperty("tickets")
    private List<Ticket> tickets ;
}
