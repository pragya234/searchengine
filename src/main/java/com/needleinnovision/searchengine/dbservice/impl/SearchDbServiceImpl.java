package com.needleinnovision.searchengine.dbservice.impl;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.needleinnovision.searchengine.dbservice.SearchDbService;
import com.needleinnovision.searchengine.models.Show;
import com.needleinnovision.searchengine.persistence.entity.Listing;
import com.needleinnovision.searchengine.persistence.entity.Rating;
import com.needleinnovision.searchengine.persistence.entity.Titles;
import com.needleinnovision.searchengine.persistence.repository.ListingRepository;
import com.needleinnovision.searchengine.persistence.repository.RatingRepository;
import com.needleinnovision.searchengine.persistence.repository.TitlesRepository;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class SearchDbServiceImpl implements SearchDbService {
    private final TitlesRepository titlesRepository;
    private final ListingRepository listingRepository;
    private final RatingRepository ratingRepository;
    private List<Show> allShows;
    public SearchDbServiceImpl(TitlesRepository titlesRepository, ListingRepository listingRepository, RatingRepository ratingRepository) throws IOException {
        this.titlesRepository = titlesRepository;
        this.listingRepository = listingRepository;
        this.ratingRepository = ratingRepository;
        File csvFile = new File("data.csv");

        CsvMapper csvMapper = new CsvMapper();

        CsvSchema csvSchema = csvMapper
                .typedSchemaFor(Show.class)
                .withHeader()
                .withColumnSeparator(',')
                .withComments();

        MappingIterator<Show> showIterator = csvMapper
                .readerWithTypedSchemaFor(Show.class)
                .with(csvSchema)
                .readValues(csvFile);
        allShows = showIterator.readAll();
    }
    @Override
    public List<Show> filterWithAnd(String type, String cast, String director, String title, String listedIn) {
       List<Show> filteredShow =
               allShows.stream().filter(show -> {
                   if(Objects.isNull(type))
                       return true;
                   if(show.getType().toLowerCase(Locale.ROOT).contains(type.toLowerCase(Locale.ROOT)))
                       return true;
                   return false;
               }).filter(show -> {
                   if(Objects.isNull(title))
                       return true;
                   if(show.getTitle().toLowerCase(Locale.ROOT).contains(title.toLowerCase(Locale.ROOT)))
                       return true;
                   return false;
               }).filter(show -> {
                   if(Objects.isNull(cast))
                       return true;
                   if(show.getCast().toLowerCase(Locale.ROOT).contains(cast.toLowerCase(Locale.ROOT)))
                       return true;
                   return false;
               }).filter(show -> {
                   if(Objects.isNull(director))
                       return true;
                   if(show.getDirector().toLowerCase(Locale.ROOT).contains(director.toLowerCase(Locale.ROOT)))
                       return true;
                   return false;
               }).filter(show -> {
                   if(Objects.isNull(listedIn))
                       return true;
                   if(show.getListedIn().toLowerCase(Locale.ROOT).contains(listedIn.toLowerCase(Locale.ROOT)))
                       return true;
                   return false;
               }).collect(Collectors.toList());
        return filteredShow;
    }


    public void persistInDb(){
        for(Show show: allShows){
            Titles title = getTitlesObjectFromShowObject(show);
            System.out.println(title);
            titlesRepository.save(title);
        }

    }

    private Titles getTitlesObjectFromShowObject(Show show){
       Titles titles = new Titles();
       titles.setShowId(show.getShowId());
       titles.setTitle(show.getTitle());
       titles.setDirector(show.getDirector());
       titles.setTitleType(show.getType());
       titles.setRating(getRatingObjectFromRatingName(show.getRating()));
      // System.out.println("rating is: "+ show.getRating());
       String[] listingNames = show.getListedIn().split(",");
       List<Listing> listOfListings = new ArrayList<>();
       for(int i = 0; i<listingNames.length; i++) {
        Listing listing = getListingObjectFromListingName(listingNames[i]);
        listOfListings.add(listing);
       }
        titles.setListing(listOfListings);
       return titles;
    }

    private Rating getRatingObjectFromRatingName(String ratingName){
        Rating rating = ratingRepository.findByName(ratingName);
        if(rating!=null){
            //System.out.println(rating);
            return rating;
        }
        rating = new Rating();
        rating.setName(ratingName);
       // System.out.println(rating);
        return rating;
    }

    private Listing getListingObjectFromListingName(String listingName){
        Listing listing = listingRepository.findByName(listingName);
        if(listing!= null){
            return listing;
        }
        listing = new Listing();
        listing.setName(listingName);
        return listing;
    }
}
