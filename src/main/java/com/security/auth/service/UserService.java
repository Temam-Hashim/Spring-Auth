package com.security.auth.service;


import com.security.auth.entity.Users;
import com.security.auth.exception.AlreadyExistException;
import com.security.auth.exception.NotFoundException;
import com.security.auth.repositary.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Users>  getUsers(){
        return userRepository.findAll();
    }

    public Users getUser(long id){
        return userRepository.findById(id)
                .orElseThrow(
                        () ->{
                            NotFoundException notFoundException = new NotFoundException("No user found with Id " + id);
                            LOGGER.error("Error getting user with id {}",id,notFoundException);
                            return notFoundException;
                        });
    }

    public Users createUser(Users user) {
        var email = userRepository.findByEmail(user.getEmail());
        var mobile = userRepository.findByMobile(user.getMobile());
        if(email.isPresent()){
            throw new AlreadyExistException("Email already Exists");
        }
        else if(mobile.isPresent()){
            throw new AlreadyExistException("Mobile already Exists");
        }

        var hashPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        return userRepository.save(user);
    }

    public Users updateUser(long userId, Users updatedUser) {
        Optional<Users> existingUser = userRepository.findById(userId);

        if (existingUser.isPresent()) {
            Users user = existingUser.get();

            if(updatedUser.getFirstName()!=null || !updatedUser.getFirstName().isEmpty()){
                user.setFirstName(updatedUser.getFirstName());
            }
            if(updatedUser.getLastName()!=null || !updatedUser.getLastName().isEmpty()){
                user.setLastName(updatedUser.getLastName());
            }
            if(updatedUser.getEmail()!=null || !updatedUser.getEmail().isEmpty()){
                user.setEmail(updatedUser.getEmail());
            }
            if(updatedUser.getMobile()!=null || !updatedUser.getMobile().isEmpty()){
                user.setMobile(updatedUser.getMobile());
            }
            if(updatedUser.getRole()!=null || !updatedUser.getRole().isEmpty()){
                user.setRole(updatedUser.getRole());
            }
            if(updatedUser.getPicture()!=null){
                user.setPicture(updatedUser.getPicture());
            }
            if(updatedUser.getPassword()!=null){
                var hashPassword = passwordEncoder.encode(updatedUser.getPassword());
                user.setPassword(hashPassword);
            }

            return userRepository.save(user);
        } else {
            // Handle the case where the user does not exist
            throw new NotFoundException("User with Id "+userId+" not found");
        }
    }

    public Users deleteUser(long userId)  {
        Optional<Users> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new NotFoundException("No Customer found with ID: " + userId);
        } else {
            userRepository.deleteById(userId);
            return user.get();
        }
    }

}
