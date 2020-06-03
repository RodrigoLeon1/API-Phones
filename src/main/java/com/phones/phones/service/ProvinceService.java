package com.phones.phones.service;

import com.phones.phones.exception.province.ProviceAlreadyExistException;
import com.phones.phones.exception.province.ProvinceNotExistException;
import com.phones.phones.model.Province;
import com.phones.phones.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProvinceService {

    private final ProvinceRepository provinceRepository;

    @Autowired
    public ProvinceService(final ProvinceRepository provinceRepository) {
        this.provinceRepository = provinceRepository;
    }


    public Province create(Province newProvince) throws ProviceAlreadyExistException {
        Optional<Province> province = provinceRepository.findByName(newProvince.getName());
        if (province.isPresent()) {
            throw new ProviceAlreadyExistException();
        }
        return provinceRepository.save(newProvince);
    }

    public List<Province> findAll() {
        return provinceRepository.findAll();
    }

    public Province findById(Long id) throws ProvinceNotExistException {
        Optional<Province> province = provinceRepository.findById(id);
        if (province.isEmpty()) {
            throw new ProvinceNotExistException();
        }
        return province.get();
    }

}
