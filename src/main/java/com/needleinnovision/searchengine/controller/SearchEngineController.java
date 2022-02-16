package com.needleinnovision.searchengine.controller;

import com.needleinnovision.searchengine.models.Show;
import com.needleinnovision.searchengine.service.SearchEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/searchEngine")
public class SearchEngineController {
    private final SearchEngineService searchEngineService;
    @Autowired
    public SearchEngineController(SearchEngineService searchEngineService) {
        this.searchEngineService = searchEngineService;
    }
    @RequestMapping(value = "search", method = RequestMethod.GET)
    public ResponseEntity<List<Show>> search(@RequestParam(value = "type",required = false) String type, @RequestParam(value = "cast",required = false) String cast,
                                             @RequestParam(value = "director",required = false) String director, @RequestParam(value = "title",required = false) String title,
                                             @RequestParam(value = "listedIn",required = false) String listedIn,
                                             @RequestParam(value = "offset",required = false,defaultValue = "0")Integer offset,
                                             @RequestParam(value = "pageSize",required = false)Integer pageSize) {
        if(Objects.isNull(pageSize)){
            pageSize =Integer.MAX_VALUE;
        }
        List<Show> shows = searchEngineService.search(type, cast, director, title, listedIn,offset,pageSize);
        return ResponseEntity.ok().body(shows);

    }

    public ResponseEntity<Void> saveInDb(){
        return ResponseEntity.ok().build();
    }
}
