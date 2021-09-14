package org.tim.weathertracker.core.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Entity(name = "user_data")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserData {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="user_id")
    @Type(type = "uuid-char")
    private UUID userId;
    @Column(name="name")
    private String name;
    @Column(name="email")
    private String email;
}
