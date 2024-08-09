package com.example.pcmallprovidergoods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallprovidergoods.dao.IGoodsDao;
import com.example.pcmallprovidergoods.service.IGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GoodsServiceImpl implements IGoodsService {
    @Autowired
    private IGoodsDao goodsDao;

    public GoodsServiceImpl() {
        log.debug("创建Service对象：{}", this);
    }

    @Override
    public Goods searchGoodsById(Map<String, Boolean> aspectRule, Integer id) {
        return goodsDao.selectById(id);
    }

    @Override
    public List<Goods> searchGoodsList(Map<String, Boolean> aspectRule, GoodsSearchType searchType, Map<String, Object> searchValue) {
        Integer currentPage = (Integer) searchValue.get("currentPage");
        Integer pageSize = (Integer) searchValue.get("pageSize");
        if (searchType == GoodsSearchType.ALL) {
            Page<Goods> page = new Page<>(currentPage, pageSize);
            return goodsDao.selectPage(page, null).getRecords();
        } else if (searchType == GoodsSearchType.SEARCH) {
            String str = (String) searchValue.get("searchValue");
            return goodsDao.searchGoodsList(str, (currentPage - 1) * pageSize, pageSize);
        } else if (searchType == GoodsSearchType.SEARCH_BY_CID_AND_BID) {
            Integer cid = (Integer) searchValue.get("cid");
            Integer bid = (Integer) searchValue.get("bid");
            return goodsDao.searchGoodsByCidAndBid(cid, bid, (currentPage - 1) * pageSize, pageSize);
        }
        return List.of();
    }

    @Override
    public Long getTotalCount(GoodsSearchType searchType, Map<String, Object> searchValue) {
        if (searchType == GoodsSearchType.ALL) {
            return goodsDao.selectCount(null);
        } else if (searchType == GoodsSearchType.SEARCH) {
            return goodsDao.getRecordsFiltered((String) searchValue.get("searchValue"));
        } else if (searchType == GoodsSearchType.SEARCH_BY_CID_AND_BID) {
            Integer cid = (Integer) searchValue.get("cid");
            Integer bid = (Integer) searchValue.get("bid");
            QueryWrapper<Goods> queryWrapper = new QueryWrapper<Goods>()
                    .eq("cid", cid)
                    .eq("bid", bid)
                    .eq("status", 0)
                    .eq("is_delete", 0);
            return goodsDao.selectCount(queryWrapper);
        }
        return 0L;
    }

    @Override
    public void addGoods(Goods goods) {
        if (goodsDao.insert(goods) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }

    @Override
    public void updateGoods(Goods goods) {
        goods.setUpdateTime(LocalDateTime.now());
        if (goodsDao.updateById(goods) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }

    @Override
    public void updateGoodsStatus(Goods goods) {
        Goods searched = searchGoodsById(null, goods.getId());
        if (goods.getStatus() == 0 && searched.getCount() == 0) {
            //上架操作如果商品没货，设置为缺货
            goods.setStatus(1);
        }
        updateGoods(goods);
    }
}
