package com.kjq.project.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 *
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUIN = 1L;

    private String userAccount;

    private String userPassword;
}
