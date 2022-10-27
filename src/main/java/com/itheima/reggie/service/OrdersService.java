package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.Entity.Orders;

public interface OrdersService extends IService<Orders> {

    public void submit(Orders orders);

}
