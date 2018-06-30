package com.sample_mvvm.util;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.lang.ref.WeakReference;

public class UsPhoneNumberFormatter implements TextWatcher {

    private boolean mFormatting;
    private boolean clearFlag, checkNum1 = false;
    private int mLastStartLocation;
    private String mLastBeforeText;
    private WeakReference<EditText> mWeakEditText;

    public UsPhoneNumberFormatter(WeakReference<EditText> weakEditText) {
        this.mWeakEditText = weakEditText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        if (after == 0 && s.toString().equals("1 ")) {
            clearFlag = true;
        }
        mLastStartLocation = start;
        mLastBeforeText = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!mFormatting) {
            mFormatting = true;
            int curPos = mLastStartLocation;
            String beforeValue = mLastBeforeText;
            String currentValue = s.toString();
            String formattedValue = formatUsNumber(s);
            if (currentValue.length() > beforeValue.length()) {
                int setCusorPos = formattedValue.length()
                        - (beforeValue.length() - curPos);
                mWeakEditText.get().setSelection(setCusorPos < 0 ? 0 : setCusorPos);
                if (checkNum1) {
                    mWeakEditText.get().setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
                    Log.e("check", "1");
                } else {
                    mWeakEditText.get().setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});
                    Log.e("check", "2");
                }
            } else {
                int setCusorPos = formattedValue.length()
                        - (currentValue.length() - curPos);
                if (setCusorPos > 0 && !Character.isDigit(formattedValue.charAt(setCusorPos - 1))) {
                    setCusorPos--;
                }
                mWeakEditText.get().setSelection(setCusorPos < 0 ? 0 : setCusorPos);
            }
            mFormatting = false;
        }
    }

    private String formatUsNumber(Editable text) {
        StringBuilder formattedString = new StringBuilder();
        int p = 0;
        while (p < text.length()) {
            char ch = text.charAt(p);
            if (!Character.isDigit(ch)) {
                text.delete(p, p + 1);
            } else {
                p++;
            }
        }
        String allDigitString = text.toString();
        int totalDigitCount = allDigitString.length();
        if (totalDigitCount == 0
                || (totalDigitCount > 10 && !allDigitString.startsWith("1"))
                || totalDigitCount > 11) {
            text.clear();
            text.append(allDigitString);
            return allDigitString;
        }
        int alreadyPlacedDigitCount = 0;
        if (totalDigitCount - alreadyPlacedDigitCount > 3) {
            formattedString.append("("
                    + allDigitString.substring(alreadyPlacedDigitCount,
                    alreadyPlacedDigitCount + 3) + ") ");
            alreadyPlacedDigitCount += 3;
        }
        if (totalDigitCount - alreadyPlacedDigitCount > 3) {
            formattedString.append(allDigitString.substring(
                    alreadyPlacedDigitCount, alreadyPlacedDigitCount + 3)
                    + "-");
            alreadyPlacedDigitCount += 3;
        }
        if (totalDigitCount > alreadyPlacedDigitCount) {
            formattedString.append(allDigitString
                    .substring(alreadyPlacedDigitCount));
        }

        text.clear();
        text.append(formattedString.toString());
        return formattedString.toString();
    }
}
