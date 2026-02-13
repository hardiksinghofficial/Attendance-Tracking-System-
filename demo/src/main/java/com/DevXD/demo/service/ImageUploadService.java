package com.DevXD.demo.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@Service
public class ImageUploadService {

    @Autowired
    private Cloudinary cloudinary;

    /**
     * Upload base64 image to Cloudinary
     * @param base64Image Base64 encoded image string
     * @param folder Folder name in Cloudinary (e.g., "attendance", "profiles")
     * @return Cloudinary URL of uploaded image
     */
    public String uploadImage(String base64Image, String folder) throws IOException {
        try {
            // Remove data:image/png;base64, prefix if present
            String imageData = base64Image;
            if (base64Image.contains(",")) {
                imageData = base64Image.split(",")[1];
            }

            // Decode base64 to bytes
            byte[] imageBytes = Base64.getDecoder().decode(imageData);

            // Upload to Cloudinary
            Map uploadResult = cloudinary.uploader().upload(imageBytes,
                    ObjectUtils.asMap(
                            "folder", folder,
                            "resource_type", "image"
                    )
            );

            // Return secure URL
            return (String) uploadResult.get("secure_url");

        } catch (Exception e) {
            throw new IOException("Failed to upload image: " + e.getMessage());
        }
    }

    /**
     * Delete image from Cloudinary
     * @param imageUrl Full Cloudinary URL
     */
    public void deleteImage(String imageUrl) {
        try {
            // Extract public_id from URL
            String publicId = extractPublicId(imageUrl);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            System.err.println("Failed to delete image: " + e.getMessage());
        }
    }

    private String extractPublicId(String imageUrl) {
        // Extract public_id from Cloudinary URL
        // Example: https://res.cloudinary.com/demo/image/upload/v1234/folder/image.jpg
        // Public ID: folder/image
        String[] parts = imageUrl.split("/upload/");
        if (parts.length > 1) {
            String path = parts[1];
            // Remove version number (v1234567890/)
            path = path.replaceFirst("v\\d+/", "");
            // Remove file extension
            return path.substring(0, path.lastIndexOf('.'));
        }
        return null;
    }
}