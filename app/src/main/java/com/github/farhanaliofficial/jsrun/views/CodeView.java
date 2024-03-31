package com.github.farhanaliofficial.jsrun.views;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import java.util.Objects;
import java.util.regex.Pattern;
import android.graphics.Color;
import java.util.regex.Matcher;
import com.github.farhanaliofficial.jsrun.R;
import com.github.farhanaliofficial.jsrun.activity.MainActivity;

public class CodeView extends AppCompatMultiAutoCompleteTextView {
    private static final Pattern PATTERN_COMMENT = Pattern.compile("\\/\\/.*($|\\n)");
    private static final Pattern PATTERN_COMMENT_ML = Pattern.compile("\\/\\*(?:[^*]|[\\r\\n])+\\*\\/");
    private static final Pattern PATTERN_KEYWORDS = Pattern.compile("\\b(const|function|var|let|const|console|extends|class|eval|alert|prompt|confirm|Date|hasOwnProperty|Array|isNaN|NaN|Math|Number|Object|String|Error|float|int|true|false|null|debugger)\\b");
    private static final Pattern PATTERN_LINE = Pattern.compile(".*\\n");
    private static final Pattern PATTERN_METHODS = Pattern.compile("(?:\\.)(log|map|some|every|forEach|reduce)\\b");
    private static final Pattern PATTERN_NUMS = Pattern.compile("\\b(\\d*[.]?\\d+)\\b");
    private static final Pattern PATTERN_OPERATORS = Pattern.compile("\\b(break|continue|return|typeof|new|in|delete|throw|yield|try|default|catch|switch|case|instanceof|for|while|if|else)\\b");
    private static final Pattern PATTERN_SPECIAL = Pattern.compile("\\b(undefined)\\b");
    private static final Pattern PATTERN_SPECIAL_OPS = Pattern.compile("(=>)");
    private static final Pattern PATTERN_STRINGS = Pattern.compile("['|\"]([^'\"]*?)['|\"]", 8);
    private static final Pattern PATTERN_USUAL_OPS = Pattern.compile("(=|===|==|!=|>=|<=|&&|\\|\\|)");
    private static final Pattern PATTERN_VARS = Pattern.compile("\\b(arguments|super|this)\\b");
    private static final String TAG = "CodeView";
    private int colorComments;
    private int colorError;
    private int colorKeyword;
    private int colorMethods;
    private int colorNums;
    private int colorOperators;
    private int colorStrings;
    private int colorVars;
    public static int currentTheme;
    private boolean dirty = false;
    private int errorLine = 0;
    public static boolean isLineNumbersOn;
    public static int isMonospace;
    private boolean isPairedAutoCloseOn;
    public boolean modified = true;
    private OnTextChangedListener onTextChangedListener;
    private Paint paint;
    private Rect rect;
    private int tabWidth = 0;
    private int tabWidthInCharacters = 0;
    private int updateDelay = 100;
    private final Handler updateHandler = new Handler();
    private final Runnable updateRunnable = new Runnable() {
        public void run() {
            Editable text = CodeView.this.getText();
            if (CodeView.this.onTextChangedListener != null) {
                CodeView.this.onTextChangedListener.onTextChanged(text.toString());
            }
            CodeView.this.highlightWithoutChange(text);
        }
    };

    public interface OnTextChangedListener {
        void onTextChanged(String str);
    }

    public CodeView(Context context) {
        super(context);
        init(context);
    }

    public CodeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void setDirty(boolean z) {
        this.dirty = z;
    }

    private void setSyntaxColors(Context context) {
        if (this.currentTheme == 0) {
            this.colorKeyword = ContextCompat.getColor(context, R.color.BColorKeyword);
            this.colorOperators = ContextCompat.getColor(context, R.color.BColorOperators);
            this.colorStrings = ContextCompat.getColor(context, R.color.BColorStrings);
            this.colorMethods = ContextCompat.getColor(context, R.color.BColorMethods);
            this.colorNums = ContextCompat.getColor(context, R.color.BColorNums);
            this.colorComments = ContextCompat.getColor(context, R.color.BColorComment);
            this.colorVars = ContextCompat.getColor(context, R.color.BColorVars);
            return;
        }
        this.colorKeyword = ContextCompat.getColor(context, R.color.DColorKeyword);
        this.colorOperators = ContextCompat.getColor(context, R.color.DColorOperators);
        this.colorStrings = ContextCompat.getColor(context, R.color.DColorStrings);
        this.colorMethods = ContextCompat.getColor(context, R.color.DColorMethods);
        this.colorNums = ContextCompat.getColor(context, R.color.DColorNums);
        this.colorComments = ContextCompat.getColor(context, R.color.DColorComment);
        this.colorVars = ContextCompat.getColor(context, R.color.DColorVars);
    }

    public void setTheme(int i) {
        this.currentTheme = i;
        setSyntaxColors(getContext());
    }

