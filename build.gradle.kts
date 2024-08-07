import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.npm.npmProject
import org.jetbrains.kotlin.incremental.deleteDirectoryContents
import java.io.File
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL

var serverPort = 9317
plugins {
    kotlin("multiplatform") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
    id("maven-publish")
}


group = "com.github.autojs_kotlin_sdk"
version = "2.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    js(IR) {
        // 是否使用 nodejs 的下载功能.让 gradle 去自动下载 node 到 gradle 缓存文件夹
        rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin> {
            rootProject.the<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension>().download = true
        }
        // 输出模块名称
        moduleName = "main"
        // 设置package.json
        compilations["main"].packageJson {
            customField("scripts", mapOf("babel" to "babel kotlin -d kotlin_babel", "build" to "webpack"))
        }
//        generateTypeScriptDefinitions() // 生成 TypeScript 声明文件 (d.ts)
//        useEsModules() // 使用 ES 模块，使用后输出 mjs 文件。
        nodejs {
            testTask {
                // 是否启用测试
                enabled = true
            }
        }
        binaries.executable()
        taskList()
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
            }
        }
    }
}


fun KotlinJsTargetDsl.taskList() {

    val mainFile = compilations.getByName("main").npmProject.dir.get().asFile        // 编译输出文件夹
    val mainKotlinFile = File(mainFile,"kotlin")                                       // 编译输出文件夹
    val configFile = buildFile.parentFile.resolve("config")                         // 配置文件夹
    val compilationFile = buildFile.parentFile.resolve("build/autojs/compilation") // 最后编译输出文件夹
    val intermediateCompilationFile =  buildFile.parentFile.resolve("build/autojs/intermediate_compilation_files") // 最后中间编译输出文件夹
    val intermediateCompilationMainJsFile =  File(intermediateCompilationFile, "main.js") // 最后中间编译输出文件夹
    val compilationMainJsFile =  File(compilationFile, "main.js")                                    // 输出最后编译文件

    val webpackIntermediateFiles = project.findProperty("autojs.webpack.intermediate.files") as String
    val useUI = project.findProperty("autojs.use.ui") as String
    val webpackAutoRun = project.findProperty("autojs.webpack.auto.run") as String
    val webpackAutoUpload = project.findProperty("autojs.webpack.auto.upload") as String

    tasks.register("run"){
        group = "autojs"
        dependsOn("compile")
        finalizedBy("httpRunProject")

        doLast {
            // 将编译后的文件复制到 mainPath
            if (compilationFile.exists()) compilationFile.deleteDirectoryContents()
            copy {
                from(intermediateCompilationFile)
                into(compilationFile)
            }
            // main.js 前面添加 "ui"; 进入UI模式
            if (compilationMainJsFile.exists() && useUI.contains("true")) {
                val content = compilationMainJsFile.readText()
                val newlineIndex1 = content.indexOf("\r\n")
                val newlineIndex2 = content.indexOf('\n')
                var firstLine = if (newlineIndex1 == -1) null else content.substring(0, newlineIndex1)
                firstLine = firstLine ?: if (newlineIndex2 == -1) "" else content.substring(0, newlineIndex2)
                if (!firstLine.contains("ui") || firstLine.length > 6) {
                    compilationMainJsFile.writeText("\"ui\";\n$content")
                }
            }
        }
    }


    tasks.register("httpRunProject") {
        group = "autojs"
        doLast {
            val isEmpty = compilationFile.list().let {
                it?.isEmpty() ?: true
            }
            if (!compilationFile.exists() && isEmpty){
                throw NullPointerException("${compilationFile.canonicalPath} path is null")
            }
            post("http://127.0.0.1:$serverPort/upload_run_path",compilationFile.canonicalPath)
        }
    }

    tasks.register("httpUploadProject") {
        group = "autojs"
        doLast {
            val isEmpty = compilationFile.list().let {
                it?.isEmpty() ?: true
            }
            if (!compilationFile.exists() && isEmpty){
                throw NullPointerException("${compilationFile.canonicalPath} path is null")
            }
            post("http://127.0.0.1:$serverPort/upload_path",compilationFile.canonicalPath)
        }
    }

    tasks.register("webpack"){
        description = "Build project with webpack"
        group = "autojs"

        dependsOn("compile")
        if (compilationMainJsFile.exists() && webpackAutoUpload.contains("true")) {
            finalizedBy("httpUploadProject")
        }

        if (compilationMainJsFile.exists() && webpackAutoRun.contains("true")) {
            finalizedBy("httpRunProject")
        }

        doLast{
            val path = mainKotlinFile.path
            // 复制中间编译文件回初次编译位置
            if (webpackIntermediateFiles.contains("true")) {
                if (mainKotlinFile.exists()) mainKotlinFile.deleteDirectoryContents()
                copy {
                    from(intermediateCompilationFile)
                    into(mainKotlinFile)
                }
            }
            // 移动配置文件到 build 项目文件夹下
            copy {
                from(configFile)
                into(mainFile)
            }

            // 执行命令
            exec {
                compilations.getByName("main").npmProject.useTool(
                    this, File(path, "..\\..\\..\\node_modules\\webpack\\bin\\webpack.js").path, emptyList(), emptyList()
                )
            }

            // 将编译后的文件复制到 mainPath
            if (compilationFile.exists()) compilationFile.deleteDirectoryContents()
            copy {
                from(mainFile.resolve("dist"))
                into(compilationFile)
            }

            // 将编译后的文件文件夹内的文件复制到 mainPath
            mainKotlinFile.listFiles()?.asSequence()?.filter {
                it.name.endsWith(".js").not()
            }?.filter {
                it.name.endsWith(".mjs").not()
            }?.filter {
                it.name.endsWith(".ts").not()
            }?.filter {
                it.name.endsWith(".mjs.map").not()
            }?.filter {
                it.name.endsWith(".js.map").not()
            }?.toList()?.forEach {
                copy {
                    println(it)
                    from(it)
                    into(compilationFile)
                }
            }

            // main.js 前面添加 "ui"; 进入UI模式
            if (compilationMainJsFile.exists() && useUI.contains("true")) {
                val content = compilationMainJsFile.readText()
                compilationMainJsFile.writeText("\"ui\";\n$content")
            }
        }
    }

    tasks.register("info") {
        group = "autojs"
        val nodeExecutable = compilations.getByName("main").npmProject.nodeJs.requireConfigured().executable
        val nodeDir = compilations.getByName("main").npmProject.nodeJs.requireConfigured().nodeBinDir
        val platformName = compilations.getByName("main").npmProject.nodeJs.requireConfigured().platformName
        val architectureName = compilations.getByName("main").npmProject.nodeJs.requireConfigured().architectureName
        val path = mainFile.path
        val mainPath = buildFile.parentFile.resolve("build/autojs")

        println("-----------------------------------------------------------------------------")
        println("> PlatformName name:       $platformName")
        println("> ArchitectureName name:   $architectureName")
        println("> Kotlin compiler version: ${KotlinVersion.CURRENT} ")
        println("> Kotlin compiler type:    ${KotlinJsCompilerType.IR}")
        println("> Node executable:         $nodeExecutable")
        println("> Node directory:          $nodeDir")
        println("> Build js project path:   $path")
        println("> Output path:             $mainPath")
        println("> Config:")
        println("   > use_ui                       :$useUI")
        println("   > webpack_intermediate_files   :$webpackIntermediateFiles")
        println("-----------------------------------------------------------------------------")
    }

    tasks.register("initEnvironment") {
        description = "Initialize environment"
        group = "autojs"
        // 设置 Node.js 环境(下载并安装 Node.js,配置 Node.js 的版本,安装 Yarn 包管理工具)
        dependsOn("kotlinNodeJsSetup")
        // 生成 package.json 文件
        dependsOn("jsPackageJson")
        // 升级 yarn.lock 文件
        dependsOn("kotlinUpgradeYarnLock")

        doLast {
            // 移动配置文件到 build 项目文件夹下
            copy {
                from(configFile)
                into(mainFile)
            }
        }
    }

    tasks.register("npmInstall") {
        description = "Update environment"
        group = "autojs"
        // 生成 package.json 文件
        dependsOn("jsPackageJson")
        // 升级 yarn.lock 文件
        dependsOn("kotlinUpgradeYarnLock")
        // 安装依赖包
        dependsOn("kotlinNpmInstall")

        doLast {
            // 移动配置文件到 build 项目文件夹下
            copy {
                from(buildFile.parentFile.resolve("config"))
                into(mainFile)
            }
        }
    }

    tasks.register("compile") {
        description = "Compile Autojs project"
        group = "autojs"
        dependsOn("initEnvironment")
        dependsOn("jsPackageJson")
        // 直接编译发布可运行 js 文件
        dependsOn("jsProductionExecutableCompileSync")
        dependsOn("compileProductionExecutableKotlinJs")

        if(intermediateCompilationMainJsFile.exists()) intermediateCompilationFile.deleteDirectoryContents()

        doLast{
            copy {
                from(mainKotlinFile)
                into(intermediateCompilationFile)
            }
            // main.js 前面添加 "ui"; 进入UI模式
            if (compilationMainJsFile.exists() && useUI.contains("true")) {
                val content = compilationMainJsFile.readText()
                compilationMainJsFile.writeText("\"ui\";\n$content")
            }
        }
    }

    tasks.register("compileContinuous") {
        dependsOn("jsPackageJson")
        dependsOn("jsProductionExecutableCompileSync")
        dependsOn("compileProductionExecutableKotlinJs")
    }
}

fun post(url:String,data:String){
    val connection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
    println("send data: $data")
    connection.requestMethod = "POST"
    connection.doOutput = true
    connection.setRequestProperty("Content-Type", "application/json")

    // Send request
    val os = connection.outputStream
    os.write(data.toByteArray())
    os.flush()
    os.close()

    // Get Response
    val responseCode = connection.responseCode
    println("Response Code : $responseCode")
    if (responseCode != 200){
        val response = connection.inputStream.bufferedReader().use { it.readText() }
        throw Exception("Response Code : $responseCode, Response: $response")
    }

    connection.disconnect()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["kotlin"])
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
        }
    }
}
