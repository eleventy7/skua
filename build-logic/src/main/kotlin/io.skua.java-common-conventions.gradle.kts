/*
 *
 *  * Copyright 2021-2023 Shaun Laurens
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

plugins {
    // Apply the java Plugin to add support for Java.
    checkstyle
    java
}

repositories {
    mavenLocal()
    mavenCentral()
}

checkstyle {
    maxWarnings = 0
    toolVersion = libs.findVersion("checkstyleVersion").get().toString()
}

dependencies {
    constraints {
        // Define dependency versions as constraints
        implementation("org.apache.commons:commons-text:1.10.0")
    }
}

testing {
    suites {
        // Configure the built-in test suite
        @Suppress("UNUSED_VARIABLE")
        val test by getting(JvmTestSuite::class) {
            // Use JUnit Jupiter test framework
            useJUnitJupiter(libs.findVersion("junitVersion").get().toString())
        }
    }
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
        vendor = org.gradle.jvm.toolchain.JvmVendorSpec.AZUL
    }
}
