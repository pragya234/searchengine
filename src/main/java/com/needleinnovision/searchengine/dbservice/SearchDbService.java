package com.needleinnovision.searchengine.dbservice;

import com.needleinnovision.searchengine.models.Show;

import java.util.List;

public interface SearchDbService {
    List<Show> filterWithAnd(String type, String cast, String director, String title, String listedIn);
    //void saveShowList(List<ShowEntity> showEntities);
  }
