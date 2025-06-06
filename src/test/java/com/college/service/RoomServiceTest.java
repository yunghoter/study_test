package com.college.service;

import com.college.entity.Room;
import com.college.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    private Room room;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setRoomId(1L);
        room.setRoomNumber("101");
        room.setCapacity(30);
    }

    @Test
    void findAll_ShouldReturnAllRooms() {
        when(roomRepository.findAll()).thenReturn(Arrays.asList(room));

        List<Room> result = roomService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRoomId()).isEqualTo(1L);
        assertThat(result.get(0).getRoomNumber()).isEqualTo("101");
        assertThat(result.get(0).getCapacity()).isEqualTo(30);
        verify(roomRepository).findAll();
    }

    @Test
    void findById_WhenExists_ShouldReturnRoom() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        Optional<Room> result = roomService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getRoomId()).isEqualTo(1L);
        assertThat(result.get().getRoomNumber()).isEqualTo("101");
        assertThat(result.get().getCapacity()).isEqualTo(30);
        verify(roomRepository).findById(1L);
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Room> result = roomService.findById(1L);

        assertThat(result).isEmpty();
        verify(roomRepository).findById(1L);
    }

    @Test
    void save_ShouldReturnSavedRoom() {
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        Room result = roomService.save(room);

        assertThat(result).isNotNull();
        assertThat(result.getRoomId()).isEqualTo(1L);
        assertThat(result.getRoomNumber()).isEqualTo("101");
        assertThat(result.getCapacity()).isEqualTo(30);
        verify(roomRepository).save(room);
    }

    @Test
    void deleteById_ShouldCallRepository() {
        doNothing().when(roomRepository).deleteById(1L);

        roomService.deleteById(1L);

        verify(roomRepository).deleteById(1L);
    }
}
