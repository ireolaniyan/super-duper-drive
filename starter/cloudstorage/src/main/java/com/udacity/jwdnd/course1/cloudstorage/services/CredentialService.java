package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.PostConstruct;
import javax.validation.metadata.CrossParameterDescriptor;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final UserMapper userMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, UserMapper userMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.userMapper = userMapper;
        this.encryptionService = encryptionService;
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("creating CredentialService bean");
    }

    public List<Credential> getCredentials(Authentication auth) {
        User user = userMapper.getUser(auth.getName());
        return credentialMapper.getUserStoredCredentials(user.getUserId());
    }

    public void addCredential(Authentication auth, @ModelAttribute Credential credential) {
        User user = userMapper.getUser(auth.getName());

        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        credentialMapper.addCredential(new Credential(null, credential.getUrl(), credential.getUserName(), encodedKey, encryptedPassword, user.getUserId()));
    }

    public void updateCredential(Credential credentialUpdate) {
        Credential credential = credentialMapper.getCredentialById(credentialUpdate.getCredentialId());
        String key = credential.getKey();

        credentialUpdate.setUrl(credentialUpdate.getUrl());
        credentialUpdate.setUserName(credentialUpdate.getUserName());
        credentialUpdate.setPassword(encryptionService.encryptValue(credentialUpdate.getPassword(), key));
        credentialMapper.updateCredential(credentialUpdate);
    }

    public String getDecryptedPassword(Integer credentialId) {
        Credential credential = credentialMapper.getCredentialById(credentialId);
        return encryptionService.decryptValue(credential.getPassword(), credential.getKey());
    }
}
