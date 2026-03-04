package com.tmt.ticketsystem.ticket.model.dto;

public record TicketDto(
        Long id,
        String ticketId,
        String category,
        String subCategory,
        String comments,
        String ticketPriority,
        String date,
        String status,
        String raisedBy,
        String ticketInfo,
        String assignee) {
}
