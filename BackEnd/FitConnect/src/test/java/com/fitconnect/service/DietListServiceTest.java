package com.fitconnect.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fitconnect.dto.DietListDto;
import com.fitconnect.repository.DietListDao;

class DietListServiceTest {

    @Mock
    private DietListDao dao;

    @InjectMocks
    private DietListServiceImpl service;

    @BeforeEach
    void setUp() {
        // Mock 객체 초기화
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetList() {
        // Given
        DietListDto mockDto = new DietListDto();
        mockDto.setKeyword("testKeyword");
        List<DietListDto> mockList = List.of(new DietListDto());

        // When
        when(dao.getList(any(DietListDto.class))).thenReturn(mockList);
        Map<String, Object> result = service.getList(mockDto);

        // Then
        assertNotNull(result);  // 결과가 null이 아니어야 함
        assertEquals(1, result.size());  // 반환된 목록의 크기가 1이어야 함
        verify(dao).getList(any(DietListDto.class));  // DAO의 getList 메서드가 호출되었는지 확인
    }

    @Test
    void testInsert() {
        // Given
        DietListDto mockDto = new DietListDto();
        mockDto.setKeyword("testKeyword");

        // When
        service.insert(mockDto);

        // Then
        verify(dao).insert(mockDto);  // DAO의 insert 메서드가 호출되었는지 확인
    }
}
