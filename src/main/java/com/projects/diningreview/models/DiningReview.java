package com.projects.diningreview.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="DINING_REVIEW")
public class DiningReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="RESTAURANT_ID")
    private Long restaurantId;

    @Column(name="DISPLAY_NAME")
    private String displayName;
    @Column(name="PEANUT_SCORE")
    private Integer peanutScore;
    @Column(name="EGG_SCORE")
    private Integer eggScore;
    @Column(name="DAIRY_SCORE")
    private Integer dairyScore;
    @Column(name="COMMENTARY")
    private String commentary;
    @Enumerated(EnumType.STRING)
    @Column(name="REVIEW_STATUS")
    private ReviewStatus reviewStatus;
}
