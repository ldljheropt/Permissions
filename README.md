
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


## 2、在app/module的build.gradle文件中添加配置：
dependencies {
  
    implementation 'com.github.ldljheropt:Permissions:1.0'
}
