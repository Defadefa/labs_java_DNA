package org.chessclub.service;

import org.chessclub.model.MemberUser;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

public class MembershipQueueService {

    private final Queue<MemberUser> applications = new ArrayDeque<>();

    public void submit(MemberUser applicant) {
        if (applicant == null) {
            throw new IllegalArgumentException("Заявка не может быть пустой");
        }
        applications.offer(applicant);
    }

    public Optional<MemberUser> approveNext() {
        return Optional.ofNullable(applications.poll());
    }

    public Optional<MemberUser> peekNext() {
        return Optional.ofNullable(applications.peek());
    }

    public int pendingCount() {
        return applications.size();
    }
}
