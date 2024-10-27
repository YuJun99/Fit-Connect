package com.fitconnect.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fitconnect.dto.MemberDto;
import com.fitconnect.dto.TrainerDto;
import com.fitconnect.dto.UserDto;
import com.fitconnect.repository.MemberDao;
import com.fitconnect.repository.UserDao;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
	
	@InjectMocks
	private MemberServiceImpl memberService;
	
	@Mock
	private UserDao userDao;
	
	@Mock
	private MemberDao memberDao;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void testAddMember() {
		MemberDto dto = new MemberDto();
		dto.setMember_num(1);
		dto.setTrainer_num(2);
		dto.setMember_height((float) 174.4);
		dto.setMember_weight((float) 66.6);
		dto.setMember_gender("MAN");
		when(memberDao.getData(1)).thenReturn(dto);
		
		boolean result = memberService.addMember(dto);
		
		assertNotNull(result);
		assertEquals(1, result);
		verify(memberDao, times(1)).insert(dto);
		verify(memberDao, times(1)).getData(1);
	}
	
    @Test
    public void testUpdateMemberInfo() {
        // Given
        MemberDto dto = new MemberDto();

        // When
        memberService.updateMemberInfo(dto);

        // Then
        verify(memberDao, times(1)).updateInfo(dto);
    }
    
    @Test
    public void testUpdateMemberPlan() {
        // Given
        MemberDto dto = new MemberDto();

        // When
        memberService.updateMemberPlan(dto);

        // Then
        verify(memberDao, times(1)).updatePlan(dto);
    }
    
    @Test
    public void testUpdateMemberTrainer() {
        // Given
        MemberDto dto = new MemberDto();

        // When
        memberService.updateMemberTrainer(dto);

        // Then
        verify(memberDao, times(1)).updateTrainer(dto);
    }
    
    @Test
    public void testDeleteMember() {
        String userName = "testUser";
        UserDto userDto = new UserDto();
        userDto.setId(1);
        when(userDao.getData(userName)).thenReturn(userDto);

        memberService.deleteMember(userName);

        verify(memberDao, times(1)).delete(1);
    }
    
    @Test
    public void testSelectOne() {
        String userName = "testUserName";
        UserDto userDto = new UserDto();
        userDto.setId(1);
        MemberDto memberDto = new MemberDto();
        when(userDao.getData(userName)).thenReturn(userDto);
        when(memberDao.getData(1)).thenReturn(memberDto);

        MemberDto result = memberService.selectOne(userName);

        assertEquals(memberDto, result);
    }
    
    @Test
    public void testSelectList() {
        List<MemberDto> memberList = List.of(new MemberDto(), new MemberDto());
        when(memberDao.getList()).thenReturn(memberList);

        List<MemberDto> result = memberService.selectList();

        assertEquals(memberList, result);
    }
}
