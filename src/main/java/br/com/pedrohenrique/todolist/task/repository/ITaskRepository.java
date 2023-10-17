package br.com.pedrohenrique.todolist.task.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.pedrohenrique.todolist.task.models.TaskModel;
import java.util.List;

public interface ITaskRepository extends JpaRepository<TaskModel, UUID>{
    List<TaskModel> findByIdUser(UUID idUser);
}
