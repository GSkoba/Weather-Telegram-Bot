package com.skobelev.model;


import javax.persistence.*;

@Entity
@Table(name = "bot_user")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "user_city")
    private String userCity;

    @Column(name = "count_resp")
    private Integer count;

    public User(){}

    public User(Integer id, Long chatId, String userCity,
                Integer count){
        this.id = id;
        this.chatId = chatId;
        this.userCity = userCity;
        this.count = count;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
