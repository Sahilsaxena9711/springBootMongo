package org.mongo.Repository;

import org.mongo.Entity.Attendance;
import org.mongo.Entity.Leave;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LeaveRepository extends MongoRepository<Leave,Integer> {


    List<Leave> getByApproved(boolean approved);

    Leave getLeaveByUsernameAndDate(String username, String date);

    List<Leave> getLeaveByUsername(String username);

    List<Leave> getLeaveByUsernameAndMonth(String username, String month);
}
