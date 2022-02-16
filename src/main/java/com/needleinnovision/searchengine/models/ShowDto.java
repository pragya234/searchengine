package com.needleinnovision.searchengine.models;

public class ShowDto {
    private String director;
    public ShowDto showMapper(Show show){
        this.director= show.getDirector();
        return this;
    }
}
