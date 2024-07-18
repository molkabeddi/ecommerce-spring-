package com.example.ecommercespring.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommercespring.models.Articles;
import com.example.ecommercespring.Repository.ArticleRepository;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class ArticleController {

    @Autowired
    ArticleRepository articleRepository;

    @GetMapping("/articles")
    public ResponseEntity<List<Articles>> getAllArticles(@RequestParam(required = false) String title) {
        try {
            List<Articles> articles = new ArrayList<Articles>();

            if (title == null)
                articleRepository.findAll().forEach(articles::add);
            else
                articleRepository.findByTitleContainingIgnoreCase(title).forEach(articles::add);

            if (articles.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(articles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/articles/{id}")
    public ResponseEntity<Articles> getArticleById(@PathVariable("id") long id) {
        Optional<Articles> articleData = articleRepository.findById(id);

        if (articleData.isPresent()) {
            return new ResponseEntity<>(articleData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/articles")
    public ResponseEntity<Articles> createArticle(@RequestBody Articles article) {
        try {
            Articles _article = articleRepository
                    .save(new Articles(article.getTitle(), article.getContent(), false));
            return new ResponseEntity<>(_article, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/articles/{id}")
    public ResponseEntity<Articles> updateArticle(@PathVariable("id") long id, @RequestBody Articles article) {
        Optional<Articles> articleData = articleRepository.findById(id);

        if (articleData.isPresent()) {
            Articles _article = articleData.get();
            _article.setTitle(article.getTitle());
            _article.setContent(article.getContent());
            _article.setPublished(article.isPublished());
            return new ResponseEntity<>(articleRepository.save(_article), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/articles/{id}")
    public ResponseEntity<HttpStatus> deleteArticle(@PathVariable("id") long id) {
        try {
            articleRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/articles")
    public ResponseEntity<HttpStatus> deleteAllArticles() {
        try {
            articleRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/articles/published")
    public ResponseEntity<List<Articles>> findByPublished() {
        try {
            List<Articles> articles = articleRepository.findByPublished(true);

            if (articles.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(articles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}