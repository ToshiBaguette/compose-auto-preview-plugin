<<<<<<< HEAD:src/main/kotlin/fr/toshi/autocomposepreview/listeners/MyProjectManagerListener.kt
package fr.toshi.autocomposepreview.listeners
=======
package com.github.toshibane.composeautopreview.listeners
>>>>>>> e84af9f2afe13c6be72ebebdeff0ea83400ad246:src/main/kotlin/com/github/toshibane/composeautopreview/listeners/MyProjectManagerListener.kt

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
<<<<<<< HEAD:src/main/kotlin/fr/toshi/autocomposepreview/listeners/MyProjectManagerListener.kt
import fr.toshi.autocomposepreview.services.MyProjectService
=======
import com.github.toshibane.composeautopreview.services.MyProjectService
>>>>>>> e84af9f2afe13c6be72ebebdeff0ea83400ad246:src/main/kotlin/com/github/toshibane/composeautopreview/listeners/MyProjectManagerListener.kt

internal class MyProjectManagerListener : ProjectManagerListener {

    override fun projectOpened(project: Project) {
        project.service<MyProjectService>()
    }
}