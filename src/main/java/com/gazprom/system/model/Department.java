package com.gazprom.system.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "department_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @ManyToOne(cascade = CascadeType.MERGE)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "unit_id")
    public Unit unit;

    public Department(String title, Unit unit) {
        this.unit = unit;
        this.title = title;
    }
}
