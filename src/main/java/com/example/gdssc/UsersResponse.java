package com.example.gdssc;

import java.util.List;

public class UsersResponse {
    List<User> results;

    public UsersResponse(List<User> results) {
        this.results = results;
    }

    public List<User> getResults() {
        return results;
    }
}
