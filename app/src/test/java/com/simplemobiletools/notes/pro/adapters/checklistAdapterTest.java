package com.simplemobiletools.notes.pro.adapters;

import static org.junit.Assert.*;

import com.simplemobiletools.commons.activities.BaseSimpleActivity;
import com.simplemobiletools.commons.views.MyRecyclerView;
import com.simplemobiletools.notes.pro.activities.MainActivity;
import com.simplemobiletools.notes.pro.interfaces.ChecklistItemsListener;
import com.simplemobiletools.notes.pro.models.ChecklistItem;

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
public class checklistAdapterTest {

    private ChecklistAdapter adapter;

    private ChecklistItemsListener listener;
    private MyRecyclerView recycler;
    private Function1<Object, Unit> callback;
    private ActivityController<MainActivity> controller;
    private MainActivity activity;


    private ChecklistItem itemA;
    private ChecklistItem itemB;
    private ChecklistItem itemC;

    @Before
    public void setup() {
        controller = Robolectric.buildActivity(MainActivity.class).setup();
        activity = controller.get();
        listener = mock(ChecklistItemsListener.class);
        AttributeSet attrs = Robolectric.buildAttributeSet().build();
        recycler = new MyRecyclerView(activity, attrs);
        callback = ignored -> Unit.INSTANCE;

        itemA = new ChecklistItem(1, 0L, "A", false);
        itemB = new ChecklistItem(2, 0L, "B", false);
        itemC = new ChecklistItem(3, 0L, "C", false);

        ArrayList<ChecklistItem> list = new ArrayList<>(Arrays.asList(itemA, itemB, itemC));

        adapter = new ChecklistAdapter(activity, list, listener, recycler, true, callback);
    }


    @Test
    public void testGetSelectableItemCountAndGetItemCount() {
        assertEquals(3, adapter.getSelectableItemCount());
        assertEquals(3, adapter.getItemCount());
    }

    @Test
    public void testGetIsItemSelectable() {
        assertTrue(adapter.getIsItemSelectable(0));
        assertTrue(adapter.getIsItemSelectable(1));
    }

    @Test
    public void testGetItemSelectionKey() {
        assertEquals(Integer.valueOf(1), adapter.getItemSelectionKey(0));
        assertNull(adapter.getItemSelectionKey(10));
    }

    @Test
    public void testGetItemKeyPosition() {
        assertEquals(0, adapter.getItemKeyPosition(1));
        assertEquals(1, adapter.getItemKeyPosition(2));
        assertEquals(-1, adapter.getItemKeyPosition(99));
    }

    @Test
    public void testOnRowMoved_forwardSwap() {
        adapter.onRowMoved(0, 2);
        assertEquals(Integer.valueOf(2), adapter.getItemSelectionKey(0));
        assertEquals(Integer.valueOf(3), adapter.getItemSelectionKey(1));
        assertEquals(Integer.valueOf(1), adapter.getItemSelectionKey(2));
    }

    @Test
    public void testOnRowMoved_backwardSwap() {
        adapter.onRowMoved(2, 0);
        assertEquals(Integer.valueOf(3), adapter.getItemSelectionKey(0));
        assertEquals(Integer.valueOf(1), adapter.getItemSelectionKey(1));
        assertEquals(Integer.valueOf(2), adapter.getItemSelectionKey(2));
    }

    @Test
    public void testOnRowMoved_noChange() {
        adapter.onRowMoved(1, 1);
        assertEquals(Integer.valueOf(1), adapter.getItemSelectionKey(0));
        assertEquals(Integer.valueOf(2), adapter.getItemSelectionKey(1));
        assertEquals(Integer.valueOf(3), adapter.getItemSelectionKey(2));
    }
}
