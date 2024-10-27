package com.fitconnect.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fitconnect.dto.DietListDto;


@Repository
public class DietListDaoImpl implements DietListDao{
	
	@Autowired SqlSession session;
	
	@Override
	public List<DietListDto> getList(DietListDto dto) {
		
		return session.selectList("dietList.getList", dto);
	}

	@Override
	public boolean insert(DietListDto dto) {
		
		int result = session.insert("dietList.insert", dto);
		if(result > 0) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public int getCount() {
		
		return session.selectOne("dietList.getCount");
	}

}
