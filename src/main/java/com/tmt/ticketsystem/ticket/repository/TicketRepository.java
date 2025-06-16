package com.tmt.ticketsystem.ticket.repository;

import com.tmt.ticketsystem.ticket.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Ticket findByTicketId(String ticketId);

    List<Ticket> findByRaisedBy(String raisedBy);

    List<Ticket> findByAssignee(String assignee);
}
