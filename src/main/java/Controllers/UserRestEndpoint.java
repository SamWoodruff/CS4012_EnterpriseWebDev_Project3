package Controllers;

import config.annotation.RestEndpoint;
import entities.User;
import exception.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;
@RestEndpoint
public class UserRestEndpoint {

    @Inject DatabaseAccessObject dao;



    @RequestMapping(value = "user/{user}", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> discover(@PathVariable("user") int userId)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Allow", "OPTIONS,GET,POST,DELETE");
        return new ResponseEntity<>(null, headers, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "user/{userId}", method = RequestMethod.GET)
    public ResponseEntity<User> getUserById(@PathVariable("userId") int userId){
        User user = dao.getById(userId);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(user, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "user/{userId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserById(@RequestBody UserForm form, @PathVariable("userId") int userId){
        User user = dao.getById(userId);
        User temp = new User();
        temp.setUsersId(user.getUsersId());
        temp.setUsername(form.getUsername());
        temp.setPassword(form.getPassword());
        temp.setFirstName(form.getFirstname());
        temp.setLastName(form.getLastname());
        temp.setLastUpdated(new Date());

        if(!temp.getUsername().equals("")) {
            user.setUsername(temp.getUsername());
        }
        user.setPassword(temp.getPassword());
        user.setFirstName(temp.getFirstName());
        user.setLastName(temp.getLastName());
        user.setLastUpdated(new Date());
        dao.saveUser(user);
    }

    @RequestMapping(value = "user/{userId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity deleteUserById(@PathVariable("userId") int userId,@RequestBody SignInForm form){
        if(this.dao.getById(userId) == null)
            throw new ResourceNotFoundException();

        if(form.getAdminusername().equals("admin") && form.getAdminpassword().equals("admin")) {
            dao.deleteUser(userId);
        }
        else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "user", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> discover()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Allow", "OPTIONS,GET,POST");
        return new ResponseEntity<>(null, headers, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value="user", method= RequestMethod.POST)
    public ResponseEntity<User> create(@RequestBody UserForm form)
    {
        User user = new User();
        user.setUsername(form.getUsername());
        user.setPassword(form.getPassword());
        user.setFirstName(form.getFirstname());
        user.setLastName(form.getLastname());
        user.setLastUpdated(new Date());
        dao.saveUser(user);
        String uri = ServletUriComponentsBuilder.fromCurrentServletMapping()
                .path("/user/{id}").buildAndExpand(user.getUsersId()).toString();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", uri);

        return new ResponseEntity<>(user, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public ResponseEntity<List<User>> listUsers()
    {
       List<User> users = dao.getUsers();
       return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }


}
