package com.fitconnect.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitconnect.dto.DietListDto;
import com.fitconnect.repository.DietListDao;


@Service
public class DietListServiceImpl implements DietListService{
	//한 페이지에 글을 몇개씩 표시할 것인지
	final int PAGE_ROW_COUNT=5;
	//하단 페이지 UI를 몇개씩 표시할 것인지
	final int PAGE_DISPLAY_COUNT=5;
	
	
	@Autowired DietListDao dao;
	
		
	@Override
	public Map<String, Object> getList(DietListDto dto) {
		//페이지 번호를 얻어낸다
		int pageNum = dto.getPageNum();
		//보여줄 페이지의 시작 ROWNUM
		int startRowNum = 1 + (pageNum-1)*PAGE_ROW_COUNT;
		//보여줄 페이지의 끝 ROWNUM
		int endRowNum = pageNum*PAGE_ROW_COUNT;
		
		//하단 시작 페이지 번호
		int startPageNum = 1 + ((pageNum-1)/PAGE_DISPLAY_COUNT)*PAGE_DISPLAY_COUNT;
		//하단 끝 페이지 번호
		int endPageNum = startPageNum + PAGE_DISPLAY_COUNT-1;
		
		//전체 글의 갯수
		int totalRow = dao.getCount();
		//전체 페이지의 갯수 구하기
		int totalPageCount = (int) Math.ceil(totalRow/PAGE_ROW_COUNT);
		//끝 페이지 번호가 이미 전체 페이지 갯수보다 크게 계산되었다면 잘못된 값
		if(endPageNum > totalPageCount) {
			endPageNum = totalPageCount; // 보정
		}
		//위에서 계산된 startPageNum 과 endRowNum 을 dto에 담고
		dto.setStartRowNum(startRowNum);
		dto.setEndRowNum(endRowNum);
		// 사용자가 검색한 키워드에 해당하는 음식만 조회할 수 있도록 dto 에 getKeyword 를 담아준다.
		dto.setKeyword(dto.getKeyword());
		//dto 를 인자로 전달해서 목록 얻어오기
		List<DietListDto> list = dao.getList(dto);
		//식단목록과 페이징 처리에 관련된 정보를 Map 에 담아서 리턴해준다.
		return Map.of("list", list,
						"startPageNum", startPageNum,
						"endPageNum", endPageNum,
						"totalPageCount", totalPageCount,
						"pageNum", pageNum,
						"totalRow", totalRow);
	}

	@Override
	public boolean insert(DietListDto dto) {
		
		boolean isSuccess = dao.insert(dto);
		return isSuccess;
	}

}
