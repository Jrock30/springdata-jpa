package com.jrock.springdata.repository;

/**
 * 중첩 구조 처리
 */
public interface NestedClosedProjections {
    String getUsername();
    TeamInfo getTeam();

    interface TeamInfo {
        String getName();
    }
}
