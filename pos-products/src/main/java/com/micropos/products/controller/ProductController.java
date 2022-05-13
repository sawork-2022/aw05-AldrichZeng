package com.micropos.products.controller;

import com.micropos.controller.ProductsApi;
import com.micropos.dto.ProductDto;
import com.micropos.products.mapper.ProductMapper;
import com.micropos.products.model.Product;
import com.micropos.products.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("api")
public class ProductController implements ProductsApi {

    private final ProductMapper productMapper;

    private final ProductService productService;


    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productMapper = productMapper;
        this.productService = productService;
    }

    @Override
    public ResponseEntity<List<ProductDto>> listProducts() {
        // 获取List<Product>
        List<Product> products = this.productService.products();
        // 转化为List<ProductDto>
        Collection<ProductDto> productsCollection = productMapper.toProductsDto(products);
        List<ProductDto> productDtos = new ArrayList<>(productsCollection);
        if (productDtos.isEmpty()) {
            // 空，则返回404 Not Found.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // 正确，则返回200 OK
        return new ResponseEntity<>(productDtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProductDto> showProductById(String productId) {
        // 获取Product
        Product product = this.productService.getProduct(productId);

        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // 转化为ProductDto
        ProductDto productDto = productMapper.toProductDto(product);
        if (productDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productDto, HttpStatus.OK);

    }


}
