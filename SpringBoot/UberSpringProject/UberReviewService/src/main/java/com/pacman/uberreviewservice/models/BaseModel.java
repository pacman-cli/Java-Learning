package com.pacman.uberreviewservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass // ✅ Tells JPA to include fields in child entities
public abstract class BaseModel {//this class will have all the bare minimum properties that any other class will be requiring //making abstract class because by doing this you cannot instantiate object out of it.
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    protected Long id;

    //this is to store  time and date both for that TIMESTAMP and because spring is going to take the works for that we don't have to pass this. this is the type of value I want to store
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate //->this annotation tells spring that only handle it for object creatio
    protected Date cratedAt;

    //this is to store  time and date both for that TIMESTAMP and because spring is going to take the works for that we don't have to pass this. this is the type of value I want to store
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate //->this annotation tells spring that only handle it for object update
    protected Date updatedAt;
}
