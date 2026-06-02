package com.projects.diningreview.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="RESTAURANT")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="NAME")
    private String name;
    @Column(name="ZIP_CODE")
    private String zipCode;
    @Column(name="PEANUT_SCORE")
    private Float peanutScore;
    @Column(name="EGG_SCORE")
    private Float eggScore;
    @Column(name="DAIRY_SCORE")
    private Float dairyScore;
    @Column(name="OVERALL_SCORE")
    private Float overallScore;
}
