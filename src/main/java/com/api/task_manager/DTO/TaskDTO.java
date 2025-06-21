package com.api.task_manager.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.*;

@Data
public class TaskDTO {
    @NotBlank(message = "Task Name cannot be blank")
    String taskName;

    @FutureOrPresent(message = "Date cannot be past")
    LocalDate date;

    private boolean comp;

    public TaskDTO(){}

    public TaskDTO(String taskName, LocalDate date, boolean comp) {
        this.taskName = taskName;
        this.date = date;
        this.comp = comp;
    }
}
