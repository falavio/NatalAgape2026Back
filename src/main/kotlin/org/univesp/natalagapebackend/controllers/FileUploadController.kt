package org.univesp.natalagapebackend.controllers


import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.univesp.natalagapebackend.services.GoogleDriveService

@RestController
@RequestMapping("/api/upload")
class UploadController(private val googleDriveService: GoogleDriveService) {

    @PostMapping
    fun uploadFile(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        return try {
            val fileId = googleDriveService.uploadFile(file)
            ResponseEntity.ok(fileId)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().body("Error uploading file: ${e.message}")
        }
    }

    @GetMapping("/open/{fileId}")
    fun openFile(@PathVariable fileId: String): ResponseEntity<ByteArray> {
        return try {
            val (fileContent, mimeType) = googleDriveService.openFile(fileId)
            ResponseEntity.ok()
                .header("Content-Type", mimeType)
                .header("Content-Disposition", "inline") // Permite visualização direta no navegador
                .body(fileContent)
        } catch (_: Exception) {
            ResponseEntity.status(500).body(null)
        }
    }
}