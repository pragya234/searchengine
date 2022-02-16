package com.needleinnovision.searchengine.persistence.entity;

import lombok.Data;


import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "listing")
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToMany(mappedBy = "listing")
    private List<Titles> titlesList;
}
