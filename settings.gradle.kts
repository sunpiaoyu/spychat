/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
//        google()
//        mavenCentral()
        // 阿里云云效仓库：https://maven.aliyun.com/mvn/guide
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }

        // 华为开源镜像：https://mirrors.huaweicloud.com
        maven { url = uri("https://repo.huaweicloud.com/repository/maven") }
        // JitPack 远程仓库：https://jitpack.io
        maven { url = uri("https://jitpack.io") }
        // MavenCentral 远程仓库：https://mvnrepository.com
        mavenCentral()
        google()
        // noinspection JcenterRepositoryObsolete
        jcenter()
    }
}

rootProject.name = "spychat"
include(":app")