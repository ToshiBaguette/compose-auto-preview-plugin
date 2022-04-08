<<<<<<< HEAD:src/main/kotlin/fr/toshi/autocomposepreview/services/MyProjectService.kt
package fr.toshi.autocomposepreview.services

import com.intellij.openapi.project.Project
import fr.toshi.autocomposepreview.MyBundle
=======
package com.github.toshibane.composeautopreview.services

import com.intellij.openapi.project.Project
import com.github.toshibane.composeautopreview.MyBundle
>>>>>>> e84af9f2afe13c6be72ebebdeff0ea83400ad246:src/main/kotlin/com/github/toshibane/composeautopreview/services/MyProjectService.kt

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}