package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/credentials")
public class CredentialController {
    private CredentialService credentialService;

    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @GetMapping()
    public ModelAndView getCredentials(Authentication auth, ModelMap model) {
        model.addAttribute("credentials", credentialService.getCredentials(auth));
        return new ModelAndView("redirect:/home", model);
    }

    @PostMapping("/create")
    public ModelAndView createCredential(Credential credential, Authentication auth, ModelMap model) {
        if (!ObjectUtils.isEmpty(credential.getCredentialId())){
            credentialService.updateCredential(credential);
        } else {
            credentialService.addCredential(auth, credential);
        }
        model.addAttribute("credentials", credentialService.getCredentials(auth));
        return new ModelAndView("redirect:/home", model);
    }

    @GetMapping("/decrypt-credential/{credentialId}")
    @ResponseBody
    public List<String> decryptCredential(@PathVariable("credentialId") Integer credentialId){
        return Arrays.asList(credentialService.getDecryptedPassword(credentialId));
    }

    @GetMapping("/delete/{credentialId}")
    public ModelAndView deleteNote(@PathVariable("credentialId") int credentialId, Authentication auth, ModelMap model) {
        credentialService.deleteCredential(credentialId);
        model.addAttribute("credentials", credentialService.getCredentials(auth));
        return new ModelAndView("redirect:/home", model);
    }
}
