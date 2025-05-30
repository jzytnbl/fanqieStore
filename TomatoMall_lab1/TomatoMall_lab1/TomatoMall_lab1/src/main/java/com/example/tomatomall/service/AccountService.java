package com.example.tomatomall.service;

import com.example.tomatomall.vo.AccountVO;

public interface AccountService {
    String register(AccountVO accountVO);

    String login(String username,String password);

    AccountVO getInformation(String username);

    String updateInformation(AccountVO accountVO);
}
