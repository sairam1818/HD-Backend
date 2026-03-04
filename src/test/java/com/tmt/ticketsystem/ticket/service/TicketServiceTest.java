package com.tmt.ticketsystem.ticket.service;

import com.tmt.ticketsystem.Factory.TestDataFactory;
import com.tmt.ticketsystem.enums.TicketStatus;
import com.tmt.ticketsystem.exception.TicketNotFoundException;
import com.tmt.ticketsystem.ticket.model.dto.TicketDto;
import com.tmt.ticketsystem.ticket.model.entity.Ticket;
import com.tmt.ticketsystem.ticket.model.response.DeleteResponse;
import com.tmt.ticketsystem.ticket.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    // ======================== addTickets ========================

    @Nested
    @DisplayName("addTickets Tests")
    class AddTicketsTests {

        @Test
        @DisplayName("Positive: Should add ticket with long category (>3 chars) — exercises substring branch")
        void addTickets_WithLongCategory_ShouldSetStatusAndGenerateId() {
            Ticket ticket = TestDataFactory.buildTestTicketWithLongCategory();
            // category = "Hardware" (>3), subCategory = "Laptop" (>3)

            when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Ticket result = ticketService.addTickets(ticket);

            assertNotNull(result);
            assertEquals(TicketStatus.In_Progress.getStatus(), result.getStatus());
            // ticketId format: Har_Lap_2026-03-04_<random>
            assertTrue(result.getTicketId().startsWith("Har_Lap_2026-03-04_"));
            verify(ticketRepository, times(1)).save(ticket);
        }

        @Test
        @DisplayName("Positive: Should add ticket with short category (<=3 chars) — exercises else branch")
        void addTickets_WithShortCategory_ShouldUseFullCategoryInId() {
            Ticket ticket = TestDataFactory.buildTestTicketWithShortCategory();
            // category = "IT" (<=3), subCategory = "OS" (<=3)

            when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Ticket result = ticketService.addTickets(ticket);

            assertNotNull(result);
            assertEquals(TicketStatus.In_Progress.getStatus(), result.getStatus());
            // ticketId format: IT_OS_2026-03-04_<random>
            assertTrue(result.getTicketId().startsWith("IT_OS_2026-03-04_"));
            verify(ticketRepository, times(1)).save(ticket);
        }

        @Test
        @DisplayName("Edge: Should add ticket with exactly 3-char category — boundary for generateTicketId")
        void addTickets_WithExact3CharCategory_ShouldUseFullCategoryInId() {
            Ticket ticket = TestDataFactory.buildTestTicketWithExact3CharCategory();
            // category = "Net" (==3), subCategory = "VPN" (==3) → NOT > 3, so else branch

            when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Ticket result = ticketService.addTickets(ticket);

            assertNotNull(result);
            assertTrue(result.getTicketId().startsWith("Net_VPN_2026-03-04_"));
            verify(ticketRepository, times(1)).save(ticket);
        }

        @Test
        @DisplayName("Edge: Should add ticket with 4-char category — just above boundary, enters substring branch")
        void addTickets_With4CharCategory_ShouldSubstringTo3() {
            Ticket ticket = new Ticket();
            ticket.setCategory("ABCD"); // length 4 > 3 → "ABC"
            ticket.setSubCategory("WXYZ"); // length 4 > 3 → "WXY"
            ticket.setDate("2026-01-15T00:00:00");

            when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Ticket result = ticketService.addTickets(ticket);

            assertTrue(result.getTicketId().startsWith("ABC_WXY_2026-01-15_"));
        }

        @Test
        @DisplayName("Edge: Category with 1 character — shortest possible")
        void addTickets_WithSingleCharCategory_ShouldUseFullChar() {
            Ticket ticket = new Ticket();
            ticket.setCategory("A");
            ticket.setSubCategory("B");
            ticket.setDate("2026-06-01T12:00:00");

            when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Ticket result = ticketService.addTickets(ticket);

            assertTrue(result.getTicketId().startsWith("A_B_2026-06-01_"));
        }

        @Test
        @DisplayName("Edge: Mixed long category + short subCategory")
        void addTickets_WithMixedLengthCategories_ShouldHandleBothBranches() {
            Ticket ticket = new Ticket();
            ticket.setCategory("Software"); // >3 → "Sof"
            ticket.setSubCategory("DB"); // <=3 
                    ticket.setDate("2026-12-25T00:00:00");
            when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Ticket result = ticketService.addTickets(ticket);

            assertTrue(result.getTicketId().startsWith("Sof_DB_2026-12-25_"));
        }

        @Test
        @DisplayName("Positive: Should always set status to InProgress regardless of input status")
        void addTickets_ShouldOverrideStatusToInProgress() {
            Ticket ticket = TestDataFactory.buildTestTicketWithLongCategory();
            ticket.setStatus("Anything"); // pre-set to something else

            when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Ticket result = ticketService.addTickets(ticket);

            assertEquals(TicketStatus.In_Progress.getStatus(), result.getStatus());
        }

        @Test
        @DisplayName("Positive: Generated ticketId should contain a random numeric suffix")
        void addTickets_ShouldContainRandomNumericSuffix() {
            Ticket ticket = TestDataFactory.buildTestTicketWithLongCategory();

            when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Ticket result = ticketService.addTickets(ticket);

            // Verify the last part after the final underscore is numeric
            String ticketId = result.getTicketId();
            String[] parts = ticketId.split("_");
            String randomPart = parts[parts.length - 1];
            assertDoesNotThrow(() -> Integer.parseInt(randomPart));
        }
    }

    // ======================== getTicketsList ========================

    @Nested
    @DisplayName("getTicketsList Tests")
    class GetTicketsListTests {

        @Test
        @DisplayName("Positive: Should return all tickets")
        void getTicketsList_ShouldReturnAllTickets() {
            List<Ticket> tickets = TestDataFactory.buildTicketEntityList();
            when(ticketRepository.findAll()).thenReturn(tickets);

            List<Ticket> result = ticketService.getTicketsList();

            assertNotNull(result);
            assertEquals(2, result.size());
            verify(ticketRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Edge: Should return empty list when no tickets exist")
        void getTicketsList_ShouldReturnEmptyList_WhenNoTickets() {
            when(ticketRepository.findAll()).thenReturn(Collections.emptyList());

            List<Ticket> result = ticketService.getTicketsList();

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(ticketRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Positive: Should return single ticket in list")
        void getTicketsList_ShouldReturnSingleTicket() {
            Ticket ticket = TestDataFactory.buildSavedTicket();
            when(ticketRepository.findAll()).thenReturn(Collections.singletonList(ticket));

            List<Ticket> result = ticketService.getTicketsList();

            assertEquals(1, result.size());
            assertEquals(ticket.getTicketId(), result.get(0).getTicketId());
        }
    }

    // ======================== getTicketsByUsername ========================

    @Nested
    @DisplayName("getTicketsByUsername Tests")
    class GetTicketsByUsernameTests {

        @Test
        @DisplayName("Positive: Should return tickets for a valid username")
        void getTicketsByUsername_ShouldReturnMatchingTickets() {
            List<Ticket> tickets = Collections.singletonList(TestDataFactory.buildSavedTicket());
            when(ticketRepository.findByRaisedBy("test_user")).thenReturn(tickets);

            List<Ticket> result = ticketService.getTicketsByUsername("test_user");

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("test_user", result.get(0).getRaisedBy());
            verify(ticketRepository, times(1)).findByRaisedBy("test_user");
        }

        @Test
        @DisplayName("Negative: Should return empty list for non-existent username")
        void getTicketsByUsername_ShouldReturnEmptyList_WhenUserNotFound() {
            when(ticketRepository.findByRaisedBy("unknown_user")).thenReturn(Collections.emptyList());

            List<Ticket> result = ticketService.getTicketsByUsername("unknown_user");

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Edge: Should handle null username gracefully")
        void getTicketsByUsername_ShouldHandleNullUsername() {
            when(ticketRepository.findByRaisedBy(null)).thenReturn(Collections.emptyList());

            List<Ticket> result = ticketService.getTicketsByUsername(null);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Positive: Should return multiple tickets for the same user")
        void getTicketsByUsername_ShouldReturnMultipleTickets() {
            Ticket ticket1 = TestDataFactory.buildSavedTicket();
            Ticket ticket2 = TestDataFactory.buildSavedTicket();
            ticket2.setId(5L);
            List<Ticket> tickets = Arrays.asList(ticket1, ticket2);
            when(ticketRepository.findByRaisedBy("test_user")).thenReturn(tickets);

            List<Ticket> result = ticketService.getTicketsByUsername("test_user");

            assertEquals(2, result.size());
        }
    }

    // ======================== updateTicketStatus ========================

    @Nested
    @DisplayName("updateTicketStatus Tests")
    class UpdateTicketStatusTests {

        @Test
        @DisplayName("Positive: Should update status to SOLVED when ticket exists")
        void updateTicketStatus_ShouldSetStatusToSolved_WhenTicketExists() {
            Ticket ticket = TestDataFactory.buildSavedTicket();
            TicketDto dto = TestDataFactory.buildTestTicketDto();

            when(ticketRepository.findByTicketId("Har_Lap_2026-03-04_12345678")).thenReturn(ticket);
            when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Ticket result = ticketService.updateTicketStatus("Har_Lap_2026-03-04_12345678", dto);

            assertNotNull(result);
            assertEquals(TicketStatus.SOLVED.getStatus(), result.getStatus());
            verify(ticketRepository, times(1)).save(ticket);
        }

        @Test
        @DisplayName("Negative: Should throw NullPointerException when ticket not found")
        void updateTicketStatus_ShouldThrowNPE_WhenTicketNotFound() {
            TicketDto dto = TestDataFactory.buildTestTicketDto();

            when(ticketRepository.findByTicketId("NON_EXISTENT")).thenReturn(null);

            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> ticketService.updateTicketStatus("NON_EXISTENT", dto));

            assertEquals("Ticket not found", exception.getMessage());
            verify(ticketRepository, never()).save(any());
        }

        @Test
        @DisplayName("Edge: Should update status regardless of dto content — dto is unused")
        void updateTicketStatus_ShouldIgnoreDtoContent() {
            Ticket ticket = TestDataFactory.buildSavedTicket();
            TicketDto emptyDto = new TicketDto(); // empty dto

            when(ticketRepository.findByTicketId("Har_Lap_2026-03-04_12345678")).thenReturn(ticket);
            when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Ticket result = ticketService.updateTicketStatus("Har_Lap_2026-03-04_12345678", emptyDto);

            assertEquals(TicketStatus.SOLVED.getStatus(), result.getStatus());
        }
    }

    // ======================== getTicketDetails ========================

    @Nested
    @DisplayName("getTicketDetails Tests")
    class GetTicketDetailsTests {

        @Test
        @DisplayName("Positive: Should return ticket when found")
        void getTicketDetails_ShouldReturnTicket_WhenFound() {
            Ticket ticket = TestDataFactory.buildSavedTicket();

            when(ticketRepository.findByTicketId("Har_Lap_2026-03-04_12345678")).thenReturn(ticket);

            Ticket result = ticketService.getTicketDetails("Har_Lap_2026-03-04_12345678");

            assertNotNull(result);
            assertEquals("Har_Lap_2026-03-04_12345678", result.getTicketId());
            assertEquals("Hardware", result.getCategory());
            verify(ticketRepository, times(1)).findByTicketId("Har_Lap_2026-03-04_12345678");
        }

        @Test
        @DisplayName("Negative: Should throw TicketNotFoundException when ticket not found")
        void getTicketDetails_ShouldThrowException_WhenTicketNotFound() {
            when(ticketRepository.findByTicketId("INVALID_ID")).thenReturn(null);

            TicketNotFoundException exception = assertThrows(
                    TicketNotFoundException.class,
                    () -> ticketService.getTicketDetails("INVALID_ID")
            );

            assertEquals("Ticket not found: INVALID_ID", exception.getMessage());
        }

        @Test
        @DisplayName("Edge: Should throw TicketNotFoundException with proper message including ticketId")
        void getTicketDetails_ExceptionMessage_ShouldContainTicketId() {
            when(ticketRepository.findByTicketId("XYZ_123")).thenReturn(null);

            TicketNotFoundException exception = assertThrows(
                    TicketNotFoundException.class,
                    () -> ticketService.getTicketDetails("XYZ_123")
            );

            assertTrue(exception.getMessage().contains("XYZ_123"));
        }
    }

    // ======================== deleteTicket ========================

    @Nested
    @DisplayName("deleteTicket Tests")
    class DeleteTicketTests {

        @Test
        @DisplayName("Positive: Should delete ticket and return success response when ticket found")
        void deleteTicket_ShouldReturnSuccessResponse_WhenTicketExists() {
            Ticket ticket = TestDataFactory.buildSavedTicket();

            when(ticketRepository.findByTicketId("Har_Lap_2026-03-04_12345678")).thenReturn(ticket);
            doNothing().when(ticketRepository).deleteById(ticket.getId());

            DeleteResponse result = ticketService.deleteTicket("Har_Lap_2026-03-04_12345678");

            assertNotNull(result);
            assertEquals("Ticket withdrawal was successful", result.getResponseMessage());
            verify(ticketRepository, times(1)).deleteById(ticket.getId());
        }

        @Test
        @DisplayName("Negative: Should throw TicketNotFoundException when ticket not found")
        void deleteTicket_ShouldThrowException_WhenTicketNotFound() {
            when(ticketRepository.findByTicketId("NON_EXISTENT")).thenReturn(null);

            TicketNotFoundException exception = assertThrows(
                    TicketNotFoundException.class,
                    () -> ticketService.deleteTicket("NON_EXISTENT")
            );

            assertEquals("Ticket not found: NON_EXISTENT", exception.getMessage());
            verify(ticketRepository, never()).deleteById(any());
        }

        @Test
        @DisplayName("Negative: Should throw TicketNotFoundException when deleteById throws TicketNotFoundException")
        void deleteTicket_ShouldThrowException_WhenDeleteByIdFails() {
            Ticket ticket = TestDataFactory.buildSavedTicket();

            when(ticketRepository.findByTicketId("Har_Lap_2026-03-04_12345678")).thenReturn(ticket);
            doThrow(new TicketNotFoundException("DB error"))
                    .when(ticketRepository).deleteById(ticket.getId());

            TicketNotFoundException exception = assertThrows(
                    TicketNotFoundException.class,
                    () -> ticketService.deleteTicket("Har_Lap_2026-03-04_12345678"));

            assertEquals("Ticket withdrawal failedHar_Lap_2026-03-04_12345678", exception.getMessage());
        }

        @Test
        @DisplayName("Edge: Exception message in else branch should include ticketId")
        void deleteTicket_ExceptionMessage_ShouldContainTicketId() {
            when(ticketRepository.findByTicketId("MISSING_TICKET")).thenReturn(null);

            TicketNotFoundException exception = assertThrows(
                    TicketNotFoundException.class,
                    () -> ticketService.deleteTicket("MISSING_TICKET")
            );

            assertTrue(exception.getMessage().contains("MISSING_TICKET"));
        }
    }

    // ======================== updateTicket ========================

    @Nested
    @DisplayName("updateTicket Tests")
    class UpdateTicketTests {

        @Test
        @DisplayName("Positive: Should update assignee when ticket exists")
        void updateTicket_ShouldUpdateAssignee_WhenTicketExists() {
            Ticket ticket = TestDataFactory.buildSavedTicket();
            TicketDto updateDto = TestDataFactory.buildTicketDtoWithNewAssignee();

            when(ticketRepository.findByTicketId("Har_Lap_2026-03-04_12345678")).thenReturn(ticket);
            when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Ticket result = ticketService.updateTicket("Har_Lap_2026-03-04_12345678", updateDto);

            assertNotNull(result);
            assertEquals("new_assignee", result.getAssignee());
            verify(ticketRepository, times(1)).save(ticket);
        }

        @Test
        @DisplayName("Negative: Should throw TicketNotFoundException when ticket not found")
        void updateTicket_ShouldThrowException_WhenTicketNotFound() {
            TicketDto updateDto = TestDataFactory.buildTicketDtoWithNewAssignee();

            when(ticketRepository.findByTicketId("NON_EXISTENT")).thenReturn(null);

            TicketNotFoundException exception = assertThrows(
                    TicketNotFoundException.class,
                    () -> ticketService.updateTicket("NON_EXISTENT", updateDto));

            assertEquals("Ticket not found", exception.getMessage());
            verify(ticketRepository, never()).save(any());
        }

        @Test
        @DisplayName("Edge: Should set assignee to null when dto has null assignee")
        void updateTicket_ShouldSetAssigneeToNull_WhenDtoAssigneeIsNull() {
            Ticket ticket = TestDataFactory.buildSavedTicket();
            TicketDto dto = new TicketDto(); // assignee is null

            when(ticketRepository.findByTicketId("Har_Lap_2026-03-04_12345678")).thenReturn(ticket);
            when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Ticket result = ticketService.updateTicket("Har_Lap_2026-03-04_12345678", dto);

            assertNull(result.getAssignee());
        }

        @Test
        @DisplayName("Positive: Should only update assignee and not modify other fields")
        void updateTicket_ShouldOnlyUpdateAssignee_NotOtherFields() {
            Ticket ticket = TestDataFactory.buildSavedTicket();
            String originalCategory = ticket.getCategory();
            String originalStatus = ticket.getStatus();
            TicketDto updateDto = TestDataFactory.buildTicketDtoWithNewAssignee();

            when(ticketRepository.findByTicketId("Har_Lap_2026-03-04_12345678")).thenReturn(ticket);
            when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Ticket result = ticketService.updateTicket("Har_Lap_2026-03-04_12345678", updateDto);

            assertEquals("new_assignee", result.getAssignee());
            assertEquals(originalCategory, result.getCategory());
            assertEquals(originalStatus, result.getStatus());
        }
    }

    // ======================== getTicketsByAssignee ========================

    @Nested
    @DisplayName("getTicketsByAssignee Tests")
    class GetTicketsByAssigneeTests {

        @Test
        @DisplayName("Positive: Should return tickets for a valid assignee")
        void getTicketsByAssignee_ShouldReturnMatchingTickets() {
            Ticket ticket = TestDataFactory.buildSavedTicket();
            List<Ticket> tickets = Collections.singletonList(ticket);
            when(ticketRepository.findByAssignee("admin_user")).thenReturn(tickets);

            List<Ticket> result = ticketService.getTicketsByAssignee("admin_user");

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("admin_user", result.get(0).getAssignee());
            verify(ticketRepository, times(1)).findByAssignee("admin_user");
        }

        @Test
        @DisplayName("Negative: Should return empty list for non-existent assignee")
        void getTicketsByAssignee_ShouldReturnEmptyList_WhenAssigneeNotFound() {
            when(ticketRepository.findByAssignee("nobody")).thenReturn(Collections.emptyList());

            List<Ticket> result = ticketService.getTicketsByAssignee("nobody");

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Edge: Should handle null assignee")
        void getTicketsByAssignee_ShouldHandleNullAssignee() {
            when(ticketRepository.findByAssignee(null)).thenReturn(Collections.emptyList());

            List<Ticket> result = ticketService.getTicketsByAssignee(null);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Positive: Should return multiple tickets for same assignee")
        void getTicketsByAssignee_ShouldReturnMultipleTickets() {
            Ticket ticket1 = TestDataFactory.buildSavedTicket();
            Ticket ticket2 = TestDataFactory.buildSavedTicket();
            ticket2.setId(10L);
            when(ticketRepository.findByAssignee("admin_user")).thenReturn(Arrays.asList(ticket1, ticket2));

            List<Ticket> result = ticketService.getTicketsByAssignee("admin_user");

            assertEquals(2, result.size());
        }
    }
}
