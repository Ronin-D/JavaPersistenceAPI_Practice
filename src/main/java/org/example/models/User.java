package org.example.models;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.util.List;


@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    public User() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public User(String login, int balance) {
        this.login = login;
        this.balance = balance;
    }
    private String login;
    private int balance = 0;

}
