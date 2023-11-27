package com.security.auth.controller;

import com.security.auth.entity.Users;
import com.security.auth.exception.DeletedException;
import com.security.auth.exception.NotFoundException;
import com.security.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping()
    List<Users> getUsers(){
        return userService.getUsers();
    }

    @GetMapping(path="{userId}")
    Users getUser(@PathVariable("userId") long id ){
        return userService.getUser(id);
    }

    @PostMapping()
    public ResponseEntity<Users> createUser(@Valid @RequestBody Users user) {
        Users createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Users> updateUser(@PathVariable long userId,@RequestBody Users updatedUser) {
        try {
            Users user = userService.updateUser(userId, updatedUser);
            return ResponseEntity.ok().body(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<Users> deleteUser(@PathVariable long userId) {
        Users deletedUser = userService.deleteUser(userId);

        if(deletedUser!=null){
            throw new DeletedException("User with ID "+userId+" has been deleted");
        }else{
            throw new NotFoundException("User with ID "+userId+" not found");
        }
    }
}
