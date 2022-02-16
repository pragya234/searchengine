package com.needleinnovision.searchengine.persistence.repository;
import com.needleinnovision.searchengine.persistence.entity.Titles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TitlesRepository extends JpaRepository<Titles, String> {

}
