package com.example.pcmallprovidergoods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pcmallcommon.model.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBrandDao extends BaseMapper<Brand> {
    @Select("SELECT * FROM brand WHERE id=#{id}")
    public Brand searchBrandById(Integer id);

    @Select("SELECT brand.id,bname,created_time,update_time,image,is_delete " +
            "FROM brand INNER JOIN category_brand " +
            "ON brand.id = category_brand.bid WHERE cid=#{cid};")
    public List<Brand> searchBrandByCid(Integer cid);

    @Select("SELECT COUNT(id) FROM category_brand WHERE bid=#{bid};")
    public Long getCategoryCount(Integer bid);

    @Select("SELECT cid FROM category_brand WHERE bid=#{bid};")
    public List<Integer> getSelectedCategoryIdList(Integer bid);

    @Insert("INSERT INTO category_brand(cid, bid) VALUE (#{cid},#{bid});")
    public Integer insertCategoryBrand(Integer bid, Integer cid);

    @Delete("DELETE FROM category_brand WHERE cid=#{cid} AND bid=#{bid};")
    public Integer deleteCategoryBrand(Integer bid, Integer cid);
}
