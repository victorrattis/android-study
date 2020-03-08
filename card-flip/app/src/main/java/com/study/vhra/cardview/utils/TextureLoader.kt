package com.study.vhra.cardview.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20.*
import android.opengl.GLUtils

class TextureLoader(
    private val context: Context
) {
    private val textureMap: MutableMap<Int, String> = HashMap()

    fun loadTexture(texture: String, textureUnit: Int) {
        if (texture.contains("drawable://")
            && (!textureMap.containsKey(textureUnit)
                    || !textureMap[textureUnit].equals(texture))) {
            val id = context.resources.getIdentifier(
                texture.replace("drawable://", ""),
                "drawable",
                context.packageName)

            glActiveTexture(textureUnit)
            glBindTexture(GL_TEXTURE_2D, loadTexture(id))

            textureMap[textureUnit] = texture
        }
    }

    private fun loadTexture(resourceId: Int): Int {
        val textureHandle = IntArray(1)

        glGenTextures(1, textureHandle, 0)

        if (textureHandle[0] != 0) {
            val options = BitmapFactory.Options()
            options.inScaled = false   // No pre-scaling

            // Read in the resource
            val bitmap = BitmapFactory.decodeResource(context.resources, resourceId, options)

            // Bind to the texture in OpenGL
            glBindTexture(GL_TEXTURE_2D, textureHandle[0])

            // Set filtering
            glTexParameteri(
                GL_TEXTURE_2D,
                GL_TEXTURE_MIN_FILTER,
                GL_NEAREST
            )
            glTexParameteri(
                GL_TEXTURE_2D,
                GL_TEXTURE_MAG_FILTER,
                GL_NEAREST
            )

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0)

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle()
        }

        if (textureHandle[0] == 0) {
            throw RuntimeException("Error loading texture.")
        }

        return textureHandle[0]
    }
}