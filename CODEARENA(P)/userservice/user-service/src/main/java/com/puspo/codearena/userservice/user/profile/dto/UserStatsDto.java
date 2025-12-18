package com.puspo.codearena.userservice.user.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatsDto {
    private Integer problemsSolved;
    private Integer totalSubmissions;
    private Integer ranking;
    private Double successRate;
}
