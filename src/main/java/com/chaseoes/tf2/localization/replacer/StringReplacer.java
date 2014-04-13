package com.chaseoes.tf2.localization.replacer;

public class StringReplacer extends Replacer<String> {

    public StringReplacer(String replacedVar) {
        super(replacedVar);
    }

    @Override
    protected String getReplacementInternal(String obj) {
        return obj;
    }

}
