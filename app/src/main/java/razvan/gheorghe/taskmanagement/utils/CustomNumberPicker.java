package razvan.gheorghe.taskmanagement.utils;

import android.content.Context;
import android.os.Handler;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


public class CustomNumberPicker extends LinearLayout {

    private final long REPEAT_DELAY = 50;

    private final int ELEMENT_HEIGHT = 60;
    private final int ELEMENT_WIDTH = ELEMENT_HEIGHT; // you're all squares, yo

    private final int MINIMUM = 0;
    private final int MAXIMUM = 999;

    private final int TEXT_SIZE = 30;

    public Integer value;

    Button decrement;
    Button increment;
    public EditText valueText;

    private Handler repeatUpdateHandler = new Handler();

    private boolean autoIncrement = false;
    private boolean autoDecrement = false;

    class RepetetiveUpdater implements Runnable {
        public void run() {
            if (autoIncrement) {
                increment();
                repeatUpdateHandler.postDelayed(new RepetetiveUpdater(), REPEAT_DELAY);
            } else if (autoDecrement) {
                decrement();
                repeatUpdateHandler.postDelayed(new RepetetiveUpdater(), REPEAT_DELAY);
            }
        }
    }

    public CustomNumberPicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        LayoutParams elementParams = new LinearLayout.LayoutParams(ELEMENT_HEIGHT, ELEMENT_WIDTH);

        initDecrementButton(context);
        initValueEditText(context);
        initIncrementButton(context);


        if (getOrientation() == VERTICAL) {
            addView(increment, elementParams);
            addView(valueText, elementParams);
            addView(decrement, elementParams);
        } else {
            addView(decrement, elementParams);
            addView(valueText, elementParams);
            addView(increment, elementParams);
        }
    }

    private void initIncrementButton(Context context) {
        increment = new Button(context);
        increment.setTextSize(TEXT_SIZE);
        increment.setText("+");

        increment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                increment();
            }
        });

        increment.setOnLongClickListener(
                new View.OnLongClickListener() {
                    public boolean onLongClick(View arg0) {
                        autoIncrement = true;
                        repeatUpdateHandler.post(new RepetetiveUpdater());
                        return false;
                    }
                }
        );

        // When the button is released, if we're auto incrementing, stop
        increment.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && autoIncrement) {
                    autoIncrement = false;
                }
                return false;
            }
        });
    }

    private void initValueEditText(Context context) {

        value = new Integer(0);

        valueText = new EditText(context);
        valueText.setTextSize(TEXT_SIZE);

        valueText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int arg1, KeyEvent event) {
                int backupValue = value;
                try {
                    value = Integer.parseInt(((EditText) v).getText().toString());
                } catch (NumberFormatException nfe) {
                    value = backupValue;
                }
                return false;
            }
        });

        // Highlight the number when we get focus
        valueText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((EditText) v).selectAll();
                }
            }
        });
        valueText.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        valueText.setText(value.toString());
        valueText.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    private void initDecrementButton(Context context) {
        decrement = new Button(context);
        decrement.setTextSize(TEXT_SIZE);
        decrement.setText("-");


        // Decrement once for a click
        decrement.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                decrement();
            }
        });


        // Auto Decrement for a long click
        decrement.setOnLongClickListener(
                new View.OnLongClickListener() {
                    public boolean onLongClick(View arg0) {
                        autoDecrement = true;
                        repeatUpdateHandler.post(new RepetetiveUpdater());
                        return false;
                    }
                }
        );

        // When the button is released, if we're auto decrementing, stop
        decrement.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && autoDecrement) {
                    autoDecrement = false;
                }
                return false;
            }
        });
    }

    public void increment() {
        if (value < MAXIMUM) {
            value = value + 1;
            valueText.setText(value.toString());
        }
    }

    public void decrement() {
        if (value > MINIMUM) {
            value = value - 1;
            valueText.setText(value.toString());
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (value > MAXIMUM) value = MAXIMUM;
        if (value >= 0) {
            this.value = value;
            valueText.setText(this.value.toString());
        }
    }
}