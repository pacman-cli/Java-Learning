package com.pacman.uberreviewservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

//-> @Entity will work for java logic if you give some name in entity without using @Table(name = "bookingReview") than it will show that name other wise @Table(name = "bookingReview") overrides @entity name
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking_review")
//only work for database //@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //-> this will make single table all together //@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) //-> different tables for each class.this creates data redundancy.
@Inheritance(strategy = InheritanceType.JOINED)
public class Review extends BaseModel {


    @Column(nullable = false) //->this column will never be null
    String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    @JsonIgnore
    private Booking booking;

    private Double rating;

    //->CascadeType.ALL propagates all operations — including Hibernate-specific ones — from a parent to a child entity.
//    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
//    private Review review;//we have defined a 1:1 relationship between booking and review


    public String toString() {
        return "Review:" + this.content + " " + this.rating + " " + this.createdAt + " " + this.updatedAt;
    }
}
//if we create new Review(content,rating) don't need to pass the id because spring will automatically handle it for its annotations of being primary key