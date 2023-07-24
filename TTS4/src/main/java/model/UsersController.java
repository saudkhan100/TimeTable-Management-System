package model;

import common.dto.CredentialDTO;

import java.net.ResponseCache;

public class UsersController {

    public Object validateAdminLogin(CredentialDTO credentialDTO) {

        String tempEmail = credentialDTO.email;
        String tempPassword = credentialDTO.passwor;


        if(!tempEmail.contains("@") || !tempEmail.contains(".")){

            return null;
        }
        if(tempPassword.length()<5){
            return null;
        }



        return new Object();
    }
}
