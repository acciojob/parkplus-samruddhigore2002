package com.driver.services.impl;

import com.driver.exceptions.InsufficientAmountException;
import com.driver.exceptions.NoReservationFoundException;
import com.driver.exceptions.PaymentModeNotDetectedException;
import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.model.Spot;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Optional<Reservation> optionalReservation = reservationRepository2.findById(reservationId);
        if(!optionalReservation.isPresent()){
            throw new NoReservationFoundException("No reservation found");
        }

        Reservation reservation = optionalReservation.get();
        int hours = reservation.getNumberOfHours();
        int pricePerHour = reservation.getSpot().getPricePerHour();
        int totalAmount = hours*pricePerHour;

        if(totalAmount>amountSent){
            throw new InsufficientAmountException("insufficient amount");
        }
        Payment payment = new Payment();

        if(mode.equals("CASH")){
            payment.setPaymentMode(PaymentMode.CASH);
        } else if(mode.equals("CARD")){
            payment.setPaymentMode(PaymentMode.CARD);
        } else if(mode.equals("UPI")){
            payment.setPaymentMode(PaymentMode.UPI);
        }else{
            throw new PaymentModeNotDetectedException("Invalid payment mode");
        }

        payment.setPaymentCompleted(Boolean.TRUE);
        payment.setReservation(reservation);
        paymentRepository2.save(payment);
        return payment;
    }
}
