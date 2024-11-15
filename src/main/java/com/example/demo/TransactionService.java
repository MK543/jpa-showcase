package com.example.demo;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @SneakyThrows
    public void doTransaction(TransactionDTO transactionDTO) {
        Thread.sleep(2000);
        UserEntity sender = userRepository.findById(transactionDTO.getSenderId()).orElseThrow();
        sender.setName("update");
        UserEntity recipient = userRepository.findById(transactionDTO.getRecipientId()).orElseThrow();

        Transaction transaction = Transaction.builder().id(UUID.randomUUID())
                .amount(transactionDTO.getAmount())
                .sender(sender)
                .recipient(recipient)
                .build();

        transactionRepository.save(transaction);
    }

    public List<TransactionDTO> getTransactions(Long id) {
        List<Transaction> transactions = transactionRepository.findBySenderId(id);
        return transactions.stream().map(transaction -> TransactionDTO.builder()
                        .recipientId(transaction.getRecipient().getId())
                        .senderId(transaction.getSender().getId())
                        .amount(transaction.getAmount()).build())
                .toList();
    }
}
