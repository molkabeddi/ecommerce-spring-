package com.example.ecommercespring.controllers;

import com.example.ecommercespring.models.CartItem;
import com.example.ecommercespring.Security.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public List<CartItem> getCartItems(@PathVariable String userId) {
        return cartService.getCartItems(userId);
    }

    @PostMapping("/add/{userId}")
    public ResponseEntity<CartItem> addToCart(@PathVariable String userId, @RequestBody AddToCartRequest request) {
        if (request.getArticleId() == null || request.getQuantity() <= 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        try {
            CartItem cartItem = cartService.addToCart(userId, request.getArticleId(), request.getQuantity());
            return new ResponseEntity<>(cartItem, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/remove/{userId}/{id}")
    public ResponseEntity<Void> removeFromCart(@PathVariable String userId, @PathVariable Long id) {
        cartService.removeFromCart(userId, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    public static class AddToCartRequest {
        private Long articleId;
        private int quantity;
        private String name; // Ajoutez ce champ
        private double price; // Ajoutez ce champ

        // Getters et Setters
        public Long getArticleId() {
            return articleId;
        }

        public void setArticleId(Long articleId) {
            this.articleId = articleId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }

   }