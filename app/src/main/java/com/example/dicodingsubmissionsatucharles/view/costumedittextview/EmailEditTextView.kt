package com.example.dicodingsubmissionsatucharles.view.costumedittextview

import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.doAfterTextChanged
import com.example.dicodingsubmissionsatucharles.R
import com.google.android.material.textfield.TextInputEditText

class EmailEditTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    init {
        doAfterTextChanged { theText ->
            if (theText != null) {
                error =
                    if (theText.contains("@") && theText.contains(".")) null else context.getString(
                        R.string.invalid_email
                    )
            }
        }
    }
}