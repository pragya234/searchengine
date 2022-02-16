package com.needleinnovision.searchengine.service;

import com.needleinnovision.searchengine.dbservice.SearchDbService;
import com.needleinnovision.searchengine.models.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class SearchEngineService {
    private final SearchDbService searchDbService;

    @Autowired
    public SearchEngineService(SearchDbService searchDbService) {
        this.searchDbService = searchDbService;
    }

    public List<Show> search(String type, String cast, String director, String title, String listedIn,Integer offset,Integer pageSize) {

        // checking if all the query params are null.
        if(Objects.isNull(type) && Objects.isNull(cast) && Objects.isNull(director) && Objects.isNull(title) && Objects.isNull(listedIn))
             return new ArrayList<>();

        // fetching data from db service.
        List<Show> shows = searchDbService.filterWithAnd(type,cast,director,title,listedIn);

        // sending out the paginated result.
        List<Show> paginatedResult = new ArrayList<>();
        Integer limit = Math.min(offset+pageSize,shows.size());
        for(int i=offset;i<limit;i++){
            paginatedResult.add(shows.get(i));
        }
        return paginatedResult;
    }
}
