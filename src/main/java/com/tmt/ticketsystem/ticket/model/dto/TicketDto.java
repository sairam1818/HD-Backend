package com.tmt.ticketsystem.ticket.model.dto;

import lombok.Data;

@Data
public class TicketDto {
    private Long id;
    private String ticketId;
    private String category;
    private String subCategory;
    private String comments;
    private String ticketPriority;
    private String date;
    private String status;
    private String raisedBy;
    private String ticketInfo;
    private String assignee;
}
