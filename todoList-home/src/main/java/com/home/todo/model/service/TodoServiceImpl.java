package com.home.todo.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.todo.model.mapper.TodoMapper;

@Service // 비즈니스 로직(데이터 가공, 트랜잭션 처리) 역할 명시 + Bean 으로 등록
public class TodoServiceImpl implements TodoService {

	@Autowired
	private TodoMapper mapper;
}