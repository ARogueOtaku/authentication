package com.arogueotaku.authentication.models;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "refreshtoken_unique_constraint", columnNames = "refreshToken"))
@SuppressWarnings(value = "unused")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private long userId;

    public Session(){
    }

    public Session(String refreshToken, User user) {
        this.refreshToken = refreshToken;
        this.userId = user.getId();
    }

    public long getId() {
        return id;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public long getUserId() {
        return userId;
    }
}
