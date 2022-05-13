package com.micropos.cart.controller;

import com.micropos.cart.model.Cart;
import com.micropos.cart.model.Item;
import com.micropos.cart.inmemoryRepository.CartRepository;
import com.micropos.controller.CartsApi;
import com.micropos.dto.CartDto;
import com.micropos.cart.mapper.CartMapper;
import com.micropos.dto.CartItemDto;
import com.micropos.dto.ProductDto;
import io.swagger.models.Response;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.micropos.cart.service.CartService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
public class CartController implements CartsApi {

    private CartService cartService;
    private CartMapper cartMapper;
    private CartRepository cartRepository;


    @Autowired
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @Autowired
    public void setCartMapper(CartMapper cartMapper) {
        this.cartMapper = cartMapper;
    }

    @Autowired
    public void setCartRepository(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public ResponseEntity<CartDto> createCart(
            @Parameter(name = "CartDto", description = "An empty cart", required = true, schema = @Schema(description = "")) @Valid @RequestBody CartDto cartDto
    ) {
        Cart cart = cartMapper.toCart(cartDto);
//        cart.setItems(new ArrayList<>());//这句话并不能使得items不为null
        cartRepository.saveCart(cart);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<List<CartDto>> listCarts() {
        List<Cart> carts = cartService.getAllCarts();
        Collection<CartDto> cartDtos = cartMapper.toCartDtos(carts);
        if (cartDtos.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(cartDtos), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<CartDto>>(new ArrayList<>(cartDtos), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CartDto> showCartById(
            @Parameter(name = "cartId", description = "The id of the Cart to retrieve", required = true, schema = @Schema(description = "")) @PathVariable("cartId") Integer cartId
    ) {
        Cart cart = cartRepository.findCartById(cartId);
        if (cart != null) {
            CartDto cartDto = cartMapper.toCartDto(cart);
            return new ResponseEntity<>(cartDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<CartDto> addItemToCart(
            @Parameter(name = "cartId", description = "The id of the cart to retrieve", required = true, schema = @Schema(description = "")) @PathVariable("cartId") Integer cartId,
            @Parameter(name = "CartItemDto", description = "The details of the Item.", required = true, schema = @Schema(description = "")) @Valid @RequestBody CartItemDto cartItemDto
    ) {
        Cart cart = cartRepository.findCartById(cartId);
        if (cart != null) {
            CartDto cartDto = cartMapper.toCartDto(cart);
            Item item = cartMapper.toItem(cartItemDto, cartDto);
            cartService.add(cart, item);
            cartDto.addItemsItem(cartItemDto);
            return new ResponseEntity<>(cartDto, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public ResponseEntity<CartItemDto> addProductToCart(
            @Parameter(name = "cartId", description = "The id of the cart to retrieve", required = true, schema = @Schema(description = "")) @PathVariable("cartId") Integer cartId,
            @Parameter(name = "productId", description = "The id of the product to retrieve", required = true, schema = @Schema(description = "")) @PathVariable("productId") String productId) {
        Cart cart = cartRepository.findCartById(cartId);
        ProductDto productDto = cartService.findProductById(productId);
        if (productDto != null) {
            Item item = new Item();
            item.setCartId(cart.getId());
            item.setProductId(productId);
            item.setProductName(productDto.getName());
            item.setQuantity(1);
            item.setUnitPrice(productDto.getPrice());
            cartRepository.addItemToCart(cart.getId(), item);
            CartItemDto cartItemDto = cartMapper.toItemDto(item);
            return new ResponseEntity<>(cartItemDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Object> changeQuantity(
            @Parameter(name = "cartId", description = "The id of the cart to retrieve", required = true, schema = @Schema(description = "")) @PathVariable("cartId") Integer cartId,
            @Parameter(name = "productId", description = "The id of the product to retrieve", required = true, schema = @Schema(description = "")) @PathVariable("productId") String productId,
            @Parameter(name = "body", description = "the number of changed quantity", schema = @Schema(description = "")) @Valid @RequestBody(required = false) Integer body
    ) {

        List<Item> list = cartRepository.findItemsOfCart(cartId);
        if (list == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            for (Item item : list) {
                if(item.getProductId().equals(productId)){
                    item.setQuantity(item.getQuantity()+body);
                    if(item.getQuantity()<0){
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }else if(item.getQuantity()>0){
                        return new ResponseEntity<>(item, HttpStatus.OK);
                    }else{
                        list.remove(item);
                        return new ResponseEntity<>(item, HttpStatus.OK);
                    }

                }
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
