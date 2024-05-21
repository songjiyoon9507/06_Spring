package edu.kh.project.todo.model.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.todo.model.mapper.TodoMapper;
import lombok.RequiredArgsConstructor;

@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

	private final TodoMapper mapper;

	@Override
	public String fileUpload(MultipartFile uploadFile) {
		return "";
	}
	
}
