package com.micropos.cart.service;

import com.micropos.cart.model.Cart;
import com.micropos.cart.model.Item;
import com.micropos.dto.ProductDto;

import java.util.List;

public interface CartService {

    Cart add(Cart cart, Item item);

    List<Cart> getAllCarts();

    ProductDto findProductById(String productId);
}
