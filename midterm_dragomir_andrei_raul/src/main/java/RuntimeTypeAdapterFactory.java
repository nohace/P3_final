import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A TypeAdapterFactory for Gson that supports polymorphic types.
 * This factory maps a base class to its subtypes and ensures correct serialization and deserialization.
 */
public final class RuntimeTypeAdapterFactory<T> implements TypeAdapterFactory {
    private final Class<?> baseType;
    private final String typeFieldName;
    private final Map<String, Class<? extends T>> labelToSubtype = new HashMap<>();
    private final Map<Class<? extends T>, String> subtypeToLabel = new HashMap<>();

    private RuntimeTypeAdapterFactory(Class<?> baseType, String typeFieldName) {
        if (baseType == null || typeFieldName == null) {
            throw new NullPointerException("Base type and type field name must not be null.");
        }
        this.baseType = baseType;
        this.typeFieldName = typeFieldName;
    }

    public static <T> RuntimeTypeAdapterFactory<T> of(Class<T> baseType, String typeFieldName) {
        return new RuntimeTypeAdapterFactory<>(baseType, typeFieldName);
    }

    public RuntimeTypeAdapterFactory<T> registerSubtype(Class<? extends T> subtype, String label) {
        if (subtype == null || label == null) {
            throw new NullPointerException("Subtype and label must not be null.");
        }
        if (labelToSubtype.containsKey(label) || subtypeToLabel.containsKey(subtype)) {
            throw new IllegalArgumentException("Subtype or label already registered.");
        }
        labelToSubtype.put(label, subtype);
        subtypeToLabel.put(subtype, label);
        return this;
    }

    @Override
    public <R> TypeAdapter<R> create(Gson gson, TypeToken<R> type) {
        if (!baseType.isAssignableFrom(type.getRawType())) {
            return null;
        }

        final Map<String, TypeAdapter<?>> labelToAdapter = new HashMap<>();
        final Map<Class<?>, TypeAdapter<?>> subtypeToAdapter = new HashMap<>();

        for (Map.Entry<String, Class<? extends T>> entry : labelToSubtype.entrySet()) {
            TypeAdapter<?> adapter = gson.getDelegateAdapter(this, TypeToken.get(entry.getValue()));
            labelToAdapter.put(entry.getKey(), adapter);
            subtypeToAdapter.put(entry.getValue(), adapter);
        }

        return new TypeAdapter<R>() {
            @Override
            public void write(JsonWriter out, R value) throws IOException {
                Class<?> rawType = value.getClass();
                String label = subtypeToLabel.get(rawType);
                if (label == null) {
                    throw new JsonParseException("Unknown subtype: " + rawType);
                }

                @SuppressWarnings("unchecked")
                TypeAdapter<R> delegate = (TypeAdapter<R>) subtypeToAdapter.get(rawType);
                if (delegate == null) {
                    throw new JsonParseException("No delegate for subtype: " + rawType);
                }

                JsonObject jsonObject = delegate.toJsonTree(value).getAsJsonObject();
                jsonObject.addProperty(typeFieldName, label);
                gson.toJson(jsonObject, out);
            }

            @Override
            public R read(JsonReader in) throws IOException {
                JsonElement jsonElement = JsonParser.parseReader(in);
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonElement labelElement = jsonObject.remove(typeFieldName);

                if (labelElement == null) {
                    throw new JsonParseException("Missing type field '" + typeFieldName + "' in JSON object.");
                }

                String label = labelElement.getAsString();
                @SuppressWarnings("unchecked")
                TypeAdapter<R> delegate = (TypeAdapter<R>) labelToAdapter.get(label);

                if (delegate == null) {
                    throw new JsonParseException("Unknown type label: " + label);
                }

                return delegate.fromJsonTree(jsonObject);
            }
        };
    }
}
