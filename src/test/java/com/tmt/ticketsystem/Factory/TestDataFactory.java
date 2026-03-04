package com.tmt.ticketsystem.Factory;

import com.tmt.ticketsystem.enums.Roles;
import com.tmt.ticketsystem.enums.TicketStatus;
import com.tmt.ticketsystem.login.model.dto.LoginDto;
import com.tmt.ticketsystem.login.model.dto.UserDto;
import com.tmt.ticketsystem.login.model.entity.User;
import com.tmt.ticketsystem.login.model.response.LoginResponse;
import com.tmt.ticketsystem.login.model.response.UserResponse;
import com.tmt.ticketsystem.ticket.model.dto.TicketDto;
import com.tmt.ticketsystem.ticket.model.entity.Attachment;
import com.tmt.ticketsystem.ticket.model.entity.Ticket;
import com.tmt.ticketsystem.ticket.model.response.DeleteResponse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestDataFactory {

    // ===== User Entity Builders =====

    public static User buildTestUserEntity() {
        User user = new User();
        user.setId(1);
        user.setUserName("test_user");
        user.setPassword("password123");
        user.setRole(Roles.USER);
        return user;
    }

    public static User buildAdminUserEntity() {
        User user = new User();
        user.setId(2);
        user.setUserName("admin_user");
        user.setPassword("admin123");
        user.setRole(Roles.ADMIN);
        return user;
    }

    public static User buildSuperAdminUserEntity() {
        User user = new User();
        user.setId(3);
        user.setUserName("super_admin");
        user.setPassword("super123");
        user.setRole(Roles.SUPER_ADMIN);
        return user;
    }

    public static List<User> buildUserEntityList() {
        return Arrays.asList(buildTestUserEntity(), buildAdminUserEntity(), buildSuperAdminUserEntity());
    }

    // ===== UserDto Builders =====

    public static UserDto buildTestUserDto() {
        UserDto dto = new UserDto();
        dto.setId(1);
        dto.setUserName("test_user");
        dto.setPassword("password123");
        dto.setRole(Roles.USER);
        return dto;
    }

    public static UserDto buildAdminUserDto() {
        UserDto dto = new UserDto();
        dto.setId(2);
        dto.setUserName("admin_user");
        dto.setPassword("admin123");
        dto.setRole(Roles.ADMIN);
        return dto;
    }

    public static List<UserDto> buildUserDtoList() {
        return Arrays.asList(buildTestUserDto(), buildAdminUserDto());
    }

    // ===== LoginDto Builders =====

    public static LoginDto buildValidLoginDto() {
        LoginDto dto = new LoginDto();
        dto.setUserName("test_user");
        dto.setPassword("password123");
        return dto;
    }

    public static LoginDto buildInvalidPasswordLoginDto() {
        LoginDto dto = new LoginDto();
        dto.setUserName("test_user");
        dto.setPassword("wrong_password");
        return dto;
    }

    public static LoginDto buildNonExistentUserLoginDto() {
        LoginDto dto = new LoginDto();
        dto.setUserName("unknown_user");
        dto.setPassword("password123");
        return dto;
    }

    // ===== LoginResponse Builders =====

    public static LoginResponse buildSuccessLoginResponse() {
        LoginResponse response = new LoginResponse();
        response.setUserName("test_user");
        response.setRole(Roles.USER);
        return response;
    }

    // ===== UserResponse Builders =====

    public static UserResponse buildSuccessUserResponse() {
        UserResponse response = new UserResponse();
        response.setMessage("user deleted successfully");
        return response;
    }

    // ===== Ticket Entity Builders =====

    /**
     * Builds a Ticket with category and subCategory longer than 3 characters.
     * This exercises the substring(0,3) branch in generateTicketId.
     */
    public static Ticket buildTestTicketWithLongCategory() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setCategory("Hardware");
        ticket.setSubCategory("Laptop");
        ticket.setComments("Screen not working");
        ticket.setTicketPriority("High");
        ticket.setDate("2026-03-04T08:00:00");
        ticket.setStatus(TicketStatus.In_Progress.getStatus());
        ticket.setRaisedBy("test_user");
        ticket.setTicketInfo("Broken screen");
        ticket.setAssignee("admin_user");
        return ticket;
    }

    /**
     * Builds a Ticket with category and subCategory of 3 or fewer characters.
     * This exercises the else branch (return category) in generateTicketId.
     */
    public static Ticket buildTestTicketWithShortCategory() {
        Ticket ticket = new Ticket();
        ticket.setId(2L);
        ticket.setCategory("IT");
        ticket.setSubCategory("OS");
        ticket.setComments("OS update needed");
        ticket.setTicketPriority("Low");
        ticket.setDate("2026-03-04T10:00:00");
        ticket.setStatus(TicketStatus.In_Progress.getStatus());
        ticket.setRaisedBy("admin_user");
        ticket.setTicketInfo("Update OS");
        ticket.setAssignee("super_admin");
        return ticket;
    }

    /**
     * Builds a Ticket with category exactly 3 characters (boundary).
     */
    public static Ticket buildTestTicketWithExact3CharCategory() {
        Ticket ticket = new Ticket();
        ticket.setId(3L);
        ticket.setCategory("Net");
        ticket.setSubCategory("VPN");
        ticket.setComments("VPN not connecting");
        ticket.setTicketPriority("Medium");
        ticket.setDate("2026-03-04T12:00:00");
        ticket.setStatus(TicketStatus.In_Progress.getStatus());
        ticket.setRaisedBy("test_user");
        ticket.setTicketInfo("VPN issue");
        ticket.setAssignee(null);
        return ticket;
    }

    public static Ticket buildSavedTicket() {
        Ticket ticket = buildTestTicketWithLongCategory();
        ticket.setTicketId("Har_Lap_2026-03-04_12345678");
        ticket.setStatus(TicketStatus.In_Progress.getStatus());
        return ticket;
    }

    public static List<Ticket> buildTicketEntityList() {
        return Arrays.asList(buildSavedTicket(), buildTestTicketWithShortCategory());
    }

    // ===== TicketDto Builders =====

    public static TicketDto buildTestTicketDto() {
        TicketDto dto = new TicketDto();
        dto.setId(1L);
        dto.setTicketId("Har_Lap_2026-03-04_12345678");
        dto.setCategory("Hardware");
        dto.setSubCategory("Laptop");
        dto.setComments("Screen not working");
        dto.setTicketPriority("High");
        dto.setDate("2026-03-04T08:00:00");
        dto.setStatus(TicketStatus.SOLVED.getStatus());
        dto.setRaisedBy("test_user");
        dto.setTicketInfo("Broken screen");
        dto.setAssignee("admin_user");
        return dto;
    }

    public static TicketDto buildTicketDtoWithNewAssignee() {
        TicketDto dto = new TicketDto();
        dto.setAssignee("new_assignee");
        return dto;
    }

    // ===== DeleteResponse Builders =====

    public static DeleteResponse buildSuccessDeleteResponse() {
        DeleteResponse response = new DeleteResponse();
        response.setResponseMessage("Ticket withdrawal was successful");
        return response;
    }

    // ===== Attachment Builders =====

    public static Attachment buildTestAttachment() {
        return Attachment.builder()
                .id(1L)
                .name("test_image.png")
                .type("image/png")
                .file(new byte[] { 1, 2, 3, 4, 5 })
                .build();
    }

    public static Attachment buildTestAttachmentWithCompressedData(byte[] compressedData) {
        return Attachment.builder()
                .id(1L)
                .name("test_image.png")
                .type("image/png")
                .file(compressedData)
                .build();
    }
}
