package com.test.project.litis.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.project.litis.model.dto.Organization;
import com.test.project.litis.model.mapper.LitisTestMapper;

import lombok.RequiredArgsConstructor;

@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor
public class LitisTestServiceImpl implements LitisTestService {
	
	private final LitisTestMapper mapper;
	
	@Override
	public List<Organization> selectAll() {
		return mapper.selectAll();
	}

}
