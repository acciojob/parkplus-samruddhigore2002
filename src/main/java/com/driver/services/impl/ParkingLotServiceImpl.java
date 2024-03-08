package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);
        return parkingLotRepository1.save(parkingLot);

    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {

        Optional<ParkingLot> optionalParkingLot = parkingLotRepository1.findById(parkingLotId);

        if(!optionalParkingLot.isPresent()) {
            throw new RuntimeException("Invalid Parking lot ID");
        }

        ParkingLot parkingLot = optionalParkingLot.get();
        Spot spot = new Spot();
        spot.setPricePerHour(pricePerHour);
        spot.setOccupied(false);

        if(numberOfWheels>4){
            spot.setSpotType(SpotType.OTHERS);
        }else if(numberOfWheels>2){
            spot.setSpotType(SpotType.FOUR_WHEELER);
        }else{
            spot.setSpotType(SpotType.TWO_WHEELER);
        }

        List<Spot> spots = parkingLot.getSpotList();
        spots.add(spot);
        parkingLot.setSpotList(spots);

        ParkingLot savedParkingLot = parkingLotRepository1.save(parkingLot);
        spot.setParkingLot(savedParkingLot);
        return spot;
    }

    @Override
    public void deleteSpot(int spotId) {

        Optional<Spot> optionalSpot = spotRepository1.findById(spotId);
        if(optionalSpot.isPresent()){
            Spot spot = optionalSpot.get();
            ParkingLot parkingLot = spot.getParkingLot();
            List<Spot> spots = parkingLot.getSpotList();

            for (Spot spot1: spots) {
                if (spot1.getId() == spotId) {
                    spots.remove(spot1);
                    break;
                }
            }
            parkingLot.setSpotList(spots);
            parkingLotRepository1.save(parkingLot);
        }
        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        Optional<ParkingLot> optionalParkingLot = parkingLotRepository1.findById(parkingLotId);
        if(!optionalParkingLot.isPresent()){
            throw new RuntimeException("Invalid ParkingLot ID");
        }
        ParkingLot parkingLot = optionalParkingLot.get();
        List<Spot> spots = parkingLot.getSpotList();
        Spot spot = null;

        for (Spot spot1: spots) {
            if (spot1.getId() == spotId) {
                spot = spot1;
                break;
            }
        }

        if(spot == null){
            throw  new RuntimeException("Invalid spot id");
        }
        // set new details
        spot.setPricePerHour(pricePerHour);

        return spotRepository1.save(spot);
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
//        Optional<ParkingLot> optionalParkingLot = parkingLotRepository1.findById(parkingLotId);
//        if(!optionalParkingLot.isPresent()){
//            throw new RuntimeException();
//        }
//
//        ParkingLot parkingLot = optionalParkingLot.get();
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
