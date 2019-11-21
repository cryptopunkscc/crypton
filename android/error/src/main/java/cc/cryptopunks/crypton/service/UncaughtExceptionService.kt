package cc.cryptopunks.crypton.service

import android.os.Environment
import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.d
import cc.cryptopunks.crypton.util.e
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*

fun initExceptionService() = Thread.setDefaultUncaughtExceptionHandler(
    UncaughtExceptionService(
        Environment
            .getExternalStorageDirectory()
            .resolve(cryptonDirName)
    )
)

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
