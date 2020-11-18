
# Permissions
编译时权限申请框架

# 集成步骤：

## 1、在工程的build.gradle文件中添加配置：

allprojects {

    repositories {
	
		...
		
		maven { url 'https://jitpack.io' }
		
	}	
}


## 2、在app或module的build.gradle文件中添加配置：

dependencies {
  
    implementation 'com.github.ldljheropt:Permissions:1.0'
}

## 注意app和moulde中添加配置的区别

## 3、在app的build.gradle文件末尾添加配置：


buildscript {

    repositories {
    
        mavenCentral()
	
    }

    dependencies {
        classpath 'org.aspectj:aspectjtools:1.8.9'
        classpath 'org.aspectj:aspectjweaver:1.8.9'
    }
}


import org.aspectj.bridge.IMessage

import org.aspectj.bridge.MessageHandler

import org.aspectj.tools.ajc.Main

final def log = project.logger
final def variants = project.android.applicationVariants

variants.all { variant ->

    if (!variant.buildType.isDebuggable()) {
        log.debug("Skipping non-debuggable build type '${variant.buildType.name}'.")
        return;
    }

    JavaCompile javaCompile = variant.javaCompile

    javaCompile.doLast {

        String[] args = ["-showWeaveInfo",

                         "-1.8",

                         "-inpath", javaCompile.destinationDir.toString(),

                         "-aspectpath", javaCompile.classpath.asPath,

                         "-d", javaCompile.destinationDir.toString(),

                         "-classpath", javaCompile.classpath.asPath,

                         "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)]

        log.debug "ajc args: " + Arrays.toString(args)

        MessageHandler handler = new MessageHandler(true);

        new Main().run(args, handler);

        for (IMessage message : handler.getMessages(null, true)) {

            switch (message.getKind()) {

                case IMessage.ABORT:

                case IMessage.ERROR:

                case IMessage.FAIL:

                    log.error message.message, message.thrown

                    break;

                case IMessage.WARNING:

                    log.warn message.message, message.thrown

                    break;

                case IMessage.INFO:

                    log.info message.message, message.thrown

                    break;

                case IMessage.DEBUG:

                    log.debug message.message, message.thrown

                    break;
            }
        }
    }
}


## 在module的build.gradle文件末尾添加配置：


buildscript {

    repositories {
    
        mavenCentral()
	
    }

    dependencies {
        classpath 'org.aspectj:aspectjtools:1.8.9'
        classpath 'org.aspectj:aspectjweaver:1.8.9'
    }
}

import org.aspectj.bridge.IMessage

import org.aspectj.bridge.MessageHandler

import org.aspectj.tools.ajc.Main

import com.android.build.gradle.LibraryPlugin

final def log = project.logger
final def variants = android.libraryVariants

variants.all { variant ->

    LibraryPlugin plugin = project.plugins.getPlugin(LibraryPlugin)

    JavaCompile javaCompile = variant.javaCompile

    javaCompile.doLast {

        String[] args = ["-showWeaveInfo",

                         "-1.8",

                         "-inpath", javaCompile.destinationDir.toString(),

                         "-aspectpath", javaCompile.classpath.asPath,

                         "-d", javaCompile.destinationDir.toString(),

                         "-classpath", javaCompile.classpath.asPath,

                         "-bootclasspath", plugin.project.android.bootClasspath.join(File.pathSeparator)]

        log.debug "ajc args: " + Arrays.toString(args)

        MessageHandler handler = new MessageHandler(true);

        new Main().run(args, handler);

        for (IMessage message : handler.getMessages(null, true)) {

            switch (message.getKind()) {

                case IMessage.ABORT:

                case IMessage.ERROR:

                case IMessage.FAIL:

                    log.error message.message, message.thrown

                    break;

                case IMessage.WARNING:

                    log.warn message.message, message.thrown

                    break;

                case IMessage.INFO:

                    log.info message.message, message.thrown

                    break;

                case IMessage.DEBUG:

                    log.debug message.message, message.thrown

                    break;
            }
        }
    }
}

## 4、在Activity或Fragment中添加注解：

    @Permission(value = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, requestCode = 200,isJumpSettings = true)
    public void testRequest() {
        Toast.makeText(this, "权限申请成功...", Toast.LENGTH_SHORT).show();

    }

    @PermissionCancel
    public void testCancel() {
        Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied
    public void testDenied() {
        Toast.makeText(this, "权限被拒绝（用户勾选了，不再提醒）", Toast.LENGTH_SHORT).show();
    }
    
## 5、AndroidManifest.xml文件中增加需要申请的权限列表：

    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.CAMERA"/>
	
	

