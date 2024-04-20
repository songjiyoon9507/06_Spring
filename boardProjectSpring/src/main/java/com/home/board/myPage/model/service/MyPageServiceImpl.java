package com.home.board.myPage.model.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.home.board.myPage.model.mapper.MyPageMapper;

import lombok.RequiredArgsConstructor;

@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

	private final MyPageMapper mapper;
}
