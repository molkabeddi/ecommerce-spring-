package com.example.ecommercespring.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommercespring.models.Articles;

public interface ArticleRepository extends JpaRepository<Articles, Long> {
    List<Articles> findByPublished(boolean published);

    List<Articles> findByTitleContainingIgnoreCase(String title);
}