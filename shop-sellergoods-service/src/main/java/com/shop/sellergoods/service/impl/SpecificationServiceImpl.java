package com.shop.sellergoods.service.impl;
import java.util.List;
import java.util.Map;

import com.shop.entity.PageResult;
import com.shop.mapper.TbSpecificationOptionMapper;
import com.shop.pojo.TbSpecificationOption;
import com.shop.pojo.TbSpecificationOptionExample;
import com.shop.pojogroup.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.shop.mapper.TbSpecificationMapper;
import com.shop.pojo.TbSpecification;
import com.shop.pojo.TbSpecificationExample;
import com.shop.pojo.TbSpecificationExample.Criteria;
import com.shop.sellergoods.service.SpecificationService;
import org.springframework.transaction.annotation.Transactional;


/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class SpecificationServiceImpl implements SpecificationService {

	@Autowired
	private TbSpecificationMapper specificationMapper;
	@Autowired
	private TbSpecificationOptionMapper specificationOptionMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSpecification> findAll() {
		return specificationMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbSpecification> page=   (Page<TbSpecification>) specificationMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Specification specification) {
		TbSpecification tbSpecification =specification.getSpecification();
		specificationMapper.insert(tbSpecification);
		List<TbSpecificationOption> specificationOptionList = specification.getSpecificationOptionList();
		for (TbSpecificationOption option:specificationOptionList) {
			option.setSpecId(tbSpecification.getId());
			specificationOptionMapper.insert(option);
		}
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(Specification specification){
		TbSpecification tbSpecification =specification.getSpecification();
		specificationMapper.updateByPrimaryKey(tbSpecification);

		TbSpecificationOptionExample optionExample =new TbSpecificationOptionExample();
		TbSpecificationOptionExample.Criteria criteria =optionExample.createCriteria();
		criteria.andSpecIdEqualTo(tbSpecification.getId());
		specificationOptionMapper.deleteByExample(optionExample);

		List<TbSpecificationOption> specificationOptionList = specification.getSpecificationOptionList();
		for (TbSpecificationOption option:specificationOptionList) {
			option.setSpecId(tbSpecification.getId());
			specificationOptionMapper.insert(option);
		}
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Specification findOne(Long id){
		Specification specification=new Specification();
		specification.setSpecification(specificationMapper.selectByPrimaryKey(id));
		TbSpecificationOptionExample example =new TbSpecificationOptionExample();
		TbSpecificationOptionExample.Criteria criteria =example.createCriteria();
		criteria.andSpecIdEqualTo(id);
		List<TbSpecificationOption> tbSpecificationOptions = specificationOptionMapper.selectByExample(example);
		specification.setSpecificationOptionList(tbSpecificationOptions);
		return specification;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			specificationMapper.deleteByPrimaryKey(id);
			TbSpecificationOptionExample example =new TbSpecificationOptionExample();
			TbSpecificationOptionExample.Criteria criteria =example.createCriteria();
			criteria.andSpecIdEqualTo(id);
			specificationOptionMapper.deleteByExample(example);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbSpecification specification, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbSpecificationExample example=new TbSpecificationExample();
		Criteria criteria = example.createCriteria();
		
		if(specification!=null){			
						if(specification.getSpecName()!=null && specification.getSpecName().length()>0){
				criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
			}
	
		}
		
		Page<TbSpecification> page= (Page<TbSpecification>)specificationMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public List<Map> selectOptionList() {
		return specificationMapper.selectOptionList();
	}

}
