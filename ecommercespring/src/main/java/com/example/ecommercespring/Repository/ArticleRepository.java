package com.example.ecommercespring.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommercespring.models.Articles;

public interface ArticleRepository extends JpaRepository<Articles, Long> {
    List<Articles> findByPublished(boolean published);
    Optional<Articles> findByTitle(String title);


    List<Articles> findByTitleContainingIgnoreCase(String title);
}