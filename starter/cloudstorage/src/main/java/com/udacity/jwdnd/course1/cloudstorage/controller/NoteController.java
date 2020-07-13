package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/notes")
public class NoteController {
    private NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping()
    public ModelAndView getFilesPage(Authentication auth, ModelMap model) {
        model.addAttribute("notes", noteService.getNotes(auth));
        return new ModelAndView("redirect:/home", model);
    }

    @PostMapping("/create")
    public ModelAndView createNote(Note note, Authentication auth, ModelMap model) {
        noteService.addNote(auth, note);
        model.addAttribute("notes", noteService.getNotes(auth));
        return new ModelAndView("redirect:/home", model);
    }
}
