package com.witchlink.repository;

import com.witchlink.model.Link;
import com.witchlink.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    List<Link> findByUserOrderByDisplayOrderAsc(User user);
    List<Link> findByUserAndIsActiveOrderByDisplayOrderAsc(User user, Boolean isActive);
}
