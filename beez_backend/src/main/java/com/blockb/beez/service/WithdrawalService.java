package com.blockb.beez.service;

import java.util.concurrent.ExecutionException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.blockb.beez.dao.TransactionDao;
import com.blockb.beez.dao.UserDao;
import com.blockb.beez.dao.WithdrawalDao;
import com.blockb.beez.dto.ContractCADto;
import com.blockb.beez.dto.UserDto;
import com.blockb.beez.dto.WithdrawalHistoryDto;
import com.blockb.beez.exception.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint128;

@Service
public class WithdrawalService {
    private TransactionDao transactionDao;
    @Autowired
    UserDao userDao;
    @Autowired
    WithdrawalDao withDrawalDao;
    ContractCADto addressDto = new ContractCADto();

    public WithdrawalService(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }
    
    //소상공인 토큰 출금
    public List<String> withdrawal(String userAddress, Long userId, int amount) throws IOException, ExecutionException, InterruptedException {
        
        String contract = addressDto.getWonTokenCA();
        Function function = new Function("withDraw",
                                         Arrays.asList(new Address(userAddress), new Uint128(amount)),
                                         Collections.emptyList());
        
        // 2. sendTransaction
        String txHash = transactionDao.ethSendTransaction(function, contract, userId);

        //return Hash값
        List<String> transaction = new ArrayList<>();
        transaction.add(txHash);

        // 7. getReceipt
        transactionDao.getReceipt(txHash);

        // 8. DB CHARGE HISTORY 남기기
        Map<String, String> history = new HashMap<String, String>();
        history.put("userAddress", userAddress);
        history.put("amount", String.valueOf(amount));
        history.put("txHash", txHash);
        withDrawalDao.withdrawalHistory(history);

        return transaction;
    }
    //소상공인 계좌 찾기
    public UserDto findByUserAccount(String email) {
        return userDao.findByUserAccount(email)
                .orElseThrow(() -> new UserNotFoundException("없는 유저입니다."));
    }
    //소상공인 출금 내역 list
    public List<WithdrawalHistoryDto> withdrawHistoryList(Long userId, String startDate, String endDate) {
        return withDrawalDao.withdrawHistoryList(userId, startDate, endDate);
    }
}
