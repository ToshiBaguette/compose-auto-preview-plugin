package fr.toshi.autocomposepreview.intention.generators.lightmode

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import fr.toshi.autocomposepreview.intention.generators.AbsPreviewGenerator
import org.jetbrains.kotlin.psi.KtNamedFunction

class SimplePreviewGenerator(
        project: Project,
        editor: Editor,
        file: PsiFile,
        sourceFunction: KtNamedFunction,
) : AbsPreviewGenerator("Create Simple Preview", project, editor, file, sourceFunction) {

    override fun actionPerformed(e: AnActionEvent) {
        this.writePreview("")
    }

}