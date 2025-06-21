package com.api.task_manager.Service;

import com.api.task_manager.DTO.TaskDTO;
import com.api.task_manager.Model.Task;
import com.api.task_manager.Repository.TaskRepo;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepo repo;
    public TaskService(TaskRepo repo) {
        this.repo = repo;
    }

    public Task toEntity(TaskDTO dto) {
        Task task = new Task();
        task.setTaskName(dto.getTaskName());
        task.setDate(dto.getDate());
        task.setComp(dto.isComp());
        return task;
    }

    public TaskDTO toDTO(Task task) {
        return new TaskDTO(task.getTaskName(), task.getDate(), task.isComp());
    }

    public void saveTask(TaskDTO task) {
        repo.save(toEntity(task));
    }

    public List<TaskDTO> getAllTasks() {
        return repo.findAll().stream().map(this::toDTO).toList();
    }

    public TaskDTO getById(Long id) {
        Task task = repo.findById(id).orElseThrow(()-> new RuntimeException("Task not found"));
        return toDTO(task);
    }

    public void delete(Long id) {
        Task task = repo.findById(id).orElseThrow(()-> new RuntimeException("Task Not Found"));
        repo.delete(task);
    }

    public void update(Long id, TaskDTO dto) {
        Task task = repo.findById(id).orElseThrow(()-> new RuntimeException("Task not found"));
        task.setTaskName(dto.getTaskName());
        task.setDate(dto.getDate());
        repo.save(task);
    }

    public void complete(Long id) {
        Task task = repo.findById(id).orElseThrow(()-> new RuntimeException("Task not found"));
        task.setComp(true);
        repo.save(task);
    }
}
