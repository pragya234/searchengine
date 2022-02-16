package com.needleinnovision.searchengine.persistence.entity;
import lombok.Data;


import javax.persistence.*;

@Entity
@Data
@Table(name = "rating")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;
}
