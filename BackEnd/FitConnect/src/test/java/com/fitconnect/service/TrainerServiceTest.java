package com.fitconnect.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fitconnect.dto.TrainerDto;
import com.fitconnect.dto.UserDto;
import com.fitconnect.repository.TrainerDao;
import com.fitconnect.repository.UserDao;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {
	
	@InjectMocks
	private TrainerServiceImpl trainerService;
	
	@Mock
	private UserDao userDao;
	
	@Mock
	private TrainerDao trainerDao;
	
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
	
	@Test
	public void testAddTrainer() {
        // Given
        TrainerDto dto = new TrainerDto();
        dto.setTrainer_num(1);
        dto.setTrainer_intro("hello");
        dto.setTrainer_insta("https://www.insta.com");
        dto.setGym_link("www.success.com");
        dto.setGym_name("successgym");
        when(trainerDao.getData(1)).thenReturn(dto);

        // When
        boolean result = trainerService.addTrainer(dto);

        // Then
        assertNotNull(result);
        assertEquals(1, result);
        verify(trainerDao, times(1)).insert(dto);
        verify(trainerDao, times(1)).getData(1);
	}
	
    @Test
    public void testUpdateTrainerInfo() {
        // Given
        TrainerDto dto = new TrainerDto();

        // When
        trainerService.updateTrainerInfo(dto);

        // Then
        verify(trainerDao, times(1)).updateInfo(dto);
    }
    
    @Test
    public void testUpdateTrainerGymInfo() {
        TrainerDto dto = new TrainerDto();
        
        trainerService.updateTrainerGymInfo(dto);
        
        verify(trainerDao, times(1)).updateGymInfo(dto);
    }

    @Test
    public void testDeleteTrainer() {
        String userName = "testUser";
        UserDto userDto = new UserDto();
        userDto.setId(1);
        when(userDao.getData(userName)).thenReturn(userDto);

        trainerService.deleteTrainer(userName);

        verify(trainerDao, times(1)).delete(1);
    }

    @Test
    public void testSelectOne() {
        String userName = "testUserName";
        UserDto userDto = new UserDto();
        userDto.setId(1);
        TrainerDto trainerDto = new TrainerDto();
        when(userDao.getData(userName)).thenReturn(userDto);
        when(trainerDao.getData(1)).thenReturn(trainerDto);

        TrainerDto result = trainerService.selectOne(userName);

        assertEquals(trainerDto, result);
    }

    @Test
    public void testSelectList() {
        List<TrainerDto> trainerList = List.of(new TrainerDto(), new TrainerDto());
        when(trainerDao.getList()).thenReturn(trainerList);

        List<Map<String, Object>> result = trainerService.selectList();

        assertEquals(trainerList, result);
    }

}
