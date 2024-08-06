package com.example.ecommercespring.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.ecommercespring.Security.services.CartService;
import com.example.ecommercespring.models.CartItem;
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
import com.example.ecommercespring.Security.services.ArticleService;

import com.example.ecommercespring.models.Articles;
import com.example.ecommercespring.Repository.ArticleRepository;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    ArticleRepository articleRepository;

    @GetMapping
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

    @GetMapping("/{id}")
    public ResponseEntity<Articles> getArticleById(@PathVariable("id") long id) {
        Optional<Articles> articleData = articleRepository.findById(id);

        if (articleData.isPresent()) {
            return new ResponseEntity<>(articleData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Articles> createArticle(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("published") boolean published,
            @RequestParam("category") String category,
            @RequestParam("price") double price,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            String imagePath = image != null ? uploadImage(image) : null;
            Articles newArticle = new Articles(title, content, published, category, price, imagePath);
            Articles _article = articleRepository.save(newArticle);
            return new ResponseEntity<>(_article, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String uploadImage(MultipartFile image) throws IOException {
        String uploadDir = "uploads/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = image.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Articles> updateArticle(@PathVariable("id") long id, @RequestBody Articles article) {
        Optional<Articles> articleData = articleRepository.findById(id);

        if (articleData.isPresent()) {
            Articles _article = articleData.get();
            _article.setTitle(article.getTitle());
            _article.setContent(article.getContent());
            _article.setPublished(article.isPublished());
            _article.setCategory(article.getCategory());
            _article.setPrice(article.getPrice());
            _article.setImage(article.getImage());
            return new ResponseEntity<>(articleRepository.save(_article), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteArticle(@PathVariable("id") long id) {
        try {
            articleRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAllArticles() {
        try {
            articleRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/title/{title}")
    public ResponseEntity<Void> deleteArticleByTitle(@PathVariable("title") String title) {
        Optional<Articles> article = articleRepository.findByTitle(title);
        if (article.isPresent()) {
            articleRepository.delete(article.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/published")
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

