package com.needleinnovision.searchengine.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;



@Data
@JsonPropertyOrder({ "showId", "type", "title", "director","cast","country","dateAdded","releaseYear","rating","duration","listedIn","description" })
public class Show {
    public String showId;
    public String type;
    public String title;
    public String director;
    public String cast;
    public String country;
    public String dateAdded;
    public String releaseYear;
    public String rating;
    public String duration;
    public String listedIn;
    public String description;
}
