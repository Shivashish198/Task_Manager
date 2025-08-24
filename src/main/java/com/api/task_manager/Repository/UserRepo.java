package com.api.task_manager.Repository;

import com.api.task_manager.Model.UserAcc;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepo extends JpaRepository<UserAcc,Long> {
    Optional<UserAcc> findByUsername(String username);
}
