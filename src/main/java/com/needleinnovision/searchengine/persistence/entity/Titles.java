package com.needleinnovision.searchengine.persistence.entity;

import lombok.Data;


import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "title")
public class Titles {
    @Id
    @Column(name = "show_id")
    private String showId;
    @Column(name = "title_type")
    private String titleType;
    private String title;
    private String director;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "rating_id")
    private Rating rating;
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "title_listing",
            joinColumns = { @JoinColumn(name = "show_id") },
            inverseJoinColumns = { @JoinColumn(name = "listing_id") }
    )
    private List<Listing> listing;
}
