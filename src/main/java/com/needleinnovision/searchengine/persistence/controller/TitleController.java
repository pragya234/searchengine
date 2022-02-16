package com.needleinnovision.searchengine.persistence.controller;

import com.needleinnovision.searchengine.dbservice.impl.SearchDbServiceImpl;
import com.needleinnovision.searchengine.dbservice.impl.SearchDbServiceImplBatch;
import com.needleinnovision.searchengine.persistence.entity.Listing;
import com.needleinnovision.searchengine.persistence.entity.Rating;
import com.needleinnovision.searchengine.persistence.entity.Titles;
import com.needleinnovision.searchengine.persistence.repository.TitlesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/title")
public class TitleController {
    private final SearchDbServiceImpl searchDbServiceImpl;
    private final SearchDbServiceImplBatch searchDbServiceImplBatch;
    @Autowired
    private final TitlesRepository titlesRepository;

    public TitleController(SearchDbServiceImpl searchDbServiceImpl, SearchDbServiceImplBatch searchDbServiceImplBatch, TitlesRepository titlesRepository) {
        this.searchDbServiceImpl = searchDbServiceImpl;
        this.searchDbServiceImplBatch = searchDbServiceImplBatch;
        this.titlesRepository = titlesRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Void> test(){
        Rating rating= new Rating();
        rating.setId(1);
        rating.setName("PG-13");
       // Listing listing1 = new Listing();
       // listing1.setId(1);
       // listing1.setName("TV Dramas");
        //Listing listing2 = new Listing();
       // listing2.setId(2);
       // listing1.setName("TV Horror");
      //  List<Listing> listingList = new ArrayList<>();
       // listingList.add(listing1);
        //listingList.add(listing2);
        Titles title = new Titles();
        title.setShowId("s7");
        title.setTitle("My Little Pony: A New Generation3");
        title.setDirector("Vanessa Hudgens3");
        title.setRating(rating);
        //title.setListing(listingList);
        titlesRepository.save(title);
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value= "/test2", method = RequestMethod.GET)
    public ResponseEntity<Void> test2(){
        Rating rating= new Rating();
        rating.setId(1);
        rating.setName("TV-MA");
        Listing listing1 = new Listing();
        listing1.setId(1);
        listing1.setName("TV Dramas");
        Listing listing2 = new Listing();
        listing2.setId(2);
        listing2.setName("TV Horror");
        List<Listing> listingList = new ArrayList<>();
        listingList.add(listing1);
        listingList.add(listing2);
        Titles title = new Titles();
        title.setShowId("s3");
        title.setTitle("My Little Pony: A New Generation2");
        title.setDirector("Vanessa Hudgens2");
        title.setRating(rating);
        title.setListing(listingList);
        titlesRepository.save(title);
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value= "/test3", method = RequestMethod.DELETE)
    public ResponseEntity<Void> test3(){
        titlesRepository.deleteById("s4");
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value= "/test4", method = RequestMethod.GET)
    public ResponseEntity<Titles> test4(){
        Optional<Titles> titlesOptional = titlesRepository.findById("s5");
        Titles titles = titlesOptional.get();
        return ResponseEntity.ok(titles);
    }

    @RequestMapping(value= "/test5", method = RequestMethod.GET)
    public ResponseEntity<Void> test5(){
      searchDbServiceImpl.persistInDb();
      return ResponseEntity.ok().build();
    }
    @RequestMapping(value= "/test6", method = RequestMethod.GET)
    public ResponseEntity<Void> test6(){
        searchDbServiceImplBatch.persistInDb();
        return ResponseEntity.ok().build();
    }
}
