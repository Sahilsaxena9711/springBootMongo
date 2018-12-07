package org.mongo.Repository;

import org.mongo.Entity.Attendance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AttendanceRepository extends MongoRepository<Attendance,Integer> {

    List<Attendance> getAttendanceByDate(String date);

    Attendance getAttendanceByDateAndUsername(String date, String username);

    List<Attendance> getByUsername(String username);
}
