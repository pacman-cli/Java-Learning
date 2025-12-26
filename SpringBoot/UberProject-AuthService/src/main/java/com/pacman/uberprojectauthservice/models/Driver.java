package com.pacman.uberprojectauthservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler","bookings"})
public class Driver extends BaseModel {
  private String name;

  @Column(nullable = false, unique = true)
  private String licenceNumber;

  private String phoneNumber;

  @OneToMany(mappedBy = "driver")
  @Fetch(FetchMode.SUBSELECT)
  private List<Booking> bookings;
}

