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

        // check if user and parking lot is available or not
        Optional<User> optionalUser = userRepository3.findById(userId);
        if(!optionalUser.isPresent()){
            return null;
        }
        User user = optionalUser.get();

        Optional<ParkingLot> optionalParkingLot = parkingLotRepository3.findById(parkingLotId);
        if(!optionalParkingLot.isPresent()){
            return null;
        }
        ParkingLot parkingLot = optionalParkingLot.get();

        // get the list of all spots present in the parking lot
        List<Spot> spots = parkingLot.getSpotList();

        int price = Integer.MAX_VALUE;
        Spot assignedSpot = null;
        int flag = 0;

        for(Spot spot: spots)
        {
            if(!spot.getOccupied()){
                SpotType spotType1 = spot.getSpotType();
                int wheels;
                if(String.valueOf(spotType1).equals("TWO_WHEELER")){
                    wheels = 2;
                } else if (String.valueOf(spotType1).equals("FOUR_WHEELER")) {
                    wheels = 4;
                }else {
                    wheels = 5;
                }

                if(wheels >= numberOfWheels){
                    int price1 = spot.getPricePerHour() * timeInHours;
                    if(price1 < price){
                        assignedSpot = spot;
                        price = price1;
                        flag =1;
                    }

                }
            }

        }
        if(flag==0){
            throw new Exception("No spot available, cannot make reservation");
        }

        assignedSpot.setOccupied(true);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setSpot(assignedSpot);
        reservation.setNumberOfHours(timeInHours);

        assignedSpot.getReservationList().add(reservation);
        user.getReservationList().add(reservation);

        userRepository3.save(user);
        spotRepository3.save(assignedSpot);
        return reservation;
    }
}
