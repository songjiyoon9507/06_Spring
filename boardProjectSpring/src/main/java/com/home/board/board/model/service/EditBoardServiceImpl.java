package com.home.board.board.model.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.home.board.board.model.mapper.EditBoardMapper;

import lombok.RequiredArgsConstructor;

@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor
public class EditBoardServiceImpl implements EditBoardService {

	private final EditBoardMapper mapper;
}
