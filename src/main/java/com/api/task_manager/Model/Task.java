package com.api.task_manager.Model;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.Data;
import java.time.*;

@Data
@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue
    Long id;

    @FutureOrPresent(message="Date cannot be past ")
    LocalDate date;

    @NotBlank(message="Task name cannot be blank")
    String taskName;

    boolean comp;

    public Task() {}
}
