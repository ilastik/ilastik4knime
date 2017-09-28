package org.ilastik.ilastik4knime.preferences;

import org.eclipse.ui.plugin.AbstractUIPlugin;

public class IlastikPreferencesPlugin extends AbstractUIPlugin {

        private static IlastikPreferencesPlugin instance = new IlastikPreferencesPlugin();

        public static IlastikPreferencesPlugin getDefault() {
                return instance;
        }
}
