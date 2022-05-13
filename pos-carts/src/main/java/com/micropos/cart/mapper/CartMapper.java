package com.micropos.cart.mapper;

import com.micropos.dto.CartDto;
import com.micropos.dto.CartItemDto;
import com.micropos.cart.model.Cart;
import com.micropos.cart.model.Item;
import com.micropos.dto.ProductDto;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mapper
public interface CartMapper {
    Collection<CartDto> toCartDtos(Collection<Cart> carts);

    Collection<Cart> toCarts(Collection<CartDto> cartDtos);

    default Cart toCart(CartDto cartDto) {
        Cart cart = new Cart();
        cart.setId(cartDto.getId());
        cart.setItems(toItems(cartDto.getItems(), cartDto));
        return cart;

    }

    default CartDto toCartDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setId(cart.getId());
        cartDto.setItems(toItemDtos(cart.getItems()));
        return cartDto;
    }

    default List<Item> toItems(List<CartItemDto> itemDtos, CartDto cartDto) {
        if (itemDtos == null || itemDtos.isEmpty()) {
            return null;
        }
        List<Item> list = new ArrayList<>(itemDtos.size());
        for (CartItemDto itemDto : itemDtos) {
            list.add(toItem(itemDto, cartDto));
        }

        return list;
    }

    default Item toItem(CartItemDto itemDto, CartDto cartDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setCartId(cartDto.getId());
        item.setProductId(itemDto.getProduct().getId());
        item.setProductName(itemDto.getProduct().getName());
        item.setQuantity(itemDto.getAmount());
        item.setUnitPrice(itemDto.getProduct().getPrice());
        return item;
    }

    default List<CartItemDto> toItemDtos(List<Item> items) {
        if (items == null || items.isEmpty()) {
            return null;
        }
        List<CartItemDto> list = new ArrayList<>(items.size());
        for (Item item : items) {
            list.add(toItemDto(item));
        }

        return list;
    }

    default CartItemDto toItemDto(Item item) {

        return new CartItemDto().id(item.getId())
                .amount(item.getQuantity())
                .product(getProductDto(item));
    }

    default ProductDto getProductDto(Item item) {
        return new ProductDto().id(item.getProductId())
                .name(item.getProductName())
                .price(item.getUnitPrice());
    }

}
