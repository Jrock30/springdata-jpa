package com.jrock.springdata.repository;

import com.jrock.springdata.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {

    List<Member> findMemberCustom();

}
