package org.tim.weathertracker.core.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.UUID;

@Entity(name = "user_data")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"userId"})
@ToString(exclude = "userProfiles")
public class UserData {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="user_id")
    @Type(type = "uuid-char")
    private UUID userId;
    @Column(name="name")
    private String name;
    @Column(name="email")
    private String email;

    @OneToMany(mappedBy="userData", cascade = CascadeType.ALL)
    private Set<UserProfile> userProfiles;

    public void addUserProfile(UserProfile userProfile) {
        // remove if it exists (nickname is used to identify duplicates)
        this.userProfiles.remove(userProfile);
        this.userProfiles.add(userProfile);
        userProfile.setUserData(this);
    }
}
