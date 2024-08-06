package com.example.ecommercespring.Repository;

import com.example.ecommercespring.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(String userId);
    void deleteByIdAndUserId(Long id, String userId);
    void deleteByUserId(String userId);
}
