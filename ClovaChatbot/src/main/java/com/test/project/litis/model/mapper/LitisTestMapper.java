package com.test.project.litis.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.test.project.litis.model.dto.Organization;

@Mapper
public interface LitisTestMapper {

	List<Organization> selectAll();

}
