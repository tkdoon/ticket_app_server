package com.tkdoon.ticket_app.service;


import com.tkdoon.ticket_app.entity.UserEntity;
import com.tkdoon.ticket_app.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// 例として、UserRepositoryはJPAのリポジトリと仮定
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // あなたのユーザー取得用リポジトリ

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // データベースからユーザーを検索
        List<UserEntity> users = userRepository.selectExistingUser(username);
        if (users.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        users.get(0).checkAdmin();
        // UserDetailsを返す（SpringのUserクラスや独自クラスでもOK）
        return new org.springframework.security.core.userdetails.User(
                users.get(0).getUserName(),
                "",

                /* 権限リスト */ users.get(0).getAuthorities()
        );
    }
}
