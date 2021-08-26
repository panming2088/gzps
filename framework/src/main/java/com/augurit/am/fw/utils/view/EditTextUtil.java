package com.augurit.am.fw.utils.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ac on 2016-07-26.
 */
public final class EditTextUtil {
    private EditTextUtil() {
    }

    public static void setDisableInput(EditText editText) {
        editText.setCursorVisible(false);
        editText.setInputType(EditorInfo.IME_ACTION_NONE);
        editText.setEnabled(false);
        editText.setFocusable(false);
    }

    public static void showEditTextErrorTip(EditText editText, CharSequence errorTip, Boolean isError) {
        if (isError) {
            editText.setError(errorTip);
            editText.requestFocus();
        } else {
            editText.setError(null);
        }
    }

    /**
     * EditText输入校验与提示
     *
     * @param context
     * @param isShake         采用抖动+toast还是android原生error提示
     * @param editTextObjects 需要校验的EditText，以及提示语Id（可选）
     * @return
     */
    public static boolean verifyInput(Context context, boolean isShake, Object... editTextObjects) {
        List<Object> editTextObjectList = ListUtil.getList(editTextObjects);
        List<EditText> editTextList = new ArrayList<>();
        if (!ListUtil.isEmpty(editTextObjectList)) {
            int editTextObjectSize = editTextObjectList.size();
            if (editTextObjectSize == 1) {
                EditText et = (EditText) editTextObjectList.get(0);
                boolean isInputError =
                        shakeEditTextOrNot(context, isShake, et, -1);
                if (!isInputError) {
                    return isInputError;
                }
            } else {
                for (int i = 0; i + 1 < editTextObjectSize; i = i + 2) {
                    EditText et = (EditText) editTextObjectList.get(i);
                    Object toastObject = editTextObjectList.get(i + 1);
                    if (toastObject instanceof Integer) {
                        boolean isInputError =
                                shakeEditTextOrNot(context, isShake, et, (int) toastObject);
                        if (!isInputError) {
                            return isInputError;
                        }
                    } else if (toastObject instanceof EditText) {
                        boolean isInputError =
                                shakeEditTextOrNot(context, isShake, et, -1);
                        if (!isInputError) {
                            return isInputError;
                        }
                        boolean isInputErrorTwo =
                                shakeEditTextOrNot(context, isShake, (EditText) toastObject, -1);
                        if (!isInputErrorTwo) {
                            return isInputErrorTwo;
                        }
                    } else {
                        throw new IllegalArgumentException("A EditText should be followed by a toast id");
                    }
                }
            }
        }
        return true;
    }

    public static boolean shakeEditTextOrNot(Context context, boolean isShake, EditText et, int toastId) {
        if (TextUtils.isEmpty(et.getText().toString())) {
            if (toastId == -1) {
                toastId = ResourceUtil.getStringId(context, "common_input_verify");
            }
            if (isShake) {
                et.requestFocus();
                ToastUtil.shortToast(context, toastId);
                Animation shake = AnimationUtils.loadAnimation(context, ResourceUtil.getAnimId(context, "input_shake"));
                et.startAnimation(shake);
            } else {
                et.setError(ResourceUtil.getString(context, toastId + ""));
                et.requestFocus();
            }

            return false;
        }
        return true;
    }
}
