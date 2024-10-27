package com.fitconnect.service;

import java.util.List;
import java.util.Map;

import com.fitconnect.dto.DietListDto;

public interface DietListService {
	public Map<String, Object> getList(DietListDto dto);
	public boolean insert(DietListDto dto);
}
