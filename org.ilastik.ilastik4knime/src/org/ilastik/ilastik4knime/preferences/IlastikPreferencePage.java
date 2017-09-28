package org.ilastik.ilastik4knime.preferences;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.ilastik.ilastik4ij.IlastikOptions;
import org.knime.knip.imagej2.core.IJGateway;
import org.scijava.options.OptionsService;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class IlastikPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
    private OptionsService optionsService = null;

    public IlastikPreferencePage() {
        super(GRID);
        setPreferenceStore(IlastikPreferencesPlugin.getDefault().getPreferenceStore());
        setDescription("Ilastik binary configuration");
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common
     * GUI blocks needed to manipulate various types of preferences. Each field
     * editor knows how to save and restore itself.
     */
    @Override
    public void createFieldEditors() {
        final String os = System.getProperty("os.name", "generic").toLowerCase();
        if (os.contains("mac") || os.contains("darwin"))
            addField(new DirectoryFieldEditor(PreferenceConstants.P_PATH, "&Ilastik executable location:",
                    getFieldEditorParent()));
        else
            addField(new FileFieldEditor(PreferenceConstants.P_PATH, "&Ilastik executable location:",
                    getFieldEditorParent()));
        final IntegerFieldEditor ramField = new IntegerFieldEditor(PreferenceConstants.P_RAM,
                "Max amount of &RAM (in MB) ilastik is allowed to use", getFieldEditorParent());
        ramField.setValidRange(256, Integer.MAX_VALUE);
        addField(ramField);
        IntegerFieldEditor threadsField = new IntegerFieldEditor(PreferenceConstants.P_THREADS,
                "Maximum number of threads ilastik is allowed to use (-1 for no limit)", getFieldEditorParent());
        threadsField.setValidRange(-1, Integer.MAX_VALUE);
        addField(threadsField);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    @Override
    public void init(final IWorkbench workbench) {
    }

    @Override
    public boolean performOk() {
        final boolean status = super.performOk();
        if (status) {
            if (optionsService == null) {
                optionsService = IJGateway.getImageJContext().getService(OptionsService.class);
                if (optionsService == null)
                    return false;
            }

            final IlastikOptions ilastikOptions = optionsService.getOptions(IlastikOptions.class);

            if (ilastikOptions == null) {
                // log.error("Could not find configured ilastik options!");
                return false;
            }

            final IPreferenceStore preferenceStore = getPreferenceStore();
            ilastikOptions.setExecutableFilePath(preferenceStore.getString(PreferenceConstants.P_PATH));
            ilastikOptions.setMaxRamMb(preferenceStore.getInt(PreferenceConstants.P_RAM));
            ilastikOptions.setNumThreads(preferenceStore.getInt(PreferenceConstants.P_THREADS));

            Path p = Paths.get(ilastikOptions.getExecutableFilePath());
            if (!Files.exists(p))
                this.setErrorMessage("Selected file is no valid ilastik executable");

            return ilastikOptions.isConfigured();
        }

        return status;
    }

}