package com.mrkaz.tokoin.helpers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.ContainsExtraTypeInfo;
import org.mockito.internal.matchers.text.ValuePrinter;

import java.io.Serializable;
import java.util.Objects;

public class BundleMatcher implements ArgumentMatcher<Bundle>, ContainsExtraTypeInfo, Serializable {

    @Nullable
    private final Bundle expected;

    public BundleMatcher(@Nullable Bundle expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(Bundle actual) {
        if (expected == null && actual == null) {
            return true;
        }

        if (expected == null || actual == null) {
            return false;
        }

        return areBundlesEqual(expected, actual);
    }

    private boolean areBundlesEqual(@NonNull Bundle expected, @NonNull Bundle actual) {
        if (expected.size() != actual.size()) {
            return false;
        }

        if (!expected.keySet().containsAll(actual.keySet())) {
            return false;
        }

        for (String key : expected.keySet()) {
            @Nullable Object expectedValue = expected.get(key);
            @Nullable Object actualValue = actual.get(key);

            if (expectedValue instanceof Bundle && actualValue instanceof Bundle) {
                if (!areBundlesEqual((Bundle) expectedValue, (Bundle) actualValue)) {
                    return false;
                }
            } else if (!Objects.equals(expectedValue, actualValue)) {
                return false;
            }
        }

        return true;
    }

    public String toStringWithType() {
        String clazz = expected != null ? expected.getClass().getSimpleName() : null;
        return "(" + clazz + ") " + describe(expected);
    }

    private String describe(Object object) {
        return ValuePrinter.print(object);
    }

    @Override
    public boolean typeMatches(Object actual) {
        return expected != null && actual != null && actual.getClass() == expected.getClass();
    }

    public String toString() {
        return describe(expected);
    }



}
