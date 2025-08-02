package com.api.task_manager.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.*;

@Data

@Schema(description = "Data Transfer Object  for a task")
public class TaskDTO {
    @Schema(description = "Name of the task")
    @NotBlank(message = "Task Name cannot be blank")
    String taskName;

    @Schema(description = "Due date of task")
    @FutureOrPresent(message = "Date cannot be past")
    LocalDate date;

    @Schema(description = "Task Completion Status")
    private boolean comp;

    public TaskDTO(){}

    public TaskDTO(String taskName, LocalDate date, boolean comp) {
        this.taskName = taskName;
        this.date = date;
        this.comp = comp;
    }
}
