package com.pacman.uberreviewservice.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

//-> @Entity will work for java logic if you give some name in entity without using @Table(name = "bookingReview") than it will show that name other wise @Table(name = "bookingReview") overrides @entity name
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookingReview")//only work for database
public class Review extends BaseModel {


    @Column(nullable = false) //->this column will never be null
    String content;

    Double rating;

    public String toString() {
        return "Review:" + this.content + " " + this.rating + " " + this.cratedAt + " " + this.updatedAt;
    }
}
//if we create new Review(content,rating) don't need to pass the id because spring will automatically handle it for its annotations of being primary key