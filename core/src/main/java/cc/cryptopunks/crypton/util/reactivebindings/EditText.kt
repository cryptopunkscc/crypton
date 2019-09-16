package cc.cryptopunks.crypton.util.reactivebindings

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber

private class TextChanges(
    private var editText: EditText
) : ViewPublisher<CharSequence>(), TextWatcher {

    override fun onSubscribed(subscriber: Subscriber<in CharSequence>) {
        editText.addTextChangedListener(this)
    }

    override fun onCanceled() {
        editText.removeTextChangedListener(this)
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        subscriber?.onNext(s)
    }

    override fun afterTextChanged(s: Editable?) {
        /*no-op*/
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        /*no-op*/
    }
}

fun EditText.textChangesPublisher(): Publisher<CharSequence> =
    TextChanges(this)