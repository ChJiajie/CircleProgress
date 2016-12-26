# Introduction #

wait update...
# How to #
## To get a Git project into your build: ##
Step 1. Add the JitPack repository to your build file

    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Step 2. Add the dependency

	compile 'com.github.ChJiajie:CircleProgress:0.0.1'