package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itheima.reggie.Entity.ShoppingCart;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        //设置用户ID
        Long userId = BaseContext.getThreadLocal();
        shoppingCart.setUserId(userId);
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        Long dishId = shoppingCart.getDishId();
        //判断加的是套餐还是菜品
        if(dishId!=null){//加的是菜
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        }else{//加的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart custom = shoppingCartService.getOne(queryWrapper);

        if(custom!=null){//判断是新加的还是已经加过的
            Integer count = custom.getNumber();
            custom.setNumber(count+1);
            shoppingCartService.updateById(custom);
        }else{
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            custom=shoppingCart;
        }
        custom.setCreateTime(LocalDateTime.now());
        return R.success(custom);
    }
    @PostMapping("/sub")
    public R<ShoppingCart> reduce(@RequestBody ShoppingCart shoppingCart){
        Long userId = BaseContext.getThreadLocal();
        shoppingCart.setUserId(userId);
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        Long dishId = shoppingCart.getDishId();
        //判断加的是套餐还是菜品
        if(dishId!=null){//加的是菜
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        }else{//加的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart custom = shoppingCartService.getOne(queryWrapper);
            Integer count = custom.getNumber();
            if(count-1==0){
                shoppingCartService.removeById(custom);
            }
            custom.setNumber(count-1);
            shoppingCartService.updateById(custom);

        return R.success(custom);
    }
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getThreadLocal());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }
    @DeleteMapping("/clean")
    public R<String> delete(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getThreadLocal());
        shoppingCartService.remove(queryWrapper);
        return R.success("删除成功");
    }
}
