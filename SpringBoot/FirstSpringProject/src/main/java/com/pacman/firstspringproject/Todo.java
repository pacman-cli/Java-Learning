package com.pacman.firstspringproject;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Todo {
    @Id //->this makes id as primary key
    @GeneratedValue(strategy = GenerationType.SEQUENCE) //-> this works as auto increment
    @Column(nullable = false)//->this is not null
    private Long id;

    @Column
    private String title;

    @Column
    private boolean completed;

}
