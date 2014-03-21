package com.michaelnovakjr.numberpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class NumberPickerPreference extends DialogPreference {
    private NumberPicker hPicker, mPicker, sPicker;
    private int mStartRange;
//    private int mEndRange;
    private int mDefault;

    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (attrs == null) {
            return;
        }

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.numberpicker);
        mStartRange = arr.getInteger(R.styleable.numberpicker_startRange, 0);
//        mEndRange = arr.getInteger(R.styleable.numberpicker_endRange, 900000);
        mDefault = arr.getInteger(R.styleable.numberpicker_defaultValue, 0);
        
        arr.recycle();

        setDialogLayoutResource(R.layout.pref_number_picker);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.dialogPreferenceStyle);
    }

    public NumberPickerPreference(Context context) {
        this(context, null);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        hPicker = (NumberPicker) view.findViewById(R.id.pref_num_picker_hour);
        hPicker.setRange(mStartRange, 240);
        hPicker.setCurrent(getValue()/3600);

        mPicker = (NumberPicker) view.findViewById(R.id.pref_num_picker_minute);
        mPicker.setRange(mStartRange, 60);
        mPicker.setCurrent(getValue()%3600/60);
        
        sPicker = (NumberPicker) view.findViewById(R.id.pref_num_picker_second);
        sPicker.setRange(mStartRange, 60);
        sPicker.setCurrent(getValue()%60);
        
//Shows the saved value on dialog
        TextView total_second = (TextView) view.findViewById(R.id.pref_total_second);
        total_second.setText(String.valueOf(getValue()) + " seconds");
        
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        final int origValue = getValue();
        final int curValue = sPicker.getCurrent() + mPicker.getCurrent()*60 + hPicker.getCurrent()*3600;
        
        if (positiveResult && (curValue != origValue)) {
            if (callChangeListener(curValue)) {
                saveValue(curValue);
//                System.out.println("DEBUG curValue" + String.valueOf(curValue));// DEBUG
            }
            
        }
    }

/*    public void setRange(int start, int end) {
    	hPicker.setRange(start, end);
    	mPicker.setRange(start, end);
        sPicker.setRange(start, end);
//        mPicker.setRange(start, end);
    }*/

    private boolean saveValue(int val) {
//    	System.out.println("DEBUG val" + String.valueOf(val));// DEBUG
    	return persistInt(val);
    }
    
    private int getValue() {
        return getSharedPreferences().getInt(getKey(), mDefault);
    }

}
