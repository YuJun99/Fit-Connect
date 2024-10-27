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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fitconnect.auth.PrincipalDetails;
import com.fitconnect.dto.MemberCalendarDto;
import com.fitconnect.dto.UserDto;
import com.fitconnect.repository.MemberCalendarDao;

class MemberCalendarServiceTest {

    @Mock
    private MemberCalendarDao dao;

    @Mock
    private Authentication authentication;

    @Mock
    private PrincipalDetails principalDetails;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private MemberCalendarServiceImpl service;

    @BeforeEach
    void setUp() {
        // Mock 객체 초기화
        MockitoAnnotations.openMocks(this);
        
        // SecurityContextHolder에 Mock SecurityContext를 설정
        SecurityContextHolder.setContext(securityContext);

        // Authentication 객체의 Mock 설정
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principalDetails);
        // UserDto는 PrincipalDetails에 있는 객체
        when(principalDetails.getDto()).thenReturn(new UserDto(1, 
        														"test", 
        														"1234", 
        														"1234", 
        														"test", 
        														"test@xxx.com", 
        														"USER", 
        														"USER", 
        														"2024-09-39", 
        														null, 
        														"test", 
        														"test"));
    }

    @Test
    void testGetAll() {
        // Given
        List<MemberCalendarDto> mockList = List.of(new MemberCalendarDto());
        when(dao.getList(anyInt())).thenReturn(mockList);

        // When
        List<Map<String, Object>> result = service.getAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(dao).getList(1); // 사용자가 1로 설정되었기 때문에 확인
    }

    @Test
    void testGetOne() {
        // Given
        MemberCalendarDto mockDto = new MemberCalendarDto();
        when(dao.getData(any(MemberCalendarDto.class))).thenReturn(mockDto);

        // When
        Map<String, Object> result = service.getOne(mockDto);

        // Then
        assertNotNull(result);
        assertEquals(mockDto, result.get("dto"));
        verify(dao).getData(any(MemberCalendarDto.class));
    }

    @Test
    void testInsert() {
        // Given
        MemberCalendarDto mockDto = new MemberCalendarDto();

        // When
        service.insert(mockDto);

        // Then
        verify(dao).insert(mockDto);
        assertEquals(1, mockDto.getMember_num()); // 사용자 ID가 1로 설정되었는지 확인
    }

    @Test
    void testUpdate() {
        // Given
        MemberCalendarDto mockDto = new MemberCalendarDto();

        // When
        service.update(mockDto);

        // Then
        verify(dao).update(mockDto);
        assertEquals(1, mockDto.getMember_num()); // 사용자 ID가 1로 설정되었는지 확인
    }

    @Test
    void testDelete() {
        // Given
        MemberCalendarDto dto = new MemberCalendarDto();
        dto.setM_calendar_id(100);

        // When
        service.deleteByDate(dto);

        // Then
        verify(dao).delete(1, dto.getM_calendar_id()); // 사용자가 1로 설정되었고, 해당 ID로 삭제 요청 확인
    }
}
