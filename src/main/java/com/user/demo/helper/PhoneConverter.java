package com.user.demo.helper;

import com.user.demo.dto.PhoneDTO;
import com.user.demo.model.Phone;
import org.springframework.stereotype.Component;

@Component
public class PhoneConverter {
    public Phone convertToEntity(PhoneDTO phoneDTO) {
        Phone phone = new Phone();
        phone.setNumber(phoneDTO.number());
        phone.setCitycode(phoneDTO.citycode());
        phone.setCountrycode(phoneDTO.countrycode());
        return phone;
    }
}


