package org.univesp.natalagapebackend.services

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import compressImageToMaxSize
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

@Service
class GoogleDriveService {

    private val logger: Logger = LoggerFactory.getLogger(GoogleDriveService::class.java)
    private val drive: Drive

    init {
        logger.info("Initializing GoogleDriveService")

        val credential = getCredential()

        drive = Drive.Builder(
            credential.transport,
            credential.jsonFactory,
            credential
        ).setApplicationName("Drive Upload Example").build()

        logger.info("Google Drive service initialized successfully")
    }

    private fun getCredentialFromEnvironment(): GoogleCredential {
        try {
            // Obter credenciais JSON do ambiente
            val credentialsJson = System.getenv("GOOGLE_CREDENTIALS")
                ?: throw RuntimeException("Variável de ambiente GOOGLE_CREDENTIALS não encontrada")

            // Criar InputStream a partir da string JSON
            val credentialsStream = ByteArrayInputStream(credentialsJson.toByteArray())

            return GoogleCredential.fromStream(credentialsStream)
                .createScoped(listOf("https://www.googleapis.com/auth/drive.file"))
        } catch (e: Exception) {
            logger.error("Erro ao carregar credenciais do Google Drive: ${e.message}")
            throw RuntimeException("Falha ao carregar credenciais do Google Drive", e)
        }
    }

    private fun getCredential(): GoogleCredential {
        return when (System.getenv("APP_PROFILE")) {
            "PROD" -> getCredentialFromEnvironment()
            else -> getCredentialFromFile() // Método para desenvolvimento local
        }
    }

    private fun getCredentialFromFile(): GoogleCredential {
        val credentialsStream: InputStream = this::class.java.getResourceAsStream("/credentials.json")
            ?: throw RuntimeException("Credenciais não encontradas")

        return GoogleCredential.fromStream(credentialsStream)
            .createScoped(listOf("https://www.googleapis.com/auth/drive.file"))
    }

    fun uploadFile(file: MultipartFile): String {
        logger.info("Starting file upload: ${file.originalFilename}")
        val fileMetadata = File()
        fileMetadata.name = file.originalFilename

        val fileExtension = file.originalFilename?.substringAfterLast('.', "") ?: ""
        val extensionNameAllow = listOf("jpg", "jpeg", "png")

        if (fileExtension !in extensionNameAllow) {
            logger.error("Unsupported file extension: ${file.originalFilename}")
            throw IllegalArgumentException("Unsupported file extension: $fileExtension")
        }
        var fileBytes = file.bytes
        val maxSize = 500 * 1024  // 500 KB

        // Compacta toda imagem maior que 5MB
        if (fileBytes.size > maxSize) {
            logger.info("Compressing image before upload")
            fileBytes = compressImageToMaxSize(fileBytes, fileExtension, maxSize)
        }

        val fileContent = ByteArrayInputStream(fileBytes)
        val mediaContent = com.google.api.client.http.InputStreamContent(
            file.contentType,
            fileContent
        )






        return try {
            val uploadedFile = drive.files().create(fileMetadata, mediaContent)
                .setFields("id, webViewLink")
                .execute()
            logger.info("File uploaded successfully: ${uploadedFile.id} with link ${uploadedFile.webViewLink}")
            uploadedFile.id
        } catch (e: Exception) {
            logger.error("Error uploading file: ${e.message}", e)
            throw RuntimeException("Error uploading file", e)
        }
    }

    fun openFile(fileId: String): Pair<ByteArray, String> {
        return try {
            val file = drive.files().get(fileId).setFields("id, name, mimeType").execute()
            val outputStream = ByteArrayOutputStream()
            drive.files().get(fileId).executeMediaAndDownloadTo(outputStream)
            logger.info("File opened successfully: ${file.name}")
            Pair(outputStream.toByteArray(), file.mimeType)
        } catch (e: Exception) {
            logger.error("Error opening file: ${e.message}", e)
            throw RuntimeException("Error opening file", e)
        }
    }
}