package com.itzixi.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "item_category")
public class ItemCategory {
    /**
     * 主键id
     */
    @Id
    private Integer id;

    /**
     * 商品分类名称
     */
    @Column(name = "category_name")
    private String categoryName;

    /**
     * 获取主键id
     *
     * @return id - 主键id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键id
     *
     * @param id 主键id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取商品分类名称
     *
     * @return category_name - 商品分类名称
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * 设置商品分类名称
     *
     * @param categoryName 商品分类名称
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "ItemCategory{" +
                "id=" + id +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}