package edu.kh.project.todo.model.service;

import org.springframework.web.multipart.MultipartFile;

public interface TodoService {

	String fileUpload(MultipartFile uploadFile);

}
