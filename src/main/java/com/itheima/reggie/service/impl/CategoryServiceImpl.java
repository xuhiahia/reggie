package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.Entity.Category;
import com.itheima.reggie.Entity.Dish;
import com.itheima.reggie.Entity.Setmeal;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;


    public void removeById(Long ids) {
        //查看有无关联菜品
        //Dish
        LambdaQueryWrapper<Dish> queryWrapper1=new LambdaQueryWrapper<>();
        queryWrapper1.eq(Dish::getCategoryId, ids);
        int count = dishService.count(queryWrapper1);
        if(count>0){
            throw new CustomException("已有关联菜品");
        }
        //Setmeal
        LambdaQueryWrapper<Setmeal> queryWrapper2=new LambdaQueryWrapper<>();
        queryWrapper2.eq(Setmeal::getCategoryId,ids);
        int count1 = setmealService.count(queryWrapper2);
        if(count1>0){
            throw new CustomException("已有关联套餐");
        }
        super.removeById(ids);
    }
}
