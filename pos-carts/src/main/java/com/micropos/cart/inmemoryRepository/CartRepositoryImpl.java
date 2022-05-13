package com.micropos.cart.inmemoryRepository;

import com.micropos.cart.model.Cart;
import com.micropos.cart.model.Item;
import com.micropos.dto.ProductDto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Repository
public class CartRepositoryImpl implements CartRepository {

    private Map<String, Item> cartTable = new ConcurrentHashMap<>();
    private List<Cart> carts = new ArrayList<>();

    @Override
    public List<Cart> findAllCarts() {
        return this.carts;
    }

    @Override
    public Cart findCartById(int id) {
        for (Cart cart : carts) {
            if (cart.getId() == id) {
                return cart;
            }
        }
        return null;
    }

    @Override
    public List<Item> findItemsOfCart(int id) {
        Cart cart = this.findCartById(id);
        if (cart != null) {
            return cart.getItems();
        } else {
            return null;
        }
    }

    @Override
    public boolean saveCart(Cart cart) {
        // 自动生成ID
        cart.setId(carts.size());
        cart.setItems(new ArrayList<>());
        return carts.add(cart);
    }

    @Override
    public boolean addItemToCart(int cartId, Item item) {
        Cart cart = this.findCartById(cartId);
        if(updateItemOfCart(cartId, item)){
            return true;
        }
        return cart.addItem(item);
    }

    @Override
    public boolean updateItemOfCart(int cartId, Item item) {
        Cart cart = this.findCartById(cartId);
        int tag = -1;
        for(int i=0;i<cart.getItems().size();i++){
            if(cart.getItems().get(i).getProductId().equals(item.getProductId())){
                tag=i;
                break;
            }
        }
        if(tag == -1){
            return false;
        }else{
            cart.getItems().set(tag, item);
            return true;
        }

    }


}
