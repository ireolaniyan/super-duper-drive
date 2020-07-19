package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
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
        String message;
        if (!ObjectUtils.isEmpty(note.getNoteId())){
            int rowsUpdated = noteService.updateNote(note);
            if (rowsUpdated < 0) {
                message = "An error occurred while updating note. Please try again.";
                model.addAttribute("error", message);
            } else {
                message = "Successfully updated note.";
                model.addAttribute("success", message);
            }
        } else {
           int rowsAdded = noteService.addNote(auth, note);
           if (rowsAdded < 0) {
               message = "An error occurred while adding note. Please try again.";
               model.addAttribute("error", message);
           } else {
               message = "Successfully added note.";
               model.addAttribute("success", message);
           }
        }

        model.addAttribute("notes", noteService.getNotes(auth));
        return new ModelAndView("result", model);
    }

    @GetMapping("/delete/{noteId}")
    public ModelAndView deleteNote(@PathVariable("noteId") int noteId, Authentication auth, ModelMap model) {
        String message = "Successfully deleted note.";
        noteService.deleteNote(noteId);
        model.addAttribute("successfulDelete", message);
        model.addAttribute("notes", noteService.getNotes(auth));
        return new ModelAndView("result", model);
    }
}
