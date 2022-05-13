package com.micropos.cart.service;

import com.micropos.cart.model.Cart;
import com.micropos.cart.model.Item;
//import com.micropos.cart.inmemoryRepository.ItemRepository;
import com.micropos.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import com.micropos.cart.inmemoryRepository.CartRepository;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private CartRepository cartRepository;
//    private CartMapper cartMapper;

    private static final String POS_PRODUCTS_URL = "http://pos-products/api";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public void setCartRepository(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }


    @Override
    public Cart add(Cart cart, Item item) {
        if (cart.addItem(item)){
            return cart;
        }
        return null;
    }

    @Override
    public List<Cart> getAllCarts() {
        return cartRepository.findAllCarts();
    }

    @Override
    public ProductDto findProductById(String productId) {
        ProductDto productDto = restTemplate.getForObject(POS_PRODUCTS_URL+"/products/{productId}", ProductDto.class, productId);
        return productDto;
    }
}
