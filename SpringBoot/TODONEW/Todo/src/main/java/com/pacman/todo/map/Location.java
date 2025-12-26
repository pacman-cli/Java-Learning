package com.pacman.todo.map;

import com.pacman.todo.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double lat;
    private Double lng;
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
