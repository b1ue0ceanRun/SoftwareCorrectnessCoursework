import java.lang.reflect.Field;
import java.util.List;

public class TestUtils {
    @SuppressWarnings("unchecked")
    public static <T> List<T> getPrivateField(Object instance, String fieldName) {
        try {
            Field f = instance.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return (List<T>) f.get(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
