package com.projects.diningreview.models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="DISPLAY_NAME", unique=true)
    private String displayName;
    @Column(name="CITY")
    private String city;
    @Column(name="STATE")
    private String state;
    @Column(name="ZIP_CODE")
    private String zipCode;
    @Column(name="PEANUT_ALLERGY")
    private Boolean peanutAllergy;
    @Column(name="EGG_ALLERGY")
    private Boolean eggAllergy;
    @Column(name="DAIRY_ALLERGY")
    private Boolean dairyAllergy;
}
