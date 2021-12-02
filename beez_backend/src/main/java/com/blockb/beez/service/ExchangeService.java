package com.blockb.beez.service;

import java.util.concurrent.ExecutionException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import com.blockb.beez.dao.TransactionDao;
import com.blockb.beez.dao.UserDao;
import com.blockb.beez.dto.ContractCADto;
import com.blockb.beez.dto.UserDto;
import com.blockb.beez.exception.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint128;


@Service
public class ExchangeService {
    private TransactionDao transactionDao;

    @Autowired
    UserDao userDao;
    ContractCADto addressDto = new ContractCADto();

    public ExchangeService(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    //소상공인 토큰 환전
    public List<String> exchange(String userAddress, Long userId, int amount) throws IOException, ExecutionException, InterruptedException {
        
        String contract = addressDto.getPaymentCA();
        Function function = new Function("exchange",
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
        // Map<String, String> history = new HashMap<String, String>();
        // history.put("userAddress", userAddress);
        // history.put("amount", String.valueOf(amount));
        // history.put("txHash", txHash);
        // historyDao.exchangeHistory(history);

        return transaction;
    }
    public UserDto findByUserAccount(String email) {
        return userDao.findByUserAccount(email)
                .orElseThrow(() -> new UserNotFoundException("없는 유저입니다."));
    } 
}
