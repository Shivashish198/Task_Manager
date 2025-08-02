package com.api.task_manager.Repository;

import com.api.task_manager.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {

    List<Task> findByTaskName(String taskName);

    List<Task> findByComp(boolean comp);
}
