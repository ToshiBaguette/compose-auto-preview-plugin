package com.github.toshibane.composeautopreview.services

import com.intellij.openapi.project.Project
import com.github.toshibane.composeautopreview.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
