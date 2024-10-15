package org.example;
import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String chatId;
    private String mail;
    private String name;

    public User(String chatId, String mail, String name) {
    }
}