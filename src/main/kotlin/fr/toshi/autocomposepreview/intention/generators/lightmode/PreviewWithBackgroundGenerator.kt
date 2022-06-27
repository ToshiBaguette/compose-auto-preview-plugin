package fr.toshi.autocomposepreview.intention.generators.lightmode

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import fr.toshi.autocomposepreview.intention.generators.AbsPreviewGenerator
import org.jetbrains.kotlin.psi.KtNamedFunction

class PreviewWithBackgroundGenerator(
        project: Project,
        editor: Editor,
        file: PsiFile,
        sourceFunction: KtNamedFunction
) : AbsPreviewGenerator("Create Preview With Background", project, editor, file, sourceFunction) {

    override fun actionPerformed(e: AnActionEvent) {
        this.writePreview("showBackground=true,\nbackgroundColor=0xFFFFFF")
    }

}