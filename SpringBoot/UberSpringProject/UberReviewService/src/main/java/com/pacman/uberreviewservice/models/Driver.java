package com.pacman.uberreviewservice.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Driver extends BaseModel {
  private String name;

  @Column(nullable = false, unique = true)
  private String licenceNumber;

  private String phoneNumber;

  // 1 driver can have many bookings so this relationship would be 1:n ////this is driver class so
  // one part is driver many part is booking
  // @OneToMany is Lazy by default, so you don’t need to specify fetch = FetchType.LAZY explicitly.
  // @Fetch(FetchMode.SUBSELECT) is Hibernate-specific. It helps with N+1 issues, but you might not
  // need it unless you hit performance problems.
  @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonIgnore
  private List<Booking> bookings;
}
// Lazy Loading is a design pattern that we use to defer the initialization of an object as long as
// it’s possible.
