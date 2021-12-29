package com.example.gdssc;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
public class UserController {
    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/users")
    @ResponseBody
    public ResponseEntity<UsersResponse> getUsers(@RequestParam(name="min", required=false, defaultValue="0") double min,
                                                  @RequestParam(name="max", required=false, defaultValue="4000") double max,
                                                  @RequestParam(name="offset", required=false, defaultValue="0") int offset,
                                                  @RequestParam(name="limit", required=false, defaultValue="") String limit,
                                                  @RequestParam (name="sort", required=false, defaultValue="") String sort) {
        // Validate limit
        int _limit;
        try {
            _limit = Objects.equals(limit, "") ? Integer.MAX_VALUE : Integer.parseInt(limit);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException("Limit must be a valid non-negative integer");
        }

        // Validate sort
        String _sort = sort.toUpperCase();
        List<User> res;
        switch(_sort) {
            case ("NAME"):
                res = repository.findOrderByName(min, max, offset, _limit);
                break;
            case ("SALARY"):
                res = repository.findOrderBySalary(min, max, offset, _limit);
                break;
            case (""):
                res = repository.findOrderByNone(min, max, offset, _limit);
                break;
            default:
                throw new InvalidParameterException("Sort must be of values NAME or SALARY");
        }
        return new ResponseEntity<UsersResponse>(new UsersResponse(res), HttpStatus.OK);
    }

    @RequestMapping(path="/upload", method=RequestMethod.POST, consumes = MULTIPART_FORM_DATA_VALUE )
    public Map<String,String> addUsers(@RequestParam MultipartFile file){
        try {
            List<User> userList = CsvUtils.read(User.class, file.getInputStream());
            List<User> filteredList = userList.stream().filter(user -> user.getSalary()>=0).collect(Collectors.toList());
            repository.saveAll(filteredList);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidParameterException(e.getMessage());
        }

        return Collections.singletonMap("success", "1");
    }
}
