plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}
gradlePlugin {
    plugins {
        register("crypton") {
            id = "crypton"
            implementationClass = "cc.cryptopunks.crypton.CryptonPlugin"
        }
    }
}
dependencies {
    testImplementation("junit:junit:4.13")
}
