package cc.cryptopunks.crypton.service

import android.content.Context
import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.d
import cc.cryptopunks.crypton.util.e
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*

fun Context.initExceptionService() = Thread.setDefaultUncaughtExceptionHandler(
    UncaughtExceptionService(getCryptonDir())
)

private fun Context.getCryptonDir() = externalCacheDir!!.absolutePath
    .split("Android")
    .first()
    .let(::File)
    .resolve(cryptonDirName)
    .takeIf { (it.exists() && it.canWrite()) || it.mkdirs() }
    ?: getExternalFilesDir(null)!!


private class UncaughtExceptionService(
    private val externalFilesDir: File
) :
    Thread.UncaughtExceptionHandler {

    private val previousHandler = Thread.getDefaultUncaughtExceptionHandler()!!

    override fun uncaughtException(
        thread: Thread,
        throwable: Throwable
    ) {
        externalFilesDir.writeStackTrace(
            throwable
        )
        previousHandler.uncaughtException(
            thread,
            throwable
        )
    }
}

private fun File.writeStackTrace(
    throwable: Throwable
) = try {
    val stackTrace = throwable
        .printAsString()

    crashReportsDir()
        .resolve(fileName())
        .also {
            Log.d<UncaughtExceptionService>(it)
        }
        .let(::FileWriter)
        .append(stackTrace)
        .run {
            flush()
            close()
        }
} catch (throwable: Throwable) {
    Log.e<UncaughtExceptionService>(throwable)
}

private fun Throwable.printAsString() = StringWriter().also {
    PrintWriter(it)
        .also(this::printStackTrace)
        .close()
}.toString()

private fun File.crashReportsDir() = this
    .resolve(crashDirName)
    .apply { if (!exists()) mkdirs() }

private fun fileName() =
    filePrefix + SimpleDateFormat(
        dateFormat,
        Locale.getDefault()
    ).format(
        System.currentTimeMillis()
    )


private const val cryptonDirName = "Crypton"
private const val crashDirName = "crashes"
private const val filePrefix = "crash_"
private const val dateFormat = "yyyy_MM_dd_HH_mm_ss"
