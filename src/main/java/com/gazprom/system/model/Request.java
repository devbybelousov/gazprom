package com.gazprom.system.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "request")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "request_id")
    private Long id;

    @Column(name = "status")
    private String status;

    @Column(name = "filing_date")
    private Timestamp filingDate;

    @Column(name = "expiry_date")
    private Timestamp expiryDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "system_id")
    private InformationSystem informationSystem;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "request_history", joinColumns = @JoinColumn(name = "request_id"), inverseJoinColumns = @JoinColumn(name = "history_id"))
    private Set<History> history;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "request_user", joinColumns = @JoinColumn(name = "request_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users;


    public Request(String status, Timestamp filingDate, Timestamp expiryDate, User user, InformationSystem system) {
        this.status = status;
        this.filingDate = filingDate;
        this.informationSystem = system;
        this.expiryDate = expiryDate;
    }
}
