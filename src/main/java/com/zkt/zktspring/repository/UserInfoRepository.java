package com.zkt.zktspring.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zkt.zktspring.core.TableManager;
import com.zkt.zktspring.sdk.commands.UserInfo;

import java.util.ArrayList;

@Repository
public class UserInfoRepository {

    private TableManager tableManager;

    public UserInfoRepository() {
        this.tableManager = new TableManager("user");
    }

    public List<UserInfo> getAllUsers() {

        List<Map<String, Object>> users = tableManager.getAll();
        List<UserInfo> userList = new ArrayList<>();

        for (Map<String, Object> user : users) {
            UserInfo newUser = UserInfo.convertMapToUser(user);

            userList.add(newUser);
        }

        return userList;

    }

    public UserInfo getUserById(Long id) {

        Map<String, Object> userRow = tableManager.getById(id);

        if (userRow == null) {
            return null;
        }

        UserInfo user = UserInfo.convertMapToUser(userRow);
        return user;

    }

    public UserInfo getUserByUsername(String username) {
        List<Map<String, Object>> user = tableManager
                .executeFetchQuery("SELECT * FROM user WHERE username = '" + username + "'");
        if (user.isEmpty()) {
            return null;
        }

        UserInfo newUser = UserInfo.convertMapToUser(user.get(0));

        return newUser;
    }

    public int insertUser(UserInfo user) {
        Map<String, Object> userData = UserInfo.convertUserToMap(user);

        userData.remove("id");

        return tableManager.insert(userData);
    }

    public int insertMultipleUsers(List<UserInfo> users) {
        List<Map<String, Object>> userData = new ArrayList<>();

        List<Map<String, Object>> allUsers = tableManager.getAll();

        for (UserInfo user : users) {
            Map<String, Object> userMap = UserInfo.convertUserToMap(user);
            boolean userExists = false;

            for (Map<String, Object> existingUser : allUsers) {
                if (existingUser.get("uid").equals(userMap.get("uid"))) {
                    userExists = true;
                    break;
                }
            }

            if (!userExists) {
                userData.add(userMap);
            }
        }

        return tableManager.insertMultiple(userData);

    }

    public int updateUser(UserInfo user, Long id) {
        Map<String, Object> userData = UserInfo.convertUserToMap(user);

        userData.remove("id");

        return tableManager.update(userData, id);
    }

    public int deleteUser(Long id) {
        return tableManager.delete(id);
    }
}
