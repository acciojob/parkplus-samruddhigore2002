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

        ParkingLot parkingLot;
        try{
            parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        }catch(Exception e){
            throw new RuntimeException();
        }

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
        spot.setOccupied(false);

        List<Spot> spots = parkingLot.getSpotList();
        spots.add(spot);
        parkingLot.setSpotList(spots);

        Spot savedSpot = spotRepository1.save(spot);
        ParkingLot savedParkingLot = parkingLotRepository1.save(parkingLot);

        return savedSpot;
    }

    @Override
    public void deleteSpot(int spotId) {

        Spot spot;
        try{
            spot = spotRepository1.findById(spotId).get();
        } catch (Exception e){
            throw new RuntimeException();
        }
        ParkingLot parkingLot = spot.getParkingLot();

        List<Spot> spots = parkingLot.getSpotList();

        for (int i = 0; i < spots.size(); i++) {
            Spot spot1 = spots.get(i);
            if (spot1.getId() == spotId) {
                spots.remove(i);
                spotRepository1.deleteById(spotId);
            }
        }
        parkingLot.setSpotList(spots);
        parkingLotRepository1.save(parkingLot);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        Spot spot;
        try{
            spot = spotRepository1.findById(spotId).get();
        } catch (Exception e){
            throw new RuntimeException();
        }

        ParkingLot parkingLot = spot.getParkingLot();
        List<Spot> spots = parkingLot.getSpotList();

        for (Spot spot1: spots) {
            if (spot1.getId() == spotId) {
                spot = spot1;
            }
        }

        // set new details
        spot.setPricePerHour(pricePerHour);

        spotRepository1.save(spot);
        return spot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        Optional<ParkingLot> optionalParkingLot = parkingLotRepository1.findById(parkingLotId);
        if(!optionalParkingLot.isPresent()){
            throw new RuntimeException();
        }

        ParkingLot parkingLot = optionalParkingLot.get();
        parkingLotRepository1.delete(parkingLot);
    }
}
