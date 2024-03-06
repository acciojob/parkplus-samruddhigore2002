package com.driver.services.impl;

import com.driver.exceptions.NoParkingLotFoundException;
import com.driver.exceptions.NoSpotFoundException;
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
        if(!optionalParkingLot.isPresent()){
            throw new NoParkingLotFoundException("There is no parking lot available by this id");
        }

        ParkingLot parkingLot = optionalParkingLot.get();

        Spot spot = new Spot();
        spot.setParkingLot(parkingLot);
        if(numberOfWheels>4){
            spot.setSpotType(SpotType.OTHERS);
        }else if(numberOfWheels>2){
            spot.setSpotType(SpotType.FOUR_WHEELER);
        }else{
            spot.setSpotType(SpotType.TWO_WHEELER);
        }

        spot.setPricePerHour(pricePerHour);
        spot.setOccupied(Boolean.FALSE);

        List<Spot> spots = parkingLot.getSpotList();
        spots.add(spot);
        Spot savedSpot = spotRepository1.save(spot);
        ParkingLot savedParkingLot = parkingLotRepository1.save(parkingLot);

        return savedSpot;
    }

    @Override
    public void deleteSpot(int spotId) {

        Optional<Spot> optionalSpot = spotRepository1.findById(spotId);
        if(!optionalSpot.isPresent()){
            throw new NoSpotFoundException("There is no spot available by this id in any parking lot");
        }

        Spot spot = optionalSpot.get();
        ParkingLot parkingLot = spot.getParkingLot();

        List<Spot> spots = parkingLot.getSpotList();

        for (int i = 0; i < spots.size(); i++) {
            Spot spot1 = spots.get(i);
            if (spot1.getId() == spotId) {
                spots.remove(i);
                spotRepository1.delete(spot1);
            }
        }
        parkingLot.setSpotList(spots);
        parkingLotRepository1.save(parkingLot);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        Optional<Spot> optionalSpot = spotRepository1.findById(spotId);
        if(!optionalSpot.isPresent()){
            throw new NoSpotFoundException("There is no spot available by this id in any parking lot");
        }

        Spot spot = optionalSpot.get();

        ParkingLot parkingLot = spot.getParkingLot();
        List<Spot> spots = parkingLot.getSpotList();

        for (int i = 0; i < spots.size(); i++) {
            Spot spot1 = spots.get(i);
            if (spot1.getId() == spotId) {
                spots.remove(i);
            }
        }

        // set new details
        spot.setPricePerHour(pricePerHour);

        // add new spot into the list
        spots.add(spot);
        parkingLot.setSpotList(spots);
        parkingLotRepository1.save(parkingLot);
        return spot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        Optional<ParkingLot> optionalParkingLot = parkingLotRepository1.findById(parkingLotId);
        if(!optionalParkingLot.isPresent()){
            throw new NoParkingLotFoundException("There is no parking lot available by this id to delete");
        }

        ParkingLot parkingLot = optionalParkingLot.get();
        parkingLotRepository1.delete(parkingLot);
    }
}
