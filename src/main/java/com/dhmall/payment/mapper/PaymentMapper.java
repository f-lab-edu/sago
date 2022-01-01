package com.dhmall.payment.mapper;

import com.dhmall.payment.dto.PaymentDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PaymentMapper {
    void insertPayment(PaymentDto paymentDto);
}
