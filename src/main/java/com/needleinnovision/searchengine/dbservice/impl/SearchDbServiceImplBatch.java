package com.needleinnovision.searchengine.dbservice.impl;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
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
import java.util.*;

@Repository
public class SearchDbServiceImplBatch {
    private final TitlesRepository titlesRepository;
    private final ListingRepository listingRepository;
    private final RatingRepository ratingRepository;
    private List<Show> allShows;
    private static int BATCH_SIZE=1000;

    public SearchDbServiceImplBatch(TitlesRepository titlesRepository, ListingRepository listingRepository, RatingRepository ratingRepository) throws IOException {
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

    public void persistInDb() {
        Map<String, Rating> stringToRatingMap = persistRatingInDb();
        Map<String, Listing> stringToListingMap = persistListingInDb();
        List<Titles> titlesList = new ArrayList<>();
        for (Show show : allShows) {
            Titles title = getTitlesObjectFromShowObject(show, stringToRatingMap, stringToListingMap);
            titlesList.add(title);
            if(titlesList.size()==BATCH_SIZE){
                titlesRepository.saveAll(titlesList);
                titlesList = new ArrayList<>();
            }
        }
        titlesRepository.saveAll(titlesList);
    }

    public Map<String, Rating> persistRatingInDb() {
        Set<String> ratingNameSet = new HashSet<>();
        for (Show show : allShows) {
            String ratingName = show.getRating();
            ratingNameSet.add(ratingName);
        }
        List<Rating> ratingList = new ArrayList<>();
        for (String ratingName : ratingNameSet) {
            Rating rating = getRatingObjectFromRatingName(ratingName);
            ratingList.add(rating);
        }
        ratingRepository.saveAll(ratingList);
        List<Rating> ratingListObject = ratingRepository.findAll();
        Map<String, Rating> ratingMap = new HashMap<>();
        for (Rating rating : ratingListObject) {
            ratingMap.put(rating.getName(), rating);
        }
        return ratingMap;
    }

    public Map<String, Listing> persistListingInDb() {
        Set<String> listingNameSet = new HashSet<>();
        for (Show show : allShows) {
            String[] listingNames = show.getListedIn().split(",");
            for (int i = 0; i < listingNames.length; i++) {
                listingNameSet.add(listingNames[i]);
            }
        }
        List<Listing> listingList = new ArrayList<>();
        for (String listingName : listingNameSet) {
            Listing listing = getListingObjectFromListingName(listingName);
            listingList.add(listing);
        }
        listingRepository.saveAll(listingList);
        List<Listing> listOfListing = listingRepository.findAll();
        Map<String, Listing> stringListingMap = new HashMap<>();
        for (Listing listing : listOfListing) {
            stringListingMap.put(listing.getName(), listing);
        }
        return stringListingMap;
    }

    private Titles getTitlesObjectFromShowObject(Show show, Map<String, Rating> stringToRatingMap, Map<String, Listing> stringToListingMap){
        Titles titles = new Titles();
        titles.setShowId(show.getShowId());
        titles.setTitle(show.getTitle());
        titles.setDirector(show.getDirector());
        titles.setTitleType(show.getType());
        titles.setRating(stringToRatingMap.get(show.getRating()));
        String[] listingNames = show.getListedIn().split(",");
        List<Listing> listOfListings = new ArrayList<>();
        for(int i = 0; i<listingNames.length; i++) {
            Listing listing = stringToListingMap.get(listingNames[i]);
            listOfListings.add(listing);
        }
        titles.setListing(listOfListings);
        return titles;
    }

    private Rating getRatingObjectFromRatingName(String ratingName){
        Rating rating = ratingRepository.findByName(ratingName);
        if(rating!=null){
            return rating;
        }
        rating = new Rating();
        rating.setName(ratingName);
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
