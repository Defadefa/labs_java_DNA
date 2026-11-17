package org.chessclub.service;

import org.chessclub.model.MemberUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class MembershipQueueServiceTest {

    private MemberUser member(long id, String name) {
        return new MemberUser(id, name, "m" + id + "@club.ru", 1200, LocalDate.now());
    }

    @Test
    void applicationsAreProcessedInOrder() {
        MembershipQueueService service = new MembershipQueueService();
        service.submit(member(1L, "Первый"));
        service.submit(member(2L, "Второй"));
        Assertions.assertEquals("Первый", service.approveNext().get().getFullName());
        Assertions.assertEquals("Второй", service.approveNext().get().getFullName());
    }

    @Test
    void emptyQueueReturnsEmpty() {
        MembershipQueueService service = new MembershipQueueService();
        Assertions.assertTrue(service.approveNext().isEmpty());
    }

    @Test
    void nullApplicationIsRejected() {
        MembershipQueueService service = new MembershipQueueService();
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.submit(null));
    }

    @Test
    void pendingCountDecreasesAfterApproval() {
        MembershipQueueService service = new MembershipQueueService();
        service.submit(member(1L, "А"));
        service.submit(member(2L, "Б"));
        service.approveNext();
        Assertions.assertEquals(1, service.pendingCount());
    }
}
