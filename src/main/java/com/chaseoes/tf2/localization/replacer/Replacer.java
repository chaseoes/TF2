package com.chaseoes.tf2.localization.replacer;

public abstract class Replacer<T> {

    private final String replacedVar;

    public Replacer(String replacedVar) {
        this.replacedVar = replacedVar;
    }

    public final String getReplacedVar() {
        return replacedVar;
    }

    @SuppressWarnings({"unused", "unchecked"})
    public final String getReplacement(Object obj) {
        try {
            T t = (T) obj;
        } catch (ClassCastException ex) {
            throw new RuntimeException("Failed I18N call: " + ex.getMessage());
        }
        return getReplacementInternal((T) obj);
    }

    protected abstract String getReplacementInternal(T obj);

}
