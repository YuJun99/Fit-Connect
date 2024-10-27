package com.fitconnect.repository;

import java.util.List;

import com.fitconnect.dto.DietListDto;


public interface DietListDao {
	public List<DietListDto> getList(DietListDto dto);
	public boolean insert(DietListDto dto);
	public int getCount();
}
