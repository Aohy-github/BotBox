package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.registerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements registerService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String username, String password, String confirmPassword) {
        Map<String, String> map = new HashMap<>();
        System.out.println(username + " " + password + " " + confirmPassword);
        if(username == null || password == null || confirmPassword == null) {
            map.put("error_message", "input is empty!");
            return map;
        }
        username = username.trim();
        password = password.trim();
        confirmPassword = confirmPassword.trim();
        if(username.length() == 0 || password.length() == 0 || confirmPassword.length() == 0) {
            map.put("error_message" , "input is empty!");
            return map;
        }

        if(!password.equals(confirmPassword)) {
            map.put("error_message","second password is not confirm");
            return map;
        }

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        List<User> list = userMapper.selectList(wrapper);
        if(!list.isEmpty()) {
            map.put("error_message" , "username is exist");
            return map;
        }
        String encodePassword = passwordEncoder.encode(password);
        String photo = "https://cdn.acwing.com/media/user/profile/photo/12421_lg_12b5ca1256.jpg";
        User user = new User(null , username , encodePassword , photo, 1500);
        userMapper.insert(user);
        map.put("error_message", "success");
        return map;
    }
}
