package com.bexchauvet.vin.service;

import com.bexchauvet.vin.rest.dto.MessageDTO;
import com.bexchauvet.vin.rest.dto.TokenDTO;
import com.bexchauvet.vin.rest.dto.UserDTO;

public interface UserService {

    TokenDTO generateToken(UserDTO user);

    MessageDTO create(UserDTO userDTO);
}
