package com.itzixi.service;

import com.itzixi.pojo.ItemCategory;

import java.util.List;

public interface ItemCategoryService {

    /**
     * 根据商品分类的主键id进行查询
     * @param categoryId
     * @return
     */
    public ItemCategory queryItemCategoryById(Integer categoryId);

    /**
     * 创建商品分类
     * @param itemCategory
     */
    public void createItemCategory(ItemCategory itemCategory);

    /**
     * 修改商品分类
     * @param categoryId
     * @param categoryName
     */
    public void updateItemCategory(Integer categoryId, String categoryName) throws Exception;

    /**
     * 根据商品分类的主键id删除记录
     * @param categoryId
     */
    public void deleteItemCategoryById(Integer categoryId);

    /**
     * 查询所有分类数据列表
     * @return
     */
    public List<ItemCategory> queryItemCategoryList();
}
