package org.chessclub.model;

import org.chessclub.enums.UserRole;

import java.time.LocalDate;

public class MemberUser extends User {

    private int rating;
    private final LocalDate joinDate;
    private boolean membershipActive;

    public MemberUser(long id, String fullName, String email, int rating, LocalDate joinDate) {
        super(id, fullName, email, UserRole.MEMBER);
        if (rating < 0) {
            throw new IllegalArgumentException("Рейтинг не может быть отрицательным: " + rating);
        }
        this.rating = rating;
        this.joinDate = joinDate;
        this.membershipActive = true;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating < 0) {
            throw new IllegalArgumentException("Рейтинг не может быть отрицательным: " + rating);
        }
        this.rating = rating;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public boolean isMembershipActive() {
        return membershipActive;
    }

    public void setMembershipActive(boolean membershipActive) {
        this.membershipActive = membershipActive;
    }

    @Override
    public String describePermissions() {
        return "Участие в турнирах и просмотр своей статистики";
    }
}
