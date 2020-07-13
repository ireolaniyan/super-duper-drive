package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;
    private final UserMapper userMapper;

    public NoteService(NoteMapper noteMapper, UserMapper userMapper) {
        this.noteMapper = noteMapper;
        this.userMapper = userMapper;
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("creating NoteService bean");
    }

    public List<Note> getNotes(Authentication auth) {
        User user = userMapper.getUser(auth.getName());
        return noteMapper.getUserNotes(user.getUserId());
    }

    public void addNote(Authentication auth, @ModelAttribute Note note) {
        User user = userMapper.getUser(auth.getName());
        noteMapper.addNote(new Note(null, note.getNoteTitle(), note.getNoteDescription(), user.getUserId()));
    }

    public void updateNote(Note noteUpdate) {
        noteUpdate.setNoteTitle(noteUpdate.getNoteTitle());
        noteUpdate.setNoteDescription(noteUpdate.getNoteDescription());
        noteMapper.updateNote(noteUpdate);
    }
}
