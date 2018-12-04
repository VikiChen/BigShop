package com.shop.sellergoods.service;

import com.shop.entity.PageResult;
import com.shop.pojo.TbBrand;

import java.util.List;
import java.util.Map;

/*
brand manage
 */
public interface BrandService {
    public List<TbBrand> findAll();
    public PageResult findPage(int pageNum,int pageSize);
    public void add(TbBrand brand);
    public TbBrand findOne(Long id);
    public void update(TbBrand brand);
    public void delete(Long[] ids);
    public PageResult findPage(TbBrand brand ,int pageNum,int pageSize);
    public List<Map> selectOptionList();
}
