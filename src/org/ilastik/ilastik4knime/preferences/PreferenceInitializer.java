package org.ilastik.ilastik4knime.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
         */
        public void initializeDefaultPreferences() {
                IPreferenceStore store = IlastikPreferencesPlugin.getDefault().getPreferenceStore();
                store.setDefault(PreferenceConstants.P_THREADS, -1);
                store.setDefault(PreferenceConstants.P_RAM, 512);
                store.setDefault(PreferenceConstants.P_PATH, null);
        }

}
