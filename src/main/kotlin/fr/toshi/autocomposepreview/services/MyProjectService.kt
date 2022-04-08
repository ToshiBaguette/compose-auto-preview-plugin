package fr.toshi.autocomposepreview.services

import com.intellij.openapi.project.Project
import fr.toshi.autocomposepreview.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}