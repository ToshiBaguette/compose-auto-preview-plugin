package fr.toshi.autocomposepreview.intention.generators.darkmode

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import fr.toshi.autocomposepreview.intention.generators.AbsPreviewGenerator
import org.jetbrains.kotlin.psi.KtNamedFunction

class PreviewDarkModeWithSystemUIGenerator(
        project: Project,
        editor: Editor,
        file: PsiFile,
        sourceFunction: KtNamedFunction
): AbsPreviewGenerator("[DARK MODE] Create Preview With System UI", project, editor, file, sourceFunction) {
    override fun actionPerformed(e: AnActionEvent) {
        this.writePreview("uiMode = Configuration.UI_MODE_NIGHT_YES,\nshowSystemUi=true")
    }
}