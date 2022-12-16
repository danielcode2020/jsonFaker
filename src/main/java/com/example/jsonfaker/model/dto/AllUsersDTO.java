package com.example.jsonfaker.model.dto;

import com.example.jsonfaker.model.Users;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "usersList")
@XmlAccessorType(XmlAccessType.FIELD)
public class AllUsersDTO {
    @XmlElement(name = "user")
    private List<Users> usersList;

    public List<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
    }
}
