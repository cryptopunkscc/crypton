package cc.cryptopunks.crypton.core.util

import android.view.View
import android.widget.EditText
import android.widget.TextView
import cc.cryptopunks.crypton.core.R
import cc.cryptopunks.kache.core.Kache
import cc.cryptopunks.kache.core.invoke
import cc.cryptopunks.kache.rxjava.observable
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.disposables.EmptyDisposable
import java.util.concurrent.atomic.AtomicReference

fun TextView.bind(propery: Kache<String>) = propery.observable().subscribe {
    text = it
}

fun EditText.bind(property: Kache<Input>) = AtomicReference<Input>().run {
    CompositeDisposable(
        property.observable()
            .filter {
                getAndSet(it) != it
            }
            .subscribe { input ->
                setText(input.text)
                error = input.error
                    .takeIf(String::isNotBlank)
            },
        textChanges()
            .map(CharSequence::toString)
            .distinctUntilChanged()
            .subscribe { text ->
                property {
                    copy(text = text.toString())
                        .also(::set)
                }
            }
    )
}


fun TextInputLayout.bind(property: Kache<Input>) = AtomicReference<Input>().run {
    CompositeDisposable(
        property.observable()
            .filter {
                getAndSet(it) != it
            }
            .subscribe { input ->
                editText?.setText(input.text)
                error = input.error
                    .takeIf(String::isNotBlank)
            },
        editText?.let {
            it.textChanges()
                .map(CharSequence::toString)
                .distinctUntilChanged()
                .subscribe { text ->
                    property {
                        copy(text = text.toString())
                            .also(::set)
                    }
                }
        } ?: EmptyDisposable.INSTANCE
    )
}

fun View.bind(property: Kache<Long>) = clicks().subscribe {
    property {
        plus(1)
    }
}

fun View.bind(disposable: CompositeDisposable){
    setTag(DISPOSABLE_TAG, disposable)
}

private val DISPOSABLE_TAG = R.id.viewDisposable

private val View.disposables: CompositeDisposable get() = AtomicReference<CompositeDisposable>().apply {
    ViewParentIterator(this@disposables).asSequence().firstOrNull {
        it.getTag(DISPOSABLE_TAG)?.let { set(it as CompositeDisposable) } != null
    }
}.get()