package com.tmt.ticketsystem.enums;

public enum TicketStatus {
    In_Progress("InProgress"),
    SOLVED("solved");

    private  final String status;
    TicketStatus(String status)
    {
        this.status=status;
    }

    public String getStatus() {
        return status;
    }
}
