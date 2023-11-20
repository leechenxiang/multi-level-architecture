package com.itzixi.test;

import com.itzixi.pojo.ItemCategory;
import com.itzixi.service.ItemCategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ItemCategoryTest {

    @Autowired
    private ItemCategoryService itemCategoryService;

    @Test
    public void testQuery() {
        ItemCategory itemCategory = itemCategoryService.queryItemCategoryById(1001);
        System.out.println(itemCategory.toString());
    }

    @Test
    public void testCreate() {
        Integer id = (int)((Math.random() * 9 + 1) * 100000);

        ItemCategory itemCategory = new ItemCategory();

        itemCategory.setId(id);
        itemCategory.setCategoryName("测试产品分类");

        itemCategoryService.createItemCategory(itemCategory);
    }

    @Test
    public void testUpdate() throws Exception {
        Integer id = 390004;
        String categoryName = "测试更新产品分类";

        itemCategoryService.updateItemCategory(id, categoryName);
    }

    @Test
    public void testDelete() {
        Integer id = 390004;
        itemCategoryService.deleteItemCategoryById(id);
    }

}
