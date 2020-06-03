package com.phones.phones.service;

import com.phones.phones.exception.city.CityAlreadyExistException;
import com.phones.phones.exception.city.CityNotExistException;
import com.phones.phones.exception.user.UserNotExistException;
import com.phones.phones.model.City;
import com.phones.phones.model.User;
import com.phones.phones.projection.CityTop;
import com.phones.phones.repository.CityRepository;
import com.phones.phones.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    private final CityRepository cityRepository;
    private final UserRepository userRepository;

    @Autowired
    public CityService(final CityRepository cityRepository,
                       final UserRepository userRepository) {
        this.cityRepository = cityRepository;
        this.userRepository = userRepository;
    }


    public City create(City newCity) throws CityAlreadyExistException {
        Optional<City> city = cityRepository.findByName(newCity.getName());
        if (city.isPresent()) {
            throw new CityAlreadyExistException();
        }
        return cityRepository.save(newCity);
    }

    public List<City> findAll() {
        return cityRepository.findAll();
    }

    public City findById(Long id) throws CityNotExistException {
        Optional<City> city = cityRepository.findById(id);
        if (city.isEmpty()) {
            throw new CityNotExistException();
        }
        return city.get();
    }

    public List<CityTop> findTopCitiesCallsByUserId(Long id) throws UserNotExistException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotExistException();
        }
        return cityRepository.findCitiesTopByUserId(id);
    }

}
