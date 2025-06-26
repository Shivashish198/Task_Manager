package com.api.task_manager.Controller;

import com.api.task_manager.DTO.TaskDTO;
import com.api.task_manager.Service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service.getAllTasks());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTask(@RequestBody TaskDTO task) {
        service.saveTask(task);
        return ResponseEntity.ok("Task added");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Task Deleted");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody TaskDTO task) {
        service.update(id, task);
        return ResponseEntity.ok("Task Updated");
    }

    @PutMapping("/complete/{id}")
    public ResponseEntity<?> complete(@PathVariable Long id) {
        service.complete(id);
        return ResponseEntity.ok("Task Completed");
    }
}
