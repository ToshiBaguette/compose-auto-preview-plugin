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
    private fun getPositionOfPreview(): KtNamedFunction? {
        val methods = file.children.filterIsInstance<KtNamedFunction>()
        val methodNames = methods.map { it.name }

        val startIndex = methods.indexOf(sourceFunction)
        for (i in startIndex downTo 0) {
            val previousFunction = methods[i]

            if (methodNames.contains("Preview${previousFunction.name?.capitalizeFirstLetter()}")) {
                return methods[methodNames.indexOf("Preview${previousFunction.name?.capitalizeFirstLetter()}")]
            }
        }

        return null
    }

    private fun getFirstPreviewOfFile(): KtNamedFunction? {
        val methods = file.children.filterIsInstance<KtNamedFunction>()
        val previews = methods.filter { it.name?.startsWith("Preview") ?: false }

        return if (previews.isEmpty()) null else methods[methods.indexOf(previews[0])]
    }
    
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

            val previousElement = getPositionOfPreview()
            val newFunction = if (previousElement == null) {
                val firstPreview = getFirstPreviewOfFile()

                if (firstPreview == null) {
                    file.add(factory.createFunction(functionBody))
                } else {
                    firstPreview.parent.addBefore(factory.createFunction("\n$functionBody"), firstPreview)
                }

            } else {
                previousElement.parent.addAfter(factory.createFunction("\n$functionBody"), previousElement)
            }

            editor.caretModel.primaryCaret.moveToOffset(newFunction.startOffset + functionBody.length - 3)
            editor.scrollingModel.scrollToCaret(ScrollType.CENTER)
        }
    }
}