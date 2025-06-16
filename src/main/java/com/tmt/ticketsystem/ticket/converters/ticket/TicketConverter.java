package com.tmt.ticketsystem.ticket.converters.ticket;

import com.tmt.ticketsystem.ticket.model.dto.TicketDto;
import com.tmt.ticketsystem.ticket.model.entity.Ticket;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class TicketConverter {
    private final ModelMapper modelMapper;

    public TicketDto entityToDto(Ticket ticket) {
        TicketDto ticketDto = modelMapper.map(ticket, TicketDto.class);
        return ticketDto;
    }

    public List<TicketDto> entityToDto(List<Ticket> ticketList) {
        List<TicketDto> ticketDtoList = new ArrayList<>();
        for (Ticket ticket : ticketList) {
            if (ticket != null) {
                TicketDto ticketDto = modelMapper.map(ticket, TicketDto.class);
                ticketDtoList.add(ticketDto);
            }
        }
        return ticketDtoList;
    }

    public Ticket dtoToEntity(TicketDto ticketDto) {
        Ticket ticket = modelMapper.map(ticketDto, Ticket.class);
        return ticket;
    }

    public List<Ticket> dtoToEntity(List<TicketDto> ticketDto) {
        return ticketDto.stream().map(x -> dtoToEntity(x)).collect(Collectors.toList());
    }
}
