package com.example.ecommercespring.models;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "article_id", nullable = false)
    private Long articleId;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "user_id", nullable = false)
    private String userId; // Identifie l'utilisateur

    @Column(nullable = false)
    private String name; // Nom de l'article

    @Column(nullable = false)
    private double price; // Prix de l'article

    // Constructeur par défaut
    public CartItem() {
    }

    // Constructeur avec tous les champs nécessaires
    public CartItem(String userId, Long articleId, int quantity, String name, double price) {
        this.articleId = articleId;
        this.quantity = quantity;
        this.userId = userId;
        this.name = name;
        this.price = price;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    // Override toString pour le débogage
    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", articleId=" + articleId +
                ", quantity=" + quantity +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    // Override equals et hashCode pour la comparaison et le hachage
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return quantity == cartItem.quantity &&
                Double.compare(cartItem.price, price) == 0 &&
                Objects.equals(id, cartItem.id) &&
                Objects.equals(articleId, cartItem.articleId) &&
                Objects.equals(userId, cartItem.userId) &&
                Objects.equals(name, cartItem.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, articleId, quantity, userId, name, price);
    }
}
