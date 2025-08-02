package com.api.task_manager.Controller;

import com.api.task_manager.DTO.TaskDTO;
import com.api.task_manager.Service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Task Controller", description = "APIs for managing tasks")
@RestController
@RequestMapping("/task")
@CrossOrigin(origins = "http://localhost:3000")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @Operation(summary = "Get all tasks", description = "Returns all tasks")
    @GetMapping("/get")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service.getAllTasks());
    }

    @Operation(summary = "Get task by ID", description = "Returns task for the given ID")
    @ApiResponses (value = {
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Add task", description = "Adds task to the DB")
    @PostMapping("/add")
    public ResponseEntity<?> addTask(@Valid @RequestBody TaskDTO task) {
        service.saveTask(task);
        return ResponseEntity.ok("Task added");
    }

    @Operation(summary = "Delete task by ID", description = "Deletes task for the given ID")
    @ApiResponses (value = {
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Task Deleted");
    }

    @Operation(summary = "Update task by ID", description = "Updates task for the given ID")
    @ApiResponses (value = {
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody TaskDTO task) {
        service.update(id, task);
        return ResponseEntity.ok("Task Updated");
    }

    @Operation(summary = "Task completed", description = "Completes task for the given ID")
    @PutMapping("/complete/{id}")
    public ResponseEntity<?> complete(@PathVariable Long id) {
        service.complete(id);
        return ResponseEntity.ok("Task Completed");
    }

    @GetMapping("/get/name/{taskName}")
    public ResponseEntity<?> getByName(@PathVariable String taskName) {
        service.findByTaskName(taskName);
        return ResponseEntity.ok().build();
    }
}
