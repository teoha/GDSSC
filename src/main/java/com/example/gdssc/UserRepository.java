package com.example.gdssc;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    @Query(value = "SELECT * FROM USER WHERE salary>=?1 AND salary<=?2 ORDER BY SALARY ASC LIMIT ?4 OFFSET ?3", nativeQuery = true)
    List<User> findOrderBySalary(double min, double max, int offset, int limit);

    @Query(value = "SELECT * FROM USER WHERE salary>=?1 AND salary<=?2 ORDER BY NAME ASC LIMIT ?4 OFFSET ?3", nativeQuery = true)
    List<User> findOrderByName(double min, double max, int offset, int limit);

    @Query(value = "SELECT * FROM USER WHERE salary>=?1 AND salary<=?2 ORDER BY (SELECT NULL) ASC LIMIT ?4 OFFSET ?3", nativeQuery = true)
    List<User> findOrderByNone(double min, double max, int offset, int limit);


}