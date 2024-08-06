package com.example.ecommercespring.Security.services;

import com.example.ecommercespring.models.Articles;
import com.example.ecommercespring.models.CartItem;
import com.example.ecommercespring.Repository.CartItemRepository;
import com.example.ecommercespring.Repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ArticleRepository articleRepository;

    public List<CartItem> getCartItems(String userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public CartItem addToCart(String userId, Long articleId, int quantity) {
        Articles article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));

        // Créer l'objet CartItem avec toutes les informations nécessaires
        CartItem cartItem = new CartItem();
        cartItem.setUserId(userId);
        cartItem.setArticleId(articleId);
        cartItem.setQuantity(quantity);
        cartItem.setName(article.getTitle());
        cartItem.setPrice(article.getPrice());

        return cartItemRepository.save(cartItem);
    }

    public void removeFromCart(String userId, Long id) {
        cartItemRepository.deleteByIdAndUserId(id, userId);
    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
