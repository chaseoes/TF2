package com.chaseoes.tf2.localization.replacer;

import com.chaseoes.tf2.localization.replacer.Replacer;

public class IntReplacer extends Replacer<Integer> {

    public IntReplacer(String replacedVar) {
        super(replacedVar);
    }

    @Override
    protected String getReplacementInternal(Integer obj) {
        return obj.toString();
    }

}
