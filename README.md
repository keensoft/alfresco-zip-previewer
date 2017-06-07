
Alfresco ZIP Previewer (also RAR supported now!)
================================================

This add-on provide a preview for a **ZIP** or **RAR** file including a hierarchical list of all entries inside that compressed file.

![alfresco-zip-previewer](https://cloud.githubusercontent.com/assets/5584952/26724443/b9c0bda8-4799-11e7-978d-6da9a4ca802f.png)

As entry list is generated by a custom transformer at rendition phase, files and folders are also available when searching.

![alfresco-zip-previewer-2](https://cloud.githubusercontent.com/assets/5584952/26724456/c65aa3d0-4799-11e7-8fa1-ad3655507f3a.png)

**License**
The plugin is licensed under the [LGPL v3.0](http://www.gnu.org/licenses/lgpl-3.0.html). 

**State**
Current addon release is 1.0.0

**Compatibility**
The current version has been developed using Alfresco 201702 and Alfresco SDK 3.0.0

***No original Alfresco resources have been overwritten***

Thanks to Adarsh Ramamurthy for providing a [RARoScope](https://github.com/radarsh/raroscope) tool.

Downloading the ready-to-deploy-plugin
--------------------------------------
The binary distribution is made of one JAR file to be deployed in Alfresco as a repo module:

* [repo JAR](https://github.com/keensoft/alfresco-zip-previewer/releases/download/1.1.0/zip-previewer-repo-1.1.0.jar)

You can install it by copying JAR file to `$ALFRESCO_HOME/modules/platform` and re-starting Alfresco.


Building the artifacts
----------------------
You can build the artifacts from source code using maven
```$ mvn clean package```
