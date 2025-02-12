package com.example.cbr.entity;


import java.io.Serializable;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryRoutingId implements Serializable{
	private String category_name;
    private String subcategory_name;
    public CategoryRoutingId() {}

    public CategoryRoutingId(String category_name, String subcategory_name) {
        this.category_name = category_name;
        this.subcategory_name = subcategory_name;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryRoutingId that = (CategoryRoutingId) o;
        return subcategory_name == that.subcategory_name && Objects.equals(category_name, that.category_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category_name, subcategory_name);
    }
}

