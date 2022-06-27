package fr.toshi.autocomposepreview.intention

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.ListPopup
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import fr.toshi.autocomposepreview.intention.generators.PreviewGeneratorFactory
import fr.toshi.autocomposepreview.intention.generators.darkmode.PreviewDarkModeWithBackgroundGenerator
import fr.toshi.autocomposepreview.intention.generators.darkmode.PreviewDarkModeWithSystemUIGenerator
import fr.toshi.autocomposepreview.intention.generators.darkmode.SimpleDarkModePreviewGenerator
import fr.toshi.autocomposepreview.intention.generators.lightmode.PreviewWithBackgroundGenerator
import fr.toshi.autocomposepreview.intention.generators.lightmode.PreviewWithSystemUIGenerator
import fr.toshi.autocomposepreview.intention.generators.lightmode.SimplePreviewGenerator
import org.jetbrains.kotlin.psi.KtNamedFunction

class ComposeAutoPreview: PsiElementBaseIntentionAction(), IntentionAction {
    override fun getText(): String = "Generate Compose Preview"
    override fun getFamilyName(): String = text

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        val sourceFunction =
                element as? KtNamedFunction ?: PsiTreeUtil.getParentOfType(element, KtNamedFunction::class.java)
                ?: return false

        var isComposable = false
        var isPreview = false
        sourceFunction.annotationEntries.forEach {
            if (it.text.contains("Composable"))
                isComposable = true

            if (it.text.contains("Preview"))
                isPreview = true
        }

        return isComposable && !isPreview
    }

    override fun invoke(project: Project, editor: Editor, element: PsiElement) {
        val sourceFunction =
                element as? KtNamedFunction ?: PsiTreeUtil.getParentOfType(element, KtNamedFunction::class.java)
                ?: return

        val children = PreviewGeneratorFactory.createGenerators(
                listOf(
                        SimplePreviewGenerator::class,
                        PreviewWithSystemUIGenerator::class,
                        PreviewWithBackgroundGenerator::class,
                        SimpleDarkModePreviewGenerator::class,
                        PreviewDarkModeWithSystemUIGenerator::class,
                        PreviewDarkModeWithBackgroundGenerator::class
                ),
                project,
                editor,
                element.containingFile,
                sourceFunction
        )

        val popupList: ListPopup = JBPopupFactory
                .getInstance()
                .createActionGroupPopup(
                        "Compose Auto Preview",
                        PopupGroup(children),
                        DataManager
                                .getInstance()
                                .getDataContext(editor.contentComponent),
                        JBPopupFactory.ActionSelectionAid.MNEMONICS,
                        false
                )
        popupList.showInBestPositionFor(editor)
    }

    private class PopupGroup (val children: List<AnAction?>): ActionGroup() {
        override fun getChildren(e: AnActionEvent?): Array<AnAction> = children.filterNotNull().toTypedArray()
    }
}