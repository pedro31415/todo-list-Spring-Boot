package br.com.pedrohenrique.todolist.user.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pedrohenrique.todolist.user.models.UserModel;

public interface IUserRepository  extends JpaRepository<UserModel, UUID>{
    UserModel findByUserName(String username);
}
