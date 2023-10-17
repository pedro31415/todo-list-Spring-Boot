package br.com.pedrohenrique.todolist.task.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;


import br.com.pedrohenrique.todolist.task.models.TaskModel;
import br.com.pedrohenrique.todolist.task.repository.ITaskRepository;
import br.com.pedrohenrique.todolist.util.Utils;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID) idUser);
        var currentDate = LocalDateTime.now();
        var task = this.taskRepository.save(taskModel);
        if(currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio / final deve ser maior que a data atual");
        }
        if(taskModel.getStartAt().isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio deve ser menor que a data final");
        }
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
        var task = this.taskRepository.findByIdUser((UUID) idUser);
        return task;
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel,  @PathVariable UUID id, HttpServletRequest request){
         var task = this.taskRepository.findById(id).orElse(null);
         var idUser = request.getAttribute("idUser");
         if(task == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada");
         }


         if(!task.getIdUser().equals(idUser)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usúario não tem permissão para alterar essa tarefa");
         }

         Utils.copyNonNullProperties(taskModel, task);
         var taskUpdate = this.taskRepository.save(task);
 
         return ResponseEntity.ok().body(taskUpdate);
    }
}
