package cc.cryptopunks.crypton.app.util

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import cc.cryptopunks.kache.core.Kache
import cc.cryptopunks.kache.core.invoke
import cc.cryptopunks.kache.rxjava.observable
import cc.cryptopunks.crypton.app.R
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.disposables.EmptyDisposable
import java.util.concurrent.atomic.AtomicReference

@BindingAdapter("app:property")
fun TextView?.bind(propery: Kache<String>?) {
    this ?: return
    propery ?: return
    disposables.addAll(
        propery.observable().subscribe {
            text = it
        }
    )
}

@BindingAdapter("app:property")
fun EditText?.bind(property: Kache<Input>?) {
    this ?: return
    property ?: return
    val last = AtomicReference<Input>()
    disposables.addAll(
        property.observable()
            .filter {
                last.getAndSet(it) != it
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
                        .also(last::set)
                }
            }
    )
}


@BindingAdapter("app:property")
fun TextInputLayout?.bind(property: Kache<Input>?) {
    this ?: return
    property ?: return
    val last = AtomicReference<Input>()
    disposables.addAll(
        property.observable()
            .filter {
                last.getAndSet(it) != it
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
                            .also(last::set)
                    }
                }
        } ?: EmptyDisposable.INSTANCE
    )
}

@BindingAdapter(value = ["app:property"])
fun View?.bind(property: Kache<Long>?) {
    this ?: return
    property ?: return
    disposables.addAll(
        clicks().subscribe {
            property {
                plus(1)
            }
        }
    )
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