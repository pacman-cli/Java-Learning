package com.pacman.uberreviewservice.models;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity //-> this will work for java logic
@Table(name = "bookingReview") //only work for database
public class Review {
  @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) Integer id;

  @Column(nullable = false) //->this column will never be null
  String content;

  Double rating;

    //this is to store  time and date both for that TIMESTAMP and because spring is going to take the works for that we don't have to pass this. this is the type of value I want to store
  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreatedDate //->this annotation tells spring that only handle it for object creation
  Date cratedAt;

    //this is to store  time and date both for that TIMESTAMP and because spring is going to take the works for that we don't have to pass this. this is the type of value I want to store
  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @LastModifiedDate //->this annotation tells spring that only handle it for object update
  Date updatedAt;
}
//if we create new Review(content,rating) don't need to pass the id because spring will automatically handle it for its annotations of being primary key