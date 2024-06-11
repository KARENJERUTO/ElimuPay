package com.emt.elimupay

import com.emt.elimupay.models.FeeEntity
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import java.io.OutputStream

class PdfGenerator {

    fun generatePdf(feeEntities: List<FeeEntity>, outputStream: OutputStream) {
        // Create a PdfWriter with the provided OutputStream
        val writer = PdfWriter(outputStream)

        // Create a PdfDocument using the PdfWriter
        val pdfDocument = PdfDocument(writer)

        // Create a Document instance
        val document = Document(pdfDocument)

        // Add content to the document
        for (feeEntity in feeEntities) {
            document.add(Paragraph("Description: ${feeEntity.description}, Debit: ${feeEntity.debit}, Credit: ${feeEntity.credit}, Balance: ${feeEntity.balance}, Date: ${feeEntity.transaction_date}"))
        }

        // Close the Document and PdfDocument
        document.close()
        pdfDocument.close()
    }
}