    public void setFont(int i) {
        this.isMonospace = i;
        //AssetManager assets = getContext().getApplicationContext().getAssets();
        if (i == 1) {
            //setTypeface(Typeface.createFromAsset(assets, "fonts/DroidSansMono.ttf"));
            setTypeface(Typeface.MONOSPACE);
        } else {
            setTypeface(Typeface.SANS_SERIF);
        }
    }

    public void setLineNumbersVisibility(boolean isLineNumbersOn) {
        this.isLineNumbersOn = isLineNumbersOn;
        if (isLineNumbersOn) {
            float textSize = (MainActivity.getTextSize() * getContext().getResources().getDisplayMetrics().density);
            this.rect = new Rect();
            Paint lineNumberPaint = new Paint();
            this.paint = lineNumberPaint;
            lineNumberPaint.setStyle(Paint.Style.FILL);
            this.paint.setColor(Color.BLACK);
            this.paint.setTextSize(textSize);
            this.paint.setTypeface(Typeface.MONOSPACE);
            this.paint.setAntiAlias(true);

            return;
        }
        setPadding(0);
    }


    public void setPairedAutoCloseOn(boolean z) {
        this.isPairedAutoCloseOn = z;
    }

    private void setPadding(int i) {
        setPadding((int) (((float) i) * getResources().getDisplayMetrics().density), getPaddingTop(), getPaddingRight(), getPaddingBottom());
    }

