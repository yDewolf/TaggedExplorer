package com.github.ydewolf.swing.ui.SettingsMenu;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
    protected HashMap<ManagerConfigKeys, Callable<Object>> value_map = new HashMap<>();
    protected HashMap<Enum<?>, JCheckBox> checkbox_map = new HashMap<>();

    protected JButton save_button;

    protected int width;
    protected int height;

    public SettingsMenu(FileManagerFrame manager_frame, int width, int height, int border_size) {
        super(manager_frame, "Settings", true);
        this.manager_frame = manager_frame;
        this.width = width;
        this.height = height;

        this.setSize(width, height);
        // this.setLocationRelativeTo(manager_frame);
        
        JPanel main_panel = new JPanel();
        main_panel.setLayout(new BoxLayout(main_panel, BoxLayout.Y_AXIS));
        JavaSwingUtils.setupJComponentDim(main_panel, width, height);
        this.add(main_panel);

        this.save_button = new JButton("Save");

        for (ManagerConfigKeys key : this.manager_frame.getConfigs().getConfigs()) {
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
    }

    public void addConfigOption(JPanel main_panel, ManagerConfigKeys config_key) {
        Configuration config = this.manager_frame.getConfigs().getConfig(config_key);
        // Configs that use arrays
        // if (config.getClass().isAssignableFrom(BaseArrayConfiguration.class)) {
        if (config instanceof BaseArrayConfiguration) {
            JPanel panel = this.optionForArrayConfig((BaseArrayConfiguration) config);
            main_panel.add(panel);
            return;
        }

        // Configs that uses multiple enum values
        if (config instanceof BaseEnumConfiguration) {
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
        panel.setLayout(new FlowLayout());

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
        
        value_map.put(config.getConfigKey(), () -> {
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

            return cut_split;
        });

        return panel;
    }

    protected JPanel optionForEnumConfig(BaseEnumConfiguration config) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));

        JLabel label = new JLabel(config.getName());
        panel.add(label);

        Set<Enum<?>> key_set = config.getValues().keySet();

        for (Enum<?> key : key_set) {
            JPanel key_row = new JPanel();
            key_row.setLayout(new FlowLayout());
            
            JLabel key_label = new JLabel(key.toString());
            key_row.add(key_label);

            JCheckBox checkbox = new JCheckBox();
            checkbox.setSelected(config.getValue(key));
            key_row.add(checkbox);

            this.checkbox_map.put(key, checkbox);

            panel.add(key_row);
        }

        value_map.put(config.getConfigKey(), () -> {
            HashMap<Enum<?>, Boolean> hash_map = new HashMap<>();
            for (Enum<?> key : key_set) {
                hash_map.put(key, this.checkbox_map.get(key).isSelected());
            }

            return hash_map;
        });

        return panel;
    }

    protected JPanel optionForSelectEnumConfig(BaseSelectEnumConfiguration config) {
        JPanel panel = new JPanel();
        
        JLabel label = new JLabel(config.getName());
        panel.add(label);

        Class<? extends Enum> enum_class = config.getValue().getClass();

        JComboBox<Enum<?>> dropdown = new JComboBox<>(enum_class.getEnumConstants());
        dropdown.setSelectedItem(config.getValue());

        value_map.put(config.getConfigKey(), () -> {
            return dropdown.getSelectedItem();
        });

        panel.add(dropdown);

        return panel;
    }

    public void updateConfiguration() {
        ManagerConfig new_config = new ManagerConfig();
        for (ManagerConfigKeys key : this.value_map.keySet()) {
            Callable<Object> lambda = this.value_map.get(key);
            try {
                new_config.setConfig(key, lambda.call());
            } catch (Exception e) {
                System.err.println("Couldn't run lambda function to get value for " + key + " configuration");
                e.printStackTrace();
            }
        }

        this.manager_frame.updateFileManagerConfigs(new_config);
    }
}
