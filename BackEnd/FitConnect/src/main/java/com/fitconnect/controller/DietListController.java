package com.fitconnect.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fitconnect.dto.DietListDto;
import com.fitconnect.service.DietListService;



@RestController
public class DietListController {
	
	@Autowired DietListService service;
	
	/**********************************************************************
	 * <PRE> * 메소드 정보 *
	 * 1. MethodName	: list
	 * 2. ClassName		: DietListController
	 * 3. 작성자			: uJuu
	 * 4. 작성일			: 2024. 9. 28. 오후 5:44:25
	 * 5. 설명			: 사용자와 관리자가 등록한 식단리스트 조회
	 * 					  키워드를 이용해 특정 음식만을 조회할 수 있다.
	 * </PRE>
	 * 		@return Map<String,Object>
	 * 		@param dto
	 * 		@return
	**********************************************************************/
	@GetMapping("/dietlist")
	public Map<String, Object> list( DietListDto dto){
		dto.setKeyword(dto.getKeyword());
		Map<String, Object> list = service.getList(dto);
		return list;
	}
	
	/**********************************************************************
	 * <PRE> * 메소드 정보 *
	 * 1. MethodName	: insert
	 * 2. ClassName		: DietListController
	 * 3. 작성자			: uJuu
	 * 4. 작성일			: 2024. 9. 28. 오후 5:47:31
	 * 5. 설명			: 사용자가 직접 음식리스트를 등록
	 * </PRE>
	 * 		@return Map<String,Object>
	 * 		@param dto
	 * 		@return
	**********************************************************************/
	@PostMapping("/dietlist")
	public Map<String, Object> insert(@RequestBody DietListDto dto) {
		boolean isSuccess = service.insert(dto);
		
		return Map.of("isSuccess", isSuccess);
	}
	
}
