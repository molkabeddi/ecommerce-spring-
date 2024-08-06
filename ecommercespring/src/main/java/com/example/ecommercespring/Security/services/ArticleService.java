package com.example.ecommercespring.Security.services;

import com.example.ecommercespring.Repository.ArticleRepository;
import com.example.ecommercespring.models.Articles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

    @Autowired
    private static ArticleRepository articleRepository;

    public static Articles saveArticle(Articles article) {
        return articleRepository.save(article);
    }
}