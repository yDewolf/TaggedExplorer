package com.github.ydewolf.swing.ui.SettingsMenu;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.github.ydewolf.classes.utils.config.ManagerConfig;
import com.github.ydewolf.classes.utils.config.abstract_classes.BaseArrayConfiguration;
import com.github.ydewolf.classes.utils.config.abstract_classes.BaseEnumConfiguration;
import com.github.ydewolf.classes.utils.config.abstract_classes.BaseSelectEnumConfiguration;
import com.github.ydewolf.classes.utils.config.abstract_classes.Configuration;
import com.github.ydewolf.enums.ManagerConfigKeys;
import com.github.ydewolf.swing.FileManagerFrame;
import com.github.ydewolf.swing.utils.JavaSwingUtils;

public class SettingsMenu extends JDialog {
    protected FileManagerFrame manager_frame;
    protected HashMap<ManagerConfigKeys, Object> value_map = new HashMap<>();
    protected HashMap<Enum<?>, JCheckBox> checkbox_map = new HashMap<>();
    protected ManagerConfigKeys[] EXCLUDED_SETTINGS = {};

    protected JButton save_button;

    protected int width;
    protected int height;

    public SettingsMenu(FileManagerFrame manager_frame, ManagerConfigKeys[] excluded_settings, int width, int height, int border_size) {
        this(manager_frame, width, height, border_size);
        this.setup(manager_frame, excluded_settings, width, height, border_size);
    }

    public SettingsMenu(FileManagerFrame manager_frame, int width, int height, int border_size) {
        super(manager_frame, "Settings", true);
        this.setup(manager_frame, this.EXCLUDED_SETTINGS,width, height, border_size);
    }

