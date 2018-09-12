package teli.com.kpcc.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by madhuri on 29/12/14.
 */
public class CcEditText extends EditText{

    public CcEditText(Context context) {
        super(context);
        init();
    }

    public CcEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CcEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

    }
}
