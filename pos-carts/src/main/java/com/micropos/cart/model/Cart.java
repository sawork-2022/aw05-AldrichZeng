package com.micropos.cart.model;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
//@Accessors(fluent = true, chain = true)
public class Cart implements Serializable {


    @Id
    @GeneratedValue
    private Integer id;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "items", joinColumns = @JoinColumn(name = "cart_id"))
    private List<Item> items = new ArrayList<>();


    /**
     * 加入购物车
     *
     * @param item
     * @return
     */
    public boolean addItem(Item item) {
        return items.add(item);
    }

    /**
     * 从购物车移除一项（首次出现）
     *
     * @param item
     * @return
     */
    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    public Integer getId() {
        return id;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
