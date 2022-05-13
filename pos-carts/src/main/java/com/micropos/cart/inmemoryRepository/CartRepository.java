package com.micropos.cart.inmemoryRepository;

import com.micropos.cart.model.Cart;
import com.micropos.cart.model.Item;
import com.micropos.dto.ProductDto;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface CartRepository {
    List<Cart> findAllCarts();
    Cart findCartById(int id);
    List<Item> findItemsOfCart(int id);
    boolean saveCart(Cart cart);
    boolean addItemToCart(int cartId, Item item);
    boolean updateItemOfCart(int cartId, Item item);


}