    protected void onDraw(Canvas canvas) {
        if (this.isLineNumbersOn) {
            int baseline = getBaseline();
            int lineCount = getLineCount();
            int floor = ((int) Math.floor(Math.log10((double) lineCount))) + 1;
            setPadding((floor * 10) + 10);

            for (int i = 1; i <= lineCount; i++) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("%");
                stringBuilder.append(floor);
                stringBuilder.append("d  ");
                canvas.drawText(String.format(stringBuilder.toString(), new Object[]{Integer.valueOf(i)}), (float) (this.rect.left + 10), (float) baseline, this.paint);
                baseline += getLineHeight();

            }
        }
        super.onDraw(canvas);
    }

    public void init(final Context context) {
        setFilters(new InputFilter[]{new MyInputFilter(this)});
        addTextChangedListener(new TextWatcher() {
                private int count = 0;
                private int start = 0;

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    this.start = start;
                    this.count = count;
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // TODO: Implement onTextChanged method
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // TODO: Implement afterTextChanged method
                    CodeView.this.cancelUpdate();
                    if (CodeView.this.modified) {
                        if (CodeView.this.isPairedAutoCloseOn) {
                            //CodeView.this.autoClose(s, this.start, this.count);
                        }
                        Log.d(CodeView.TAG, "afterTextChanged");
                        CodeView.this.dirty = true;
                        ((MainActivity) context).setContentModified(CodeView.this.dirty);
                        CodeView.this.updateHandler.postDelayed(CodeView.this.updateRunnable, (long) CodeView.this.updateDelay);
                    }
                }
            });
        setSyntaxColors(context);
    }



    public CharSequence MyInputFilter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
        return (this.modified && i2 - i == 1 && i < charSequence.length() && i3 <= spanned.length() && charSequence.charAt(i) == '\n') ? autoIndent(charSequence, i, i2, spanned, i3, i4) : charSequence;
    }

    private void cancelUpdate() {
        this.updateHandler.removeCallbacks(this.updateRunnable);
    }

    private void highlightWithoutChange(Editable editable) {
        this.modified = false;
        highlight(editable);
        this.modified = true;
    }

    private void autoClose(Editable editable, int i, int i2) {
        SpannableStringBuilder builder = new SpannableStringBuilder(getText());
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        if (selectionStart == selectionEnd) {
            int length = builder.length();
            int selection = selectionStart;
        }
        if (i2 > 0 && builder.length() > 0 && selectionStart > 0 && selectionStart == selectionEnd) {
            char prevChar = builder.charAt(selectionStart - 1);
            char nextChar = 'x';
            char prevPrevChar = 'x';
            if (selectionStart > 1) {
                prevPrevChar = builder.charAt(selectionStart - 2);
            }
            if (selectionStart < builder.length()) {
                nextChar = builder.charAt(selectionStart);
            }
            if (prevChar == '{') {
                editable.insert(selectionStart, "}");
                setSelection(selectionStart);
                return;
            }
            if (prevChar == '[') {
                editable.insert(selectionStart, "]");
                setSelection(selectionStart);
                return;
            }
            if (prevChar == '(') {
                editable.insert(selectionStart, ")");
                setSelection(selectionStart);
                return;
            }
            if (prevChar == '"') {
                editable.insert(selectionStart, "\"");
                setSelection(selectionStart);
                return;
            }
            if (prevChar == '\'') {
                editable.insert(selectionStart, "'");
                setSelection(selectionStart);
                return;
            }
        }
    }

    private void clearSpans(Editable editable) {
        ForegroundColorSpan[] foregroundColorSpanArr = (ForegroundColorSpan[]) editable.getSpans(0, editable.length(), ForegroundColorSpan.class);
        int length = foregroundColorSpanArr.length;
        while (true) {
            int i = length - 1;
            if (length <= 0) {
                break;
            }
            editable.removeSpan(foregroundColorSpanArr[i]);
            length = i;
        }
        BackgroundColorSpan[] backgroundColorSpanArr = (BackgroundColorSpan[]) editable.getSpans(0, editable.length(), BackgroundColorSpan.class);
        length = backgroundColorSpanArr.length;
        while (true) {
            int i2 = length - 1;
            if (length > 0) {
                editable.removeSpan(backgroundColorSpanArr[i2]);
                length = i2;
            } else {
                return;
            }
        }
    }



    public void setErrorLine(int i) {
        this.errorLine = i;
        refresh();
    }

    public void refresh() {
        highlightWithoutChange(getText());
    }

    public void setTabWidth(int i) {
        if (this.tabWidthInCharacters != i) {
            this.tabWidthInCharacters = i;
            this.tabWidth = Math.round(getPaint().measureText("m") * ((float) i));
        }
    }

    public void insertTab() {
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        getText().replace(Math.min(selectionStart, selectionEnd), Math.max(selectionStart, selectionEnd), "\t", 0, 1);
    }

    private void highlight(Editable editable) {
        // Set the text color to the default color
        editable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, editable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Iterate through all the patterns and highlight the matching text
        highlight(editable, PATTERN_COMMENT, colorComments);
        highlight(editable, PATTERN_COMMENT_ML, colorComments);
        highlight(editable, PATTERN_KEYWORDS, colorKeyword);
        highlight(editable, PATTERN_METHODS, colorMethods);
        highlight(editable, PATTERN_NUMS, colorNums);
        highlight(editable, PATTERN_OPERATORS, colorOperators);
        highlight(editable, PATTERN_SPECIAL, colorKeyword);
        highlight(editable, PATTERN_SPECIAL_OPS, colorOperators);
        highlight(editable, PATTERN_STRINGS, colorStrings);
        highlight(editable, PATTERN_USUAL_OPS, colorOperators);
        highlight(editable, PATTERN_VARS, colorVars);
    }

    private void highlight(Editable editable, Pattern pattern, int color) {
        Matcher matcher = pattern.matcher(editable);
        while (matcher.find()) {
            // Set the text color for the matching text
            editable.setSpan(new ForegroundColorSpan(color), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }


    private CharSequence autoIndent(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        char charAt2;
        CodeView CodeView = this;
        Spanned spanned = dest;
        int i = dstart;
        int i2 = dstart - 1;
        Object obj = null;
        int i3 = i2;
        int i4 = 0;
        while (i3 > -1) {
            char charAt = spanned.charAt(i3);
            if (charAt == '\n') {
                break;
            }
            if (!(charAt == ' ' || charAt == '\t')) {
                if (obj == null) {
                    if (charAt == '{') {
                        i4--;
                    }
                    if (charAt == '}') {
                        i4++;
                    }
                    obj = 1;
                }
                if (charAt == '(') {
                    i4--;
                } else if (charAt == ')') {
                    i4++;
                }
            }
            i3--;
        }
        charAt2 = 'x';
        char charAt3 = (spanned.length() <= 0 || i2 <= 0) ? 'x' : spanned.charAt(i2);
        if (i < spanned.length()) {
            charAt2 = dest.charAt(i);
        }
        String str = "";
        if (i3 > -1) {
            i3++;
            int i5 = dend;
            int i6 = i3;
            while (i6 < i5) {
                char charAt4 = spanned.charAt(i6);
                if (charAt4 != ' ' && charAt4 != '\t') {
                    break;
                }
                i6++;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(spanned.subSequence(i3, i6));
            str = stringBuilder.toString();
        }
        if (charAt2 == '}' && charAt3 == '{' && Objects.equals(source.toString(), "\n")) {
            final Integer valueOf = Integer.valueOf(getSelectionStart());
            final Integer valueOf2 = Integer.valueOf(str.length());
            CodeView.updateHandler.postDelayed(new Runnable() {
                    public void run() {
                        CodeView.this.setSelection((valueOf.intValue() + valueOf2.intValue()) + 2);
                    }
                }, (long) CodeView.updateDelay);
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append('\n');
            stringBuilder2.append(str);
            stringBuilder2.append('\t');
            stringBuilder2.append('\n');
            stringBuilder2.append(str);
            return stringBuilder2.toString();
        }
        if (i4 < 0) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str);
            stringBuilder2.append("\t");
            str = stringBuilder2.toString();
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(source);
        stringBuilder2.append(str);
        return stringBuilder2.toString();
    }
    private class MyInputFilter implements InputFilter {
        private CodeView parent;

        public MyInputFilter(CodeView parent) {
            this.parent = parent;
        }
        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
            return (modified && i2 - i == 1 && i < charSequence.length() && i3 <= spanned.length() && charSequence.charAt(i) == '\n') ? autoIndent(charSequence, i, i2, spanned, i3, i4) : charSequence;
        }
    }
}
