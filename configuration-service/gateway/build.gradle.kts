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

plugins {
    id("io.skua.java-application-conventions")
}

dependencies {
    implementation(libs.aeron)
    implementation(libs.slf4j)
    implementation(libs.logback)
    implementation(libs.nassauCore)
    implementation(projects.configurationService.protocol)
}

application {
    // Define the main class for the application.
    mainClass.set("io.skua.app.Gateway")
}
