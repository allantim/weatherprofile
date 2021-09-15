package org.tim.weathertracker.core.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.Set;

/**
 * A user can have multiple profiles.
 * There is a one to Many from User to Profile
 */
@Entity( name = "user_profile")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"nickname"})
@ToString(exclude = "userData")
public class UserProfile {

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private UserData userData;

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="nickname")
    private String nickname;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "profile_city",
        joinColumns = @JoinColumn(name = "user_profile_id"),
        inverseJoinColumns = @JoinColumn(name = "city_name"))
    private Set<CityWeather> cityWeathers;

    public void dismissUserData() {
        this.userData.dismissUserProfileChild(this);
        this.userData = null;
    }

}
