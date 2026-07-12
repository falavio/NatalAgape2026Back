import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.exif.ExifIFD0Directory
import java.awt.Image
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import javax.imageio.ImageWriter

fun compressImageToMaxSize(
    imageBytes: ByteArray,
    format: String = "jpg", // "jpg", "jpeg", "png", etc.
    maxSizeKB: Int = 500 * 1024
): ByteArray {
    val maxSize = maxSizeKB
    val inputStream = ByteArrayInputStream(imageBytes)
    val originalImage: BufferedImage = ImageIO.read(inputStream)

    val correctedImage = correctOrientation(imageBytes, originalImage)

    // Redimensiona para 50%
    val newWidth = correctedImage.width / 2
    val newHeight = correctedImage.height / 2
    val imageType = if (format.equals("png", true)) BufferedImage.TYPE_INT_ARGB else BufferedImage.TYPE_INT_RGB
    val scaledImage = correctedImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH)
    val resizedImage = BufferedImage(newWidth, newHeight, imageType)
    val g2d = resizedImage.createGraphics()
    g2d.drawImage(scaledImage, 0, 0, null)
    g2d.dispose()

    var quality = 1.0f
    var compressed: ByteArray

    do {
        val outputStream = ByteArrayOutputStream()
        val writers = ImageIO.getImageWritersByFormatName(format)
        if (!writers.hasNext()) throw IllegalArgumentException("Formato não suportado: $format")
        val writer: ImageWriter = writers.next()
        val ios = ImageIO.createImageOutputStream(outputStream)
        writer.output = ios

        val param: ImageWriteParam = writer.defaultWriteParam
        if (param.canWriteCompressed()) {
            param.compressionMode = ImageWriteParam.MODE_EXPLICIT
            // Para JPEG, usa quality; para PNG, pode usar compressionLevel (0-9)
            if (format.equals("jpg", true) || format.equals("jpeg", true)) {
                param.compressionQuality = quality
            } else if (format.equals("png", true)) {
                // PNG usa compressionLevel, se suportado
                param.setCompressionQuality(quality) // Nem todos os writers suportam, pode ser ignorado
            }
        }

        writer.write(null, IIOImage(resizedImage, null, null), param)
        ios.close()
        writer.dispose()

        compressed = outputStream.toByteArray()
        quality -= 0.05f
    } while (compressed.size > maxSize)

    return compressed
}


fun correctOrientation(imageBytes: ByteArray, originalImage: BufferedImage): BufferedImage {
    val metadata = ImageMetadataReader.readMetadata(ByteArrayInputStream(imageBytes))
    val directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory::class.java)
    val orientation = directory?.getInt(ExifIFD0Directory.TAG_ORIENTATION) ?: 1

    val transform = AffineTransform()
    when (orientation) {
        6 -> { // 90 graus CW
            transform.translate(originalImage.height.toDouble(), 0.0)
            transform.rotate(Math.toRadians(90.0))
        }
        3 -> { // 180 graus
            transform.translate(originalImage.width.toDouble(), originalImage.height.toDouble())
            transform.rotate(Math.toRadians(180.0))
        }
        8 -> { // 270 graus CW (ou 90 graus CCW)
            transform.translate(0.0, originalImage.width.toDouble())
            transform.rotate(Math.toRadians(270.0))
        }
        // outros casos: flip, etc.
    }

    if (orientation == 1) return originalImage // Sem rotação

    val op = AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR)
    val newWidth = if (orientation == 6 || orientation == 8) originalImage.height else originalImage.width
    val newHeight = if (orientation == 6 || orientation == 8) originalImage.width else originalImage.height
    val rotatedImage = BufferedImage(newWidth, newHeight, originalImage.type)
    op.filter(originalImage, rotatedImage)
    return rotatedImage
}