package com.example.pcmallprovidergoods.controller;

import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallprovidergoods.service.IGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private IGoodsService goodsService;

    public GoodsController() {
        log.debug("创建Controller对象：GoodsController");
    }

    @RequestMapping("/getGoodsList")
    public ResponseResult<List<Goods>> getGoodsList(Integer currentPage, Integer pageSize) {
        List<Goods> goodsList = goodsService.getGoodsList(currentPage, pageSize);
        return ResponseResult.ok(goodsList);
    }

    @PostMapping("/getTotalCount")
    public ResponseResult<Long> getTotalCount() {
        return ResponseResult.ok(goodsService.getTotalCount());
    }

    @RequestMapping("/addGoods")
    public ResponseResult<String> addGoods(@RequestBody Goods goods){
        if (goodsService.addGoods(goods) == 1) {
            return ResponseResult.ok("添加成功");
        }
        return ResponseResult.error("添加失败");
    }

    @RequestMapping("/updateGoods")
    public ResponseResult<String> updateGoods(@RequestBody Goods goods){
        if (goodsService.updateGoods(goods) == 1) {
            return ResponseResult.ok("修改成功");
        }
        return ResponseResult.error("修改失败");
    }
}
