package com.example.tomatomall.serviceImpl;

import com.example.tomatomall.exception.TomatomallException;
import com.example.tomatomall.po.Account;
import com.example.tomatomall.repository.AccountRepository;
import com.example.tomatomall.service.AccountService;
import com.example.tomatomall.util.SecurityUtil;
import com.example.tomatomall.util.TokenUtil;
import com.example.tomatomall.vo.AccountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TokenUtil tokenUtil;
    @Autowired
    SecurityUtil securityUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public String register(AccountVO accountVO) {
        Account account = accountRepository.findByUsername(accountVO.getUsername());
        if (account != null) {
            throw TomatomallException.usernameAlreadyExists();
        }
        Account newAccount = accountVO.toPO();
        String rawPassword = accountVO.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        newAccount.setPassword(encodedPassword);
        accountRepository.save(newAccount);
        return "注册成功";
    }

    @Override
    public String login(String username, String password) {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw TomatomallException.usernameOrPasswordError();
        }
        if(!passwordEncoder.matches(password, account.getPassword())){
            throw TomatomallException.usernameOrPasswordError();
        }
        return tokenUtil.getToken(account);
    }

    @Override
    public AccountVO getInformation(String username) {
        Account account = accountRepository.findByUsername(username);
        return account.toVO();
    }

    @Override
    public String updateInformation(AccountVO accountVO) {
        Account account=securityUtil.getCurrentAccount();
        if (accountVO.getUsername()!=null){
            account.setUsername(accountVO.getUsername());
        }
        if (accountVO.getPassword()!=null){
            String encodedPassword = passwordEncoder.encode(accountVO.getPassword());
            account.setPassword(encodedPassword);
        }
        if (accountVO.getName()!=null){
            account.setName(accountVO.getName());
        }
        if (accountVO.getAvatar()!=null){
            account.setAvatar(accountVO.getAvatar());
        }
        if (accountVO.getTelephone()!=null){
            account.setTelephone(accountVO.getTelephone());
        }
        if (accountVO.getEmail()!=null){
            account.setEmail(accountVO.getEmail());
        }
        if (accountVO.getLocation()!=null){
            account.setLocation(accountVO.getLocation());
        }
        accountRepository.save(account);
        return "更新成功";
    }
}
