package com.tmt.ticketsystem.ticket.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
