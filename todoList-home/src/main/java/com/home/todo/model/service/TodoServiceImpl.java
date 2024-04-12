package com.home.todo.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.todo.model.dto.Todo;
import com.home.todo.model.mapper.TodoMapper;

@Service // 비즈니스 로직(데이터 가공, 트랜잭션 처리) 역할 명시 + Bean 으로 등록
public class TodoServiceImpl implements TodoService {

	@Autowired
	private TodoMapper mapper;

	// 할 일 목록 + 완료된 할 일 개수 조회
	@Override
	public Map<String, Object> selectAll() {
		
		// 1. 할 일 목록 조회
		List<Todo> todoList = mapper.selectAll();
		// 2. 완료된 할 일 개수 조회
		int completeCount = mapper.getCompleteCount();
		// Map 으로 묶어서 반환
		Map<String, Object> map = new HashMap<>();
		
		map.put("todoList", todoList);
		map.put("completeCount", completeCount);
		
		return map;
	}
}
