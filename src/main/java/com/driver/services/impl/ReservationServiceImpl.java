package com.driver.services.impl;

import com.driver.exceptions.CannotMakeReservation;
import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {

        Optional<User> optionalUser = userRepository3.findById(userId);
        if(!optionalUser.isPresent()){
            throw  new CannotMakeReservation("user not found, can not make reservation");
        }

        User user = optionalUser.get();

        Optional<ParkingLot> optionalParkingLot = parkingLotRepository3.findById(parkingLotId);
        if(!optionalParkingLot.isPresent()){
            throw new CannotMakeReservation("Parking lot not found, cannot make reservation");
        }

        ParkingLot parkingLot = optionalParkingLot.get();
        List<Spot> spots = parkingLot.getSpotList();

//        SpotType spotType;
//        if(numberOfWheels==2){
//            spotType = SpotType.TWO_WHEELER;
//        } else if (numberOfWheels==4) {
//            spotType = SpotType.FOUR_WHEELER;
//        }else {
//            spotType = SpotType.OTHERS;
//        }
        int price = Integer.MAX_VALUE;
        Spot assignedSpot = new Spot();
        int flag = 0;

        for(int i=0; i<spots.size(); i++)
        {
            Spot spot = spots.get(i);
            SpotType spotType1 = spot.getSpotType();
            int wheels;
            if(String.valueOf(spotType1).equals("TWO_WHEELER")){
                wheels = 2;
            } else if (String.valueOf(spotType1).equals("FOUR_WHEELER")) {
                wheels = 4;
            }else {
                wheels = 5;
            }
            int price1 = spot.getPricePerHour() * timeInHours;
            if(wheels >= numberOfWheels){
                if(price1 < price){
                    assignedSpot = spot;
                    price = price1;
                    spot.setOccupied(Boolean.TRUE);
                    flag =1;
                }

            }
        }
        if(flag==0){
            throw new CannotMakeReservation("No spot available, cannot make reservation");

        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setSpot(assignedSpot);
        reservation.setNumberOfHours(timeInHours.intValue());

        reservationRepository3.save(reservation);
        return reservation;
    }
}
