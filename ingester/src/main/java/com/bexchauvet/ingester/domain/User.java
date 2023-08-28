package com.bexchauvet.ingester.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    @Column(unique = true)
    private String username;
    private String password;
    private List<String> roles;
    @JsonManagedReference
    @Column(name = "taste_score")
    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TasteScore> tasteScore;
    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WishList> wishlists;

}
