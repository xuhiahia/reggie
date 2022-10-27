package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.Entity.Category;

public interface CategoryService extends IService<Category> {
    public void removeById(Long id);
}
