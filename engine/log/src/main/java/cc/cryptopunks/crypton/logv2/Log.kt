package cc.cryptopunks.crypton.logv2

typealias Log<S, D> = (LogLevel, BuildLogData<S, D>) -> Unit

enum class LogLevel { Verbose, Debug, Info, Warn, Error }

typealias BuildLogData<S, D> = S.() -> D

fun <S, D> Log<S, D>.v(build: BuildLogData<S, D>) = invoke(LogLevel.Verbose, build)
fun <S, D> Log<S, D>.d(build: BuildLogData<S, D>) = invoke(LogLevel.Debug, build)
fun <S, D> Log<S, D>.i(build: BuildLogData<S, D>) = invoke(LogLevel.Info, build)
fun <S, D> Log<S, D>.w(build: BuildLogData<S, D>) = invoke(LogLevel.Warn, build)
fun <S, D> Log<S, D>.e(build: BuildLogData<S, D>) = invoke(LogLevel.Error, build)

