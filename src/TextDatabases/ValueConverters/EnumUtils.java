package TextDatabases.ValueConverters;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class EnumUtils {
    public static <TEnum extends Enum<?>> Map<Integer, TEnum> constantsByOrdinal(Class<TEnum> enumClass) {
        Map<Integer, TEnum> result = new HashMap<>();

        for (var constant : enumClass.getEnumConstants())
            result.put(constant.ordinal(), constant);

        return result;
    }

    public static <TEnum extends Enum<?>> Map<String, TEnum> constantsByName(Class<TEnum> enumClass) {
        Map<String, TEnum> result = new HashMap<>();

        for (var constant : enumClass.getEnumConstants())
            result.put(constant.name(), constant);

        return result;
    }

    public static <TEnum extends Enum<?>> Map<TEnum, String> namesByConstant(Class<TEnum> enumClass) {
        Map<TEnum, String> result = new HashMap<>();

        for (var constant : enumClass.getEnumConstants())
            result.put(constant, constant.name());

        return result;
    }

    public static boolean typeIsEnum(Type type) {
        return ((Class<?>) type).isEnum();
    }
}
