package com.simplemobiletools.notes.pro.activities;

import static com.simplemobiletools.commons.helpers.ConstantsKt.PROTECTION_FINGERPRINT;
import static com.simplemobiletools.commons.helpers.ConstantsKt.PROTECTION_NONE;

import com.simplemobiletools.commons.extensions.ContextKt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import org.mockito.MockedStatic;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import org.robolectric.annotation.Config;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import com.simplemobiletools.notes.pro.models.Note;
import com.simplemobiletools.notes.pro.models.NoteType;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33)
public class NoteTest {
    private Context realContext;
    @Mock
    private Context mockContext;
    @Mock
    private ContentResolver mockContentResolver;
    private Note emptyNote;
    private Note contentNote;
    private Note fileNote;
    private Note exceptionNote;
    private Note hasProtectionTypeNote;
    private Note hasNotProtectionTypeNote;
    private Note hasFingerprintAndBiometricNote;
    private Note hasNotFingerPrint;
    private Note hasFingerprintNonAvailableBiometric;

    @Before
    public void setUp() {
        realContext = RuntimeEnvironment.getApplication();
        MockitoAnnotations.openMocks(this);
        when(mockContext.getContentResolver()).thenReturn(mockContentResolver);

        emptyNote = new Note(1L, "title", "value", NoteType.TYPE_TEXT, "", 0, "");
        contentNote = new Note(1L, "title", "", NoteType.TYPE_TEXT, "content://mock/test", 0, "");
        exceptionNote = new Note(1L, "title", "value", NoteType.TYPE_TEXT, "invalid/fileDoesNotExist/file.txt", 0, "");
        hasProtectionTypeNote = new Note(1L, "title", "content", NoteType.TYPE_TEXT, "", 2, "hash");
        hasNotProtectionTypeNote = new Note(1L, "title", "content", NoteType.TYPE_TEXT, "", PROTECTION_NONE, "");
        hasFingerprintAndBiometricNote = new Note(1L, "title", "content", NoteType.TYPE_TEXT, "", PROTECTION_FINGERPRINT, "hash");
        hasNotFingerPrint = new Note(1L, "title", "content", NoteType.TYPE_TEXT, "", PROTECTION_NONE, "");
        hasFingerprintNonAvailableBiometric = new Note(1L, "title", "content", NoteType.TYPE_TEXT, "", PROTECTION_FINGERPRINT, "hash");
    }

    @After
    public void tearDown() {
        emptyNote = null;
        contentNote = null;
        exceptionNote = null;
        hasProtectionTypeNote = null;
        hasNotProtectionTypeNote = null;
        hasFingerprintAndBiometricNote = null;
        hasNotFingerPrint = null;
        hasFingerprintNonAvailableBiometric = null;
    }

    @Test
    public void getNoteStoredValueEmptyPath() {
        String result = emptyNote.getNoteStoredValue(mockContext);
        assertEquals("value", result);
    }

    @Test
    public void getNoteStoredValueReadFromContext() throws FileNotFoundException {
        String expectedContent = "test";
        InputStream inputStream = new ByteArrayInputStream(expectedContent.getBytes());

        Uri uri = Uri.parse("content://mock/test");
        when(mockContentResolver.openInputStream(uri)).thenReturn(inputStream);

        String result = contentNote.getNoteStoredValue(mockContext);
        assertEquals(expectedContent, result);
    }

    @Test
    public void getNoteStoredValueReadFromFile() throws IOException {
        File testFile = File.createTempFile("test", ".txt");
        testFile.deleteOnExit();

        FileWriter writer = new FileWriter(testFile);
        writer.write("test");
        writer.close();

        fileNote = new Note(1L, "title", "value", NoteType.TYPE_TEXT, testFile.getAbsolutePath(), 0, "");

        String result = fileNote.getNoteStoredValue(mockContext);
        assertEquals("test", result);
        testFile.delete();
    }

    @Test
    public void getNoteStoredValueException() {
        String result = exceptionNote.getNoteStoredValue(mockContext);
        assertNull(result);

    }

    @Test
    public void isLockedHasProtectionType() {
        boolean result = hasProtectionTypeNote.isLocked();
        assertTrue(result);
    }

    @Test
    public void isLockedHasNotProtectionType() {
        boolean result = hasNotProtectionTypeNote.isLocked();
        assertFalse(result);
    }

    @Test
    public void shouldBeUnlockedHasProtectionFingerprintAndNoBiometric() {
        try (MockedStatic<ContextKt> mocked = mockStatic(ContextKt.class)) {
            mocked.when(() -> ContextKt.isBiometricIdAvailable(any(Context.class))).thenReturn(false);

            boolean result = hasFingerprintNonAvailableBiometric.shouldBeUnlocked(realContext);
            assertTrue(result);
        }
    }

    @Test
    public void shouldBeUnlockedHasNotProtectionFingerprint() {
        try (MockedStatic<ContextKt> mocked = mockStatic(ContextKt.class)) {
            mocked.when(() -> ContextKt.isBiometricIdAvailable(any(Context.class))).thenReturn(false);

            boolean result = hasNotFingerPrint.shouldBeUnlocked(realContext);
            assertFalse(result);
        }
    }

    @Test
    public void shouldBeUnlockedHasProtectionFingerprintAndBiometric() {
        try (MockedStatic<ContextKt> mocked = mockStatic(ContextKt.class)) {
            mocked.when(() -> ContextKt.isBiometricIdAvailable(any(Context.class))).thenReturn(true);

            boolean result = hasFingerprintAndBiometricNote.shouldBeUnlocked(realContext);
            assertFalse(result);
        }
    }



}