    protected void setup(FileManagerFrame manager_frame, ManagerConfigKeys[] excluded_settings, int width, int height, int border_size) {
        this.manager_frame = manager_frame;
        this.width = width;
        this.height = height;
        this.EXCLUDED_SETTINGS = excluded_settings;
        this.setResizable(false);
        
        JPanel main_panel = new JPanel();
        JavaSwingUtils.setupJPanel(main_panel, width, height, border_size);

        this.add(main_panel);

        this.save_button = new JButton("Save");

        for (ManagerConfigKeys key : ManagerConfigKeys.values()) {
            if (this.isExcludedConfigKey(key)) { continue; }

            this.addConfigOption(main_panel, key);
        }

        JPanel button_panel = new JPanel();
        main_panel.add(button_panel);
        button_panel.setLayout(new FlowLayout());

        save_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateConfiguration();
                dispose();
            }
        });

        button_panel.add(save_button);

        JButton close_button = new JButton("Close");
        close_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();   
            }
        });
        button_panel.add(close_button);

        this.pack();
    }

    public void addConfigOption(JPanel main_panel, ManagerConfigKeys config_key) {
        Configuration config = this.manager_frame.getConfigs().getConfig(config_key);
        // Configs that use arrays
        if (config instanceof BaseArrayConfiguration) {
            JPanel panel = this.optionForArrayConfig((BaseArrayConfiguration) config);
            main_panel.add(panel);
            return;
        }

        // Configs that uses multiple enum values
        if (config instanceof BaseEnumConfiguration) {
            main_panel.add(this.createSeparator(config.getName()));
            JPanel panel = this.optionForEnumConfig((BaseEnumConfiguration) config);
            main_panel.add(panel);
            return;
        }

        // Configs that selects one of multiple enum values
        if (config instanceof BaseSelectEnumConfiguration) {
            JPanel panel = this.optionForSelectEnumConfig((BaseSelectEnumConfiguration) config);
            main_panel.add(panel);
            return;
        }
    }

    protected JPanel optionForArrayConfig(BaseArrayConfiguration config) {
        JPanel panel = new JPanel();
        JavaSwingUtils.setupJComponentDim(panel, this.width, 20);
        panel.setLayout(new GridLayout(1, 2));

        JLabel label = new JLabel(config.getName());
        panel.add(label);
        JTextField text_field = new JTextField(20);
        text_field.setToolTipText("Values separated by ','");

        String stringfied_value = "";
        for (int i = 0; i < config.getValue().length; i++) {
            if (i == 0) {
                stringfied_value += "" + config.getValue()[i];
                continue;
            }

            stringfied_value += ", " + config.getValue()[i];
        }

        text_field.setText(stringfied_value);
        panel.add(text_field);
        value_map.put(config.getConfigKey(), config.getValue());
    
        text_field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                this.changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                this.changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                String[] splitted = text_field.getText().split(",");
                ArrayList<String> extensions = new ArrayList<>();
                for (int i = 0; i < splitted.length; i++) {
                    if (splitted[i].equals("")) {
                        continue;
                    }
                    
                    extensions.add(splitted[i].trim());
                }
                String[] cut_split = new String[extensions.size()];
                for (int i = 0; i < cut_split.length; i++) {
                    cut_split[i] = extensions.get(i);
                }

                value_map.put(config.getConfigKey(), cut_split);
            }
        });

        return panel;
    }

    protected JPanel optionForEnumConfig(BaseEnumConfiguration config) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));

        Set<Enum<?>> key_set = config.getValues().keySet();

        for (Enum<?> key : key_set) {
            JPanel key_row = new JPanel();
            key_row.setLayout(new GridLayout(1, 2));
            
            JLabel key_label = new JLabel(key.toString());
            key_row.add(key_label);

            JCheckBox checkbox = new JCheckBox();
            checkbox.setSelected(config.getKeyValue(key));
            key_row.add(checkbox);

            this.checkbox_map.put(key, checkbox);

            panel.add(key_row);
        }

        HashMap<Enum<?>, Boolean> hash_map = new HashMap<>();
        value_map.put(config.getConfigKey(), hash_map);

        for (Enum<?> key : key_set) {
            JCheckBox checkbox = this.checkbox_map.get(key);
            hash_map.put(key, checkbox.isSelected());
            checkbox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    hash_map.put(key, checkbox.isSelected());
                    value_map.put(config.getConfigKey(), hash_map);

                    // Debug results
                    // for (Enum<?> key : hash_map.keySet()) {
                    //     System.out.println(key + " " + hash_map.get(key));
                    //     System.out.println();
                    //     for(Enum<?> keya : ((HashMap<Enum<?>, Boolean>) value_map.get(config.getConfigKey())).keySet()) {
                    //         System.out.println(keya + " " + ((HashMap<Enum<?>, Boolean>) value_map.get(config.getConfigKey())).get(keya));
                    //     }
                    // }
                }
            });
        }

        return panel;
    }

    protected JPanel optionForSelectEnumConfig(BaseSelectEnumConfiguration config) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));
        panel.setMaximumSize(new Dimension(width, 20));

        JLabel label = new JLabel(config.getName());
        panel.add(label);

        @SuppressWarnings("rawtypes")
        Class<? extends Enum> enum_class = config.getValue().getClass();

        JComboBox<Enum<?>> dropdown = new JComboBox<>(enum_class.getEnumConstants());
        value_map.put(config.getConfigKey(), config.getValue());
        
        dropdown.setSelectedItem(config.getValue());
        dropdown.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Enum<?> item = (Enum<?>) e.getItem();
                    value_map.put(config.getConfigKey(), item);
                }
            }
        });

        panel.add(dropdown);

        return panel;
    }

    // Updates

    public void updateConfiguration() {
        ManagerConfig new_config = new ManagerConfig();
        for (ManagerConfigKeys key : ManagerConfigKeys.values()) {
            if (this.isExcludedConfigKey(key)) {
                Object new_value = this.manager_frame.getConfigs().getConfigValue(key);
                new_config.setConfigValue(key, new_value);
                continue;
            }

            Object new_value = this.value_map.get(key);
            new_config.setConfigValue(key, new_value);
        }

        this.manager_frame.updateFileManagerConfigs(new_config);
    }

    // Utils

    protected JPanel createSeparator(String name) {
        JPanel separator = new JPanel();
        separator.setMinimumSize(new Dimension(width, 5));

        JLabel label = new JLabel(name);
        separator.add(label);

        return separator;
    }

    protected boolean isExcludedConfigKey(ManagerConfigKeys key) {
        for (ManagerConfigKeys excluded : this.EXCLUDED_SETTINGS) {
            if (key == excluded) {
                return true;
            }
        }

        return false;
    }
}
