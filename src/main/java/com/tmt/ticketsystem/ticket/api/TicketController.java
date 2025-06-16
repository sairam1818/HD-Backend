package com.tmt.ticketsystem.ticket.api;

import com.tmt.ticketsystem.ticket.model.response.DeleteResponse;
import com.tmt.ticketsystem.ticket.model.dto.TicketDto;
import com.tmt.ticketsystem.ticket.model.entity.Ticket;
import com.tmt.ticketsystem.ticket.service.TicketService;
import com.tmt.ticketsystem.ticket.converters.ticket.TicketConverter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final TicketConverter ticketConverter;

    @PostMapping
    public TicketDto addTickets(@RequestBody TicketDto ticketDto) {
        Ticket ticket = ticketConverter.dtoToEntity(ticketDto);
        return ticketConverter.entityToDto(ticketService.addTickets(ticket));
    }

    @GetMapping
    public List<TicketDto> getAllTickets() {
        List<Ticket> getTicketsList = ticketService.getTicketsList();
        return ticketConverter.entityToDto(getTicketsList);
    }

    @GetMapping("{ticketId}")
    public TicketDto getTicketDetails(@PathVariable String ticketId) {
        Ticket ticket = ticketService.getTicketDetails(ticketId);
        return ticketConverter.entityToDto(ticket);
    }

    @GetMapping("raisedBy/{raisedBy}")
    public List<TicketDto> getTicketsByUsername(@PathVariable String raisedBy) {
        return ticketConverter.entityToDto(ticketService.getTicketsByUsername(raisedBy));
    }

    @GetMapping("assignee/{assignee}")
    public List<TicketDto> getTicketsByAssignee(@PathVariable String assignee) {
        return ticketConverter.entityToDto(ticketService.getTicketsByAssignee(assignee));
    }

    @PutMapping("{ticketId}")
    public ResponseEntity<TicketDto> updateTicketStatus(@PathVariable String ticketId, @RequestBody TicketDto updateDto) {
        Ticket ticket = ticketService.updateTicketStatus(ticketId, updateDto);
        return ResponseEntity.ok(ticketConverter.entityToDto(ticket));
    }

    @DeleteMapping("{ticketId}")
    public DeleteResponse deleteTicket(@PathVariable String ticketId){
        return ticketService.deleteTicket(ticketId);
    }

    @PutMapping("assign/{ticketId}")
    public TicketDto updateTicket(@PathVariable String ticketId, @RequestBody TicketDto ticketDto) {

        return ticketConverter.entityToDto(ticketService.updateTicket(ticketId, ticketDto));
    }


}
