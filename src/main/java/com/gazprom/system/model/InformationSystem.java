package com.gazprom.system.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "information_system")
public class InformationSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "primary_admin_id")
    private User primaryAdmin;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "backup_admin_id")
    private User backupAdmin;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "system_privilege", joinColumns = @JoinColumn(name = "system_id"), inverseJoinColumns = @JoinColumn(name = "privilege_id"))
    private List<Privilege> privileges;

    public InformationSystem(String title, User owner, User primaryAdmin, User backupAdmin, List<Privilege> privileges) {
        this.title = title;
        this.owner = owner;
        this.primaryAdmin = primaryAdmin;
        this.backupAdmin = backupAdmin;
        this.privileges = privileges;
    }
}
