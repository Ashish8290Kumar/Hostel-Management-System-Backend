package com.AshishWork.HostelManagementSystem.Repositroy;

import com.AshishWork.HostelManagementSystem.Entity.Room;
import com.AshishWork.HostelManagementSystem.Enum.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByStatus(RoomStatus status);
    Optional<Room> findByRoomNumber(String roomNumber);
}
