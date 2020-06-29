package com.phones.phones.service;

import com.phones.phones.dto.CityTopDto;
import com.phones.phones.exception.city.CityAlreadyExistException;
import com.phones.phones.exception.city.CityDoesNotExistException;
import com.phones.phones.exception.user.UserDoesNotExistException;
import com.phones.phones.model.City;
import com.phones.phones.model.User;
import com.phones.phones.repository.CityRepository;
import com.phones.phones.repository.UserRepository;
import com.phones.phones.repository.dto.CityTopDtoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    private final CityRepository cityRepository;
    private final CityTopDtoRepository cityTopDtoRepository;
    private final UserRepository userRepository;

    @Autowired
    public CityService(final CityRepository cityRepository,
                       CityTopDtoRepository cityTopDtoRepository, final UserRepository userRepository) {
        this.cityRepository = cityRepository;
        this.cityTopDtoRepository = cityTopDtoRepository;
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

    public City findById(Long id) throws CityDoesNotExistException {
        Optional<City> city = cityRepository.findById(id);
        if (city.isEmpty()) {
            throw new CityDoesNotExistException();
        }
        return city.get();
    }

/*    public List<CityTop> findTopCitiesCallsByUserId(Long id) throws UserDoesNotExistException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserDoesNotExistException();
        }
        return cityRepository.findCitiesTopByUserId(id);
    }*/


    public List<CityTopDto> findTopCitiesCallsByUserId(Long id) throws UserDoesNotExistException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserDoesNotExistException();
        }
        return cityTopDtoRepository.findCitiesTopByUserId(id);
    }

}
