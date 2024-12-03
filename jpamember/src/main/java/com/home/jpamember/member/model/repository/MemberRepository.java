package com.home.jpamember.member.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.home.jpamember.member.model.entity.Member;

// JPARepository 상속 받을 interface CrudRepository 나 JPARepository 상속
// Entity 가 테이블명인 Member 로 돼있음 Type == Member
// DB 연동하기 위해서는 Entity 클래스가 DB 테이블명과 일치해야함
public interface MemberRepository extends JpaRepository<Member, Integer> {
	
}
