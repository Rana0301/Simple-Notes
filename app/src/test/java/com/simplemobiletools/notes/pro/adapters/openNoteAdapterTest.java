package com.simplemobiletools.notes.pro.adapters;

import static com.simplemobiletools.commons.helpers.ConstantsKt.PROTECTION_NONE;
import static org.junit.Assert.*;

import com.simplemobiletools.commons.activities.BaseSimpleActivity;
import com.simplemobiletools.commons.views.MyRecyclerView;
import com.simplemobiletools.notes.pro.activities.MainActivity;
import com.simplemobiletools.notes.pro.interfaces.ChecklistItemsListener;
import com.simplemobiletools.notes.pro.models.ChecklistItem;
import com.simplemobiletools.notes.pro.models.Note;
import com.simplemobiletools.notes.pro.models.NoteType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;


import kotlin.Unit;
import static org.mockito.Mockito.*;

import android.app.Activity;
import android.util.AttributeSet;

import kotlin.jvm.functions.Function1;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33, manifest = Config.NONE)
public class openNoteAdapterTest {

    private BaseSimpleActivity activity;
    private MyRecyclerView recyclerView;
    private Note noteA, noteB, noteC;
    private OpenNoteAdapter adapter;
    private ActivityController<MainActivity> controller;
    private Function1<Object, Unit> callback;



    @Before
    public void setup() {

        controller = Robolectric.buildActivity(MainActivity.class).setup();
        activity = controller.get();
        AttributeSet attrs = Robolectric.buildAttributeSet().build();
        recyclerView = new MyRecyclerView(activity, attrs);
        callback = ignored -> Unit.INSTANCE;

        noteA = new Note(1L, "Title A", "Hello world", NoteType.TYPE_TEXT, "", PROTECTION_NONE, "");
        noteB = new Note(2L, "Title B", "More text", NoteType.TYPE_TEXT, "", PROTECTION_NONE, "");
        noteC = new Note(3L, "Title C", "Another", NoteType.TYPE_TEXT, "", PROTECTION_NONE, "");

        adapter = new OpenNoteAdapter(activity, Arrays.asList(noteA, noteB, noteC), recyclerView, callback);
    }

    @Test
    public void testGetItemCount() {
        assertEquals(3, adapter.getItemCount());
    }

    @Test
    public void testGetSelectableItemCount() {
        assertEquals(3, adapter.getSelectableItemCount());
    }


    @Test
    public void testGetItemSelectionKey() {
        assertEquals(Integer.valueOf(1), adapter.getItemSelectionKey(0));
        assertNull(adapter.getItemSelectionKey(999));
    }

    @Test
    public void testGetItemKeyPosition() {
        assertEquals(0, adapter.getItemKeyPosition(1));
        assertEquals(1, adapter.getItemKeyPosition(2));
        assertEquals(-1, adapter.getItemKeyPosition(999));
    }
}

