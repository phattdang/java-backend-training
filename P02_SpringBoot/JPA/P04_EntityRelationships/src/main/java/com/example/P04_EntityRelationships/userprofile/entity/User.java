package com.example.P04_EntityRelationships.userprofile.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    // Inverse side: Profile.user owns the relationship and stores the foreign key.
    @OneToOne(mappedBy = "user")
    private Profile profile;

    public User(String name) {
        this.name = name;
    }

    public void attachProfile(Profile profile) {
        this.profile = profile;
        profile.setUser(this);
        // Synchronizes Java references only; the Profile owning side must still be persisted.
    }
}
