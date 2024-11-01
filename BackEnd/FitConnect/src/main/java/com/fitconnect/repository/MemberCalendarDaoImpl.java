package com.fitconnect.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fitconnect.dto.MemberCalendarDto;
import com.fitconnect.exception.NotCalendarIdOneException;
import com.fitconnect.exception.NullCalendarIdException;


@Repository
public class MemberCalendarDaoImpl implements MemberCalendarDao{

	// 의존객체 주입
	@Autowired SqlSession session;
	
	@Override
	public List<MemberCalendarDto> getList(int user_num) {
		
		return session.selectList("MemberCalendar.getList", user_num);
	}

	@Override
	public MemberCalendarDto getData(MemberCalendarDto dto) {
		
		return session.selectOne("MemberCalendar.getData", dto);
	}

	@Override
	public Map<String, Object> getDataByDate(MemberCalendarDto dto) throws NotCalendarIdOneException, NullPointerException {
		try {
			MemberCalendarDto result = session.selectOne("MemberCalendar.getDataByDate", dto);
			
			// m_calendar_id 가 1개 있을 때, true와 result 값 반환
			if(result != null) {
				return Map.of("isSuccess", true,
								"result", result);
				
			}else {	// m_calendar_id 가 없을 때 false 반환
				return Map.of("isSuccess", false);
				
			}
			// m_calendar_id 가 여러개 있을 때 예외처리
		}catch(org.mybatis.spring.MyBatisSystemException e) {
			throw new NotCalendarIdOneException("2개 이상의 캘린더 아이디 조회됨");
			
		}catch(NullPointerException e) {
			//m_calendar_id 가 없는 경우
			throw new NullCalendarIdException("존재하지 않는 값 입니다.");
		}
			
	}
	
	@Override
	public boolean insert(MemberCalendarDto dto) {
		
		int result = session.insert("MemberCalendar.insert", dto);
		if(result > 0) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean update(MemberCalendarDto dto) {

		int result = session.update("MemberCalendar.update", dto);
		if(result > 0) {
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	public boolean delete(int member_num, int m_calendar_id) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("member_num", member_num);
		params.put("m_calendar_id", m_calendar_id);
		
		int result = session.delete("MemberCalendar.delete", params);
		
		if(result > 0) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean getCalendarId(String regdate) throws NotCalendarIdOneException{
		try {
			MemberCalendarDto result = session.selectOne("MemberCalendar.getMcalendaridToDto", regdate);
			if(result != null) {
				return true;
			}else {
				// exception이 발생하면 false 반환값을 exception 에서 처리
				return false;
			}
		}catch(org.mybatis.spring.MyBatisSystemException e) {
			throw new NotCalendarIdOneException("2개 이상의 캘린더 아이디 조회됨");
		}
	}

	@Override
	public int getMcalendarId(String regdate) throws NotCalendarIdOneException, NullCalendarIdException{
		try {
			// exception 발생 시 false 값 반환
			return session.selectOne("MemberCalendar.getMcalendaridToInt",regdate);
			
		}catch(org.mybatis.spring.MyBatisSystemException e) {
			//m_calendar_id 가 2개 이상이 존재하는 경우
			throw new NotCalendarIdOneException("2개 이상의 캘린더 아이디 조회됨");
		}catch(NullPointerException e) {
			//m_calendar_id 가 없는 경우
			throw new NullCalendarIdException("존재하지 않는 값 입니다.");
		}
		
	}

}
