package nl.snoworange.cranberry.features.setting;

import net.minecraftforge.common.MinecraftForge;
import nl.snoworange.cranberry.util.EnumConverter;

public class Setting <T> {

    public String name;
    public T value;
    public T defaultValue;
    public T plannedValue;
    public T min;
    public T max;
    public boolean isVisible;
    public boolean hasRestriction;

    public Setting(String name, T defaultValue) {
        this.name = name;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.plannedValue = defaultValue;
        this.isVisible = true;
        this.hasRestriction = false;
    }

    public Setting(String name, T defaultValue, T min, T max) {
        this.name = name;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.plannedValue = defaultValue;
        this.min = min;
        this.max = max;
        this.isVisible = true;
        this.hasRestriction = true;
    }

    public Setting(String name, T defaultValue, T min, T max, boolean isVisible) {
        this.name = name;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.plannedValue = defaultValue;
        this.min = min;
        this.max = max;
        this.isVisible = isVisible;
        this.hasRestriction = true;
    }

    public Setting(String name, T defaultValue, boolean isVisible) {
        this.name = name;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.plannedValue = defaultValue;
        this.isVisible = false;
        this.hasRestriction = false;
    }

    public String getName() {
        return this.name;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.setPlannedValue(value);
        this.value = this.plannedValue;
    }

    public T getPlannedValue() {
        return this.plannedValue;
    }

    public void setPlannedValue(T value) {
        this.plannedValue = value;
    }

    public int getEnum(String input) {
        try {
            for (int i = 0; i < this.value.getClass().getEnumConstants().length; ++i) {
                Enum<?> e = (Enum<?>) this.value.getClass().getEnumConstants()[i];
                if (!e.name().equalsIgnoreCase(input)) continue;
                return i;
            }
        } catch (ClassCastException ignored) {

        }
        return -1;
    }

    public void setEnumValue(String value) {
        try {
            for (Enum<?> e :  ((Enum<?>) this.value).getClass().getEnumConstants()) {
                if (!e.name().equalsIgnoreCase(value)) continue;
                this.value = (T) e;
            }
        } catch (ClassCastException ignored) {

        }
    }

    public String currentEnumName() {
        try {
            return EnumConverter.getProperName((Enum<?>) this.value);
        } catch (ClassCastException ignored) {

        }
        return "";
    }

    public int currentEnum() {
        try {
            return EnumConverter.currentEnum((Enum<?>) this.value);
        } catch (ClassCastException ignored) {

        }
        return 0;
    }

    public void increaseEnumNoEvent() {
        try {
            this.value = (T) EnumConverter.increaseEnum((Enum<?>) this.value);
        } catch (ClassCastException ignored) {

        }
    }

    public String getType() {
        if (this.isEnumSetting()) {
            return "Enum";
        }
        return this.getClassName(this.defaultValue);
    }

    public <T> String getClassName(T value) {
        return value.getClass().getSimpleName();
    }

    public boolean isNumberSetting() {
        return this.value instanceof Double || this.value instanceof Integer || this.value instanceof Short || this.value instanceof Long || this.value instanceof Float;
    }

    public boolean isEnumSetting() {
        return !this.isNumberSetting() && !(this.value instanceof String) && !(this.value instanceof Character) && !(this.value instanceof Boolean) && !(this.value instanceof Bind) && !this.name.equals("Keybind");
    }

    public boolean isStringSetting() {
        return this.value instanceof String;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public String getValueAsString() {
        return this.value.toString();
    }

    public boolean hasRestriction() {
        return this.hasRestriction;
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    public T getMin() {
        return this.min;
    }

    public void setMin(T min) {
        this.min = min;
    }

    public T getMax() {
        return this.max;
    }

    public void setMax(T max) {
        this.max = max;
    }
}
