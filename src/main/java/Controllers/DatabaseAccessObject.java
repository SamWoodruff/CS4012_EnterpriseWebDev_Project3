package Controllers;

import entities.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;

@Service
public class DatabaseAccessObject {
    @PersistenceContext protected EntityManager entityManager;
    @Inject UserRepository usersRepository;

    @Transactional
    public ArrayList<User> getUsers(){
        return (ArrayList<User>) usersRepository.findAll();
    }

    @Transactional
    public void saveUser(User user){ this.usersRepository.save(user); }

    @Transactional
    public User getById(int id){ return this.usersRepository.findOne(id); }

    @Transactional
    public void deleteUser(int id){ this.usersRepository.delete(id); }
}
