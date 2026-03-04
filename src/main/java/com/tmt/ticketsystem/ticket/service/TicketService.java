package com.tmt.ticketsystem.ticket.service;

import com.tmt.ticketsystem.enums.TicketStatus;
import com.tmt.ticketsystem.exception.TicketNotFoundException;
import com.tmt.ticketsystem.ticket.model.dto.TicketDto;
import com.tmt.ticketsystem.ticket.model.entity.Ticket;
import com.tmt.ticketsystem.ticket.model.response.DeleteResponse;
import com.tmt.ticketsystem.ticket.repository.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@AllArgsConstructor
@Service
public class TicketService {

    private TicketRepository ticketRepository;

    public Ticket addTickets(Ticket ticket) {
        ticket.setStatus(TicketStatus.In_Progress.getStatus());
        Random random = new Random();
        int randomNumber = random.nextInt(90000000);
        ticket.setTicketId(generateTicketId(ticket.getCategory()) + "_" + generateTicketId(ticket.getSubCategory())
                + "_" + ticket.getDate().substring(0, 10) + "_" + randomNumber);
        return ticketRepository.save(ticket);
    }

    private String generateTicketId(String category) {
        if (category.length() > 3) {
            return category.substring(0, 3);
        } else {
            return category;
        }
    }

    public List<Ticket> getTicketsList() {
        return ticketRepository.findAll();
    }

    public List<Ticket> getTicketsByUsername(String raisedBy) {
        List<Ticket> tickets = ticketRepository.findByRaisedBy(raisedBy);
        return tickets;
    }

    public Ticket updateTicketStatus(String ticketId, TicketDto updateDto) {
        Optional<Ticket> optionalTicket = Optional.ofNullable(ticketRepository.findByTicketId(ticketId));
        if (optionalTicket.isPresent()) {
            optionalTicket.get().setStatus(TicketStatus.SOLVED.getStatus());
            return ticketRepository.save(optionalTicket.get());
        } else {
            throw new NullPointerException("Ticket not found");
        }
    }

    public Ticket getTicketDetails(String ticketId) {
        Optional<Ticket> result = Optional.ofNullable(ticketRepository.findByTicketId(ticketId));
        if (result.isEmpty()) {
            throw new TicketNotFoundException("Ticket not found: " + ticketId);
        }
        return result.get();
    }

    public DeleteResponse deleteTicket(String ticketId) {
        Optional<Ticket> ticket = Optional.ofNullable(ticketRepository.findByTicketId(ticketId));
        if (ticket.isPresent()) {
            DeleteResponse deleteResponse = new DeleteResponse();
            try {
                ticketRepository.deleteById(ticket.get().getId());
                deleteResponse.setResponseMessage("Ticket withdrawal was successful");

            } catch (TicketNotFoundException e) {
                throw new TicketNotFoundException("Ticket withdrawal failed" + ticketId);
            }
            return deleteResponse;
        } else
            throw new TicketNotFoundException("Ticket not found: " + ticketId);
    }

    public Ticket updateTicket(String ticketId, TicketDto ticketDto) {
        Optional<Ticket> optionalTicket = Optional.ofNullable(ticketRepository.findByTicketId(ticketId));
        if (optionalTicket.isPresent()) {
            optionalTicket.get().setAssignee(ticketDto.assignee());
            return ticketRepository.save(optionalTicket.get());
        } else {
            throw new TicketNotFoundException("Ticket not found");
        }
    }

    public List<Ticket> getTicketsByAssignee(String assignee) {
        List<Ticket> tickets = ticketRepository.findByAssignee(assignee);
        return tickets;
    }
}
