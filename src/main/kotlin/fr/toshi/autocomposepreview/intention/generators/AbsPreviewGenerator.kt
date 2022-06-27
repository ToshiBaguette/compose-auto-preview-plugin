package fr.toshi.autocomposepreview.intention.generators

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.refactoring.suggested.startOffset
import fr.toshi.autocomposepreview.capitalizeFirstLetter
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtPsiFactory

abstract class AbsPreviewGenerator(
        name: String,
        private val project: Project,
        private val editor: Editor,
        private val file: PsiFile,
        private val sourceFunction: KtNamedFunction
): AnAction(name) {
    companion object { val test = "" }

    protected fun writePreview(previewParameters: String) {
        val sourceFunctionName = sourceFunction.name
        val newFunctionName = "Preview" + sourceFunctionName?.capitalizeFirstLetter()

        var functionBody = "@Preview"
        if (previewParameters.isNotEmpty()) {
            functionBody += "(\n$previewParameters\n)"
        }

        functionBody += "\n@Composable\nfun $newFunctionName() {\n$sourceFunctionName()\n}"

        WriteCommandAction.writeCommandAction(project, file).run<Throwable> {
            val factory = KtPsiFactory(project)
            val newFunction = file.add(factory.createFunction(functionBody))

            editor.caretModel.primaryCaret.moveToOffset(newFunction.startOffset + functionBody.length - 3)
            editor.scrollingModel.scrollToCaret(ScrollType.CENTER)
        }
    }
}