/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL14
 *  org.lwjgl.util.vector.Quaternion
 */
package com.leave.old.Utils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.util.vector.Quaternion;

@SideOnly(value=Side.CLIENT)
public class GlStateManager {
    private static final FloatBuffer BUF_FLOAT_16 = BufferUtils.createFloatBuffer((int)16);
    private static final FloatBuffer BUF_FLOAT_4 = BufferUtils.createFloatBuffer((int)4);
    private static final AlphaState alphaState = new AlphaState();
    private static final BooleanState lightingState = new BooleanState(2896);
    private static final BooleanState[] lightState = new BooleanState[8];
    private static final ColorMaterialState colorMaterialState;
    private static final BlendState blendState;
    private static final DepthState depthState;
    private static final FogState fogState;
    private static final CullState cullState;
    private static final PolygonOffsetState polygonOffsetState;
    private static final ColorLogicState colorLogicState;
    private static final TexGenState texGenState;
    private static final ClearState clearState;
    private static final StencilState stencilState;
    private static final BooleanState normalizeState;
    private static int activeTextureUnit;
    private static final TextureState[] textureState;
    private static int activeShadeModel;
    private static final BooleanState rescaleNormalState;
    private static final ColorMask colorMaskState;
    private static final Color colorState;

    public static void pushAttrib() {
        GL11.glPushAttrib((int)8256);
    }

    public static void popAttrib() {
        GL11.glPopAttrib();
    }

    public static void disableAlpha() {
        GlStateManager.alphaState.alphaTest.setDisabled();
    }

    public static void enableAlpha() {
        GlStateManager.alphaState.alphaTest.setEnabled();
    }

    public static void alphaFunc(int func, float ref) {
        if (func != GlStateManager.alphaState.func || ref != GlStateManager.alphaState.ref) {
            GlStateManager.alphaState.func = func;
            GlStateManager.alphaState.ref = ref;
            GL11.glAlphaFunc((int)func, (float)ref);
        }
    }

    public static void enableLighting() {
        lightingState.setEnabled();
    }

    public static void disableLighting() {
        lightingState.setDisabled();
    }

    public static void enableLight(int light) {
        lightState[light].setEnabled();
    }

    public static void disableLight(int light) {
        lightState[light].setDisabled();
    }

    public static void enableColorMaterial() {
        GlStateManager.colorMaterialState.colorMaterial.setEnabled();
    }

    public static void disableColorMaterial() {
        GlStateManager.colorMaterialState.colorMaterial.setDisabled();
    }

    public static void colorMaterial(int face, int mode) {
        if (face != GlStateManager.colorMaterialState.face || mode != GlStateManager.colorMaterialState.mode) {
            GlStateManager.colorMaterialState.face = face;
            GlStateManager.colorMaterialState.mode = mode;
            GL11.glColorMaterial((int)face, (int)mode);
        }
    }

    public static void glLight(int light, int pname, FloatBuffer params) {
        GL11.glLight((int)light, (int)pname, (FloatBuffer)params);
    }

    public static void glLightModel(int pname, FloatBuffer params) {
        GL11.glLightModel((int)pname, (FloatBuffer)params);
    }

    public static void glNormal3f(float nx, float ny, float nz) {
        GL11.glNormal3f((float)nx, (float)ny, (float)nz);
    }

    public static void disableDepth() {
        GlStateManager.depthState.depthTest.setDisabled();
    }

    public static void enableDepth() {
        GlStateManager.depthState.depthTest.setEnabled();
    }

    public static void depthFunc(int depthFunc) {
        if (depthFunc != GlStateManager.depthState.depthFunc) {
            GlStateManager.depthState.depthFunc = depthFunc;
            GL11.glDepthFunc((int)depthFunc);
        }
    }

    public static void depthMask(boolean flagIn) {
        if (flagIn != GlStateManager.depthState.maskEnabled) {
            GlStateManager.depthState.maskEnabled = flagIn;
            GL11.glDepthMask((boolean)flagIn);
        }
    }

    public static void disableBlend() {
        GlStateManager.blendState.blend.setDisabled();
    }

    public static void enableBlend() {
        GlStateManager.blendState.blend.setEnabled();
    }

    public static void blendFunc(SourceFactor srcFactor, DestFactor dstFactor) {
        GlStateManager.blendFunc(srcFactor.factor, dstFactor.factor);
    }

    public static void blendFunc(int srcFactor, int dstFactor) {
        if (srcFactor != GlStateManager.blendState.srcFactor || dstFactor != GlStateManager.blendState.dstFactor) {
            GlStateManager.blendState.srcFactor = srcFactor;
            GlStateManager.blendState.dstFactor = dstFactor;
            GL11.glBlendFunc((int)srcFactor, (int)dstFactor);
        }
    }

    public static void tryBlendFuncSeparate(SourceFactor srcFactor, DestFactor dstFactor, SourceFactor srcFactorAlpha, DestFactor dstFactorAlpha) {
        GlStateManager.tryBlendFuncSeparate(srcFactor.factor, dstFactor.factor, srcFactorAlpha.factor, dstFactorAlpha.factor);
    }

    public static void tryBlendFuncSeparate(int srcFactor, int dstFactor, int srcFactorAlpha, int dstFactorAlpha) {
        if (srcFactor != GlStateManager.blendState.srcFactor || dstFactor != GlStateManager.blendState.dstFactor || srcFactorAlpha != GlStateManager.blendState.srcFactorAlpha || dstFactorAlpha != GlStateManager.blendState.dstFactorAlpha) {
            GlStateManager.blendState.srcFactor = srcFactor;
            GlStateManager.blendState.dstFactor = dstFactor;
            GlStateManager.blendState.srcFactorAlpha = srcFactorAlpha;
            GlStateManager.blendState.dstFactorAlpha = dstFactorAlpha;
            OpenGlHelper.glBlendFunc(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
        }
    }

    public static void glBlendEquation(int blendEquation) {
        GL14.glBlendEquation((int)blendEquation);
    }

    public static void enableOutlineMode(int color) {
        BUF_FLOAT_4.put(0, (float)(color >> 16 & 0xFF) / 255.0f);
        BUF_FLOAT_4.put(1, (float)(color >> 8 & 0xFF) / 255.0f);
        BUF_FLOAT_4.put(2, (float)(color >> 0 & 0xFF) / 255.0f);
        BUF_FLOAT_4.put(3, (float)(color >> 24 & 0xFF) / 255.0f);
        GlStateManager.glTexEnv(8960, 8705, BUF_FLOAT_4);
        GlStateManager.glTexEnvi(8960, 8704, 34160);
        GlStateManager.glTexEnvi(8960, 34161, 7681);
        GlStateManager.glTexEnvi(8960, 34176, 34166);
        GlStateManager.glTexEnvi(8960, 34192, 768);
        GlStateManager.glTexEnvi(8960, 34162, 7681);
        GlStateManager.glTexEnvi(8960, 34184, 5890);
        GlStateManager.glTexEnvi(8960, 34200, 770);
    }

    public static void disableOutlineMode() {
        GlStateManager.glTexEnvi(8960, 8704, 8448);
        GlStateManager.glTexEnvi(8960, 34161, 8448);
        GlStateManager.glTexEnvi(8960, 34162, 8448);
        GlStateManager.glTexEnvi(8960, 34176, 5890);
        GlStateManager.glTexEnvi(8960, 34184, 5890);
        GlStateManager.glTexEnvi(8960, 34192, 768);
        GlStateManager.glTexEnvi(8960, 34200, 770);
    }

    public static void enableFog() {
        GlStateManager.fogState.fog.setEnabled();
    }

    public static void disableFog() {
        GlStateManager.fogState.fog.setDisabled();
    }

    public static void setFog(FogMode fogMode) {
        GlStateManager.setFog(fogMode.capabilityId);
    }

    private static void setFog(int param) {
        if (param != GlStateManager.fogState.mode) {
            GlStateManager.fogState.mode = param;
            GL11.glFogi((int)2917, (int)param);
        }
    }

    public static void setFogDensity(float param) {
        if (param != GlStateManager.fogState.density) {
            GlStateManager.fogState.density = param;
            GL11.glFogf((int)2914, (float)param);
        }
    }

    public static void setFogStart(float param) {
        if (param != GlStateManager.fogState.start) {
            GlStateManager.fogState.start = param;
            GL11.glFogf((int)2915, (float)param);
        }
    }

    public static void setFogEnd(float param) {
        if (param != GlStateManager.fogState.end) {
            GlStateManager.fogState.end = param;
            GL11.glFogf((int)2916, (float)param);
        }
    }

    public static void glFog(int pname, FloatBuffer param) {
        GL11.glFog((int)pname, (FloatBuffer)param);
    }

    public static void glFogi(int pname, int param) {
        GL11.glFogi((int)pname, (int)param);
    }

    public static void enableCull() {
        GlStateManager.cullState.cullFace.setEnabled();
    }

    public static void disableCull() {
        GlStateManager.cullState.cullFace.setDisabled();
    }

    public static void cullFace(CullFace cullFace) {
        GlStateManager.cullFace(cullFace.mode);
    }

    private static void cullFace(int mode) {
        if (mode != GlStateManager.cullState.mode) {
            GlStateManager.cullState.mode = mode;
            GL11.glCullFace((int)mode);
        }
    }

    public static void glPolygonMode(int face, int mode) {
        GL11.glPolygonMode((int)face, (int)mode);
    }

    public static void enablePolygonOffset() {
        GlStateManager.polygonOffsetState.polygonOffsetFill.setEnabled();
    }

    public static void disablePolygonOffset() {
        GlStateManager.polygonOffsetState.polygonOffsetFill.setDisabled();
    }

    public static void doPolygonOffset(float factor, float units) {
        if (factor != GlStateManager.polygonOffsetState.factor || units != GlStateManager.polygonOffsetState.units) {
            GlStateManager.polygonOffsetState.factor = factor;
            GlStateManager.polygonOffsetState.units = units;
            GL11.glPolygonOffset((float)factor, (float)units);
        }
    }

    public static void enableColorLogic() {
        GlStateManager.colorLogicState.colorLogicOp.setEnabled();
    }

    public static void disableColorLogic() {
        GlStateManager.colorLogicState.colorLogicOp.setDisabled();
    }

    public static void colorLogicOp(LogicOp logicOperation) {
        GlStateManager.colorLogicOp(logicOperation.opcode);
    }

    public static void colorLogicOp(int opcode) {
        if (opcode != GlStateManager.colorLogicState.opcode) {
            GlStateManager.colorLogicState.opcode = opcode;
            GL11.glLogicOp((int)opcode);
        }
    }

    public static void enableTexGenCoord(TexGen texGen) {
        GlStateManager.texGenCoord((TexGen)texGen).textureGen.setEnabled();
    }

    public static void disableTexGenCoord(TexGen texGen) {
        GlStateManager.texGenCoord((TexGen)texGen).textureGen.setDisabled();
    }

    public static void texGen(TexGen texGen, int param) {
        TexGenCoord glstatemanager$texgencoord = GlStateManager.texGenCoord(texGen);
        if (param != glstatemanager$texgencoord.param) {
            glstatemanager$texgencoord.param = param;
            GL11.glTexGeni((int)glstatemanager$texgencoord.coord, (int)9472, (int)param);
        }
    }

    public static void texGen(TexGen texGen, int pname, FloatBuffer params) {
        GL11.glTexGen((int)GlStateManager.texGenCoord((TexGen)texGen).coord, (int)pname, (FloatBuffer)params);
    }

    private static TexGenCoord texGenCoord(TexGen texGen) {
        switch (texGen) {
            case S: {
                return GlStateManager.texGenState.s;
            }
            case T: {
                return GlStateManager.texGenState.t;
            }
            case R: {
                return GlStateManager.texGenState.r;
            }
            case Q: {
                return GlStateManager.texGenState.q;
            }
        }
        return GlStateManager.texGenState.s;
    }

    public static void setActiveTexture(int texture) {
        if (activeTextureUnit != texture - OpenGlHelper.defaultTexUnit) {
            activeTextureUnit = texture - OpenGlHelper.defaultTexUnit;
            OpenGlHelper.setActiveTexture(texture);
        }
    }

    public static void enableTexture2D() {
        GlStateManager.textureState[GlStateManager.activeTextureUnit].texture2DState.setEnabled();
    }

    public static void disableTexture2D() {
        GlStateManager.textureState[GlStateManager.activeTextureUnit].texture2DState.setDisabled();
    }

    public static void glTexEnv(int target, int parameterName, FloatBuffer parameters) {
        GL11.glTexEnv((int)target, (int)parameterName, (FloatBuffer)parameters);
    }

    public static void glTexEnvi(int target, int parameterName, int parameter) {
        GL11.glTexEnvi((int)target, (int)parameterName, (int)parameter);
    }

    public static void glTexEnvf(int target, int parameterName, float parameter) {
        GL11.glTexEnvf((int)target, (int)parameterName, (float)parameter);
    }

    public static void glTexParameterf(int target, int parameterName, float parameter) {
        GL11.glTexParameterf((int)target, (int)parameterName, (float)parameter);
    }

    public static void glTexParameteri(int target, int parameterName, int parameter) {
        GL11.glTexParameteri((int)target, (int)parameterName, (int)parameter);
    }

    public static int glGetTexLevelParameteri(int target, int level, int parameterName) {
        return GL11.glGetTexLevelParameteri((int)target, (int)level, (int)parameterName);
    }

    public static int generateTexture() {
        return GL11.glGenTextures();
    }

    public static void deleteTexture(int texture) {
        GL11.glDeleteTextures((int)texture);
        for (TextureState glstatemanager$texturestate : textureState) {
            if (glstatemanager$texturestate.textureName != texture) continue;
            glstatemanager$texturestate.textureName = -1;
        }
    }

    public static void bindTexture(int texture) {
        if (texture != GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName) {
            GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName = texture;
            GL11.glBindTexture((int)3553, (int)texture);
        }
    }

    public static void glTexImage2D(int target, int level, int internalFormat, int width, int height, int border, int format, int type, @Nullable IntBuffer pixels) {
        GL11.glTexImage2D((int)target, (int)level, (int)internalFormat, (int)width, (int)height, (int)border, (int)format, (int)type, (IntBuffer)pixels);
    }

    public static void glTexSubImage2D(int target, int level, int xOffset, int yOffset, int width, int height, int format, int type, IntBuffer pixels) {
        GL11.glTexSubImage2D((int)target, (int)level, (int)xOffset, (int)yOffset, (int)width, (int)height, (int)format, (int)type, (IntBuffer)pixels);
    }

    public static void glCopyTexSubImage2D(int target, int level, int xOffset, int yOffset, int x, int y, int width, int height) {
        GL11.glCopyTexSubImage2D((int)target, (int)level, (int)xOffset, (int)yOffset, (int)x, (int)y, (int)width, (int)height);
    }

    public static void glGetTexImage(int target, int level, int format, int type, IntBuffer pixels) {
        GL11.glGetTexImage((int)target, (int)level, (int)format, (int)type, (IntBuffer)pixels);
    }

    public static void enableNormalize() {
        normalizeState.setEnabled();
    }

    public static void disableNormalize() {
        normalizeState.setDisabled();
    }

    public static void shadeModel(int mode) {
        if (mode != activeShadeModel) {
            activeShadeModel = mode;
            GL11.glShadeModel((int)mode);
        }
    }

    public static void enableRescaleNormal() {
        rescaleNormalState.setEnabled();
    }

    public static void disableRescaleNormal() {
        rescaleNormalState.setDisabled();
    }

    public static void viewport(int x, int y, int width, int height) {
        GL11.glViewport((int)x, (int)y, (int)width, (int)height);
    }

    public static void colorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        if (red != GlStateManager.colorMaskState.red || green != GlStateManager.colorMaskState.green || blue != GlStateManager.colorMaskState.blue || alpha != GlStateManager.colorMaskState.alpha) {
            GlStateManager.colorMaskState.red = red;
            GlStateManager.colorMaskState.green = green;
            GlStateManager.colorMaskState.blue = blue;
            GlStateManager.colorMaskState.alpha = alpha;
            GL11.glColorMask((boolean)red, (boolean)green, (boolean)blue, (boolean)alpha);
        }
    }

    public static void clearDepth(double depth) {
        if (depth != GlStateManager.clearState.depth) {
            GlStateManager.clearState.depth = depth;
            GL11.glClearDepth((double)depth);
        }
    }

    public static void clearColor(float red, float green, float blue, float alpha) {
        if (red != GlStateManager.clearState.color.red || green != GlStateManager.clearState.color.green || blue != GlStateManager.clearState.color.blue || alpha != GlStateManager.clearState.color.alpha) {
            GlStateManager.clearState.color.red = red;
            GlStateManager.clearState.color.green = green;
            GlStateManager.clearState.color.blue = blue;
            GlStateManager.clearState.color.alpha = alpha;
            GL11.glClearColor((float)red, (float)green, (float)blue, (float)alpha);
        }
    }

    public static void clear(int mask) {
        GL11.glClear((int)mask);
    }

    public static void matrixMode(int mode) {
        GL11.glMatrixMode((int)mode);
    }

    public static void loadIdentity() {
        GL11.glLoadIdentity();
    }

    public static void pushMatrix() {
        GL11.glPushMatrix();
    }

    public static void popMatrix() {
        GL11.glPopMatrix();
    }

    public static void getFloat(int pname, FloatBuffer params) {
        GL11.glGetFloat((int)pname, (FloatBuffer)params);
    }

    public static void ortho(double left, double right, double bottom, double top, double zNear, double zFar) {
        GL11.glOrtho((double)left, (double)right, (double)bottom, (double)top, (double)zNear, (double)zFar);
    }

    public static void rotate(float angle, float x, float y, float z) {
        GL11.glRotatef((float)angle, (float)x, (float)y, (float)z);
    }

    public static void scale(float x, float y, float z) {
        GL11.glScalef((float)x, (float)y, (float)z);
    }

    public static void scale(double x, double y, double z) {
        GL11.glScaled((double)x, (double)y, (double)z);
    }

    public static void translate(float x, float y, float z) {
        GL11.glTranslatef((float)x, (float)y, (float)z);
    }

    public static void translate(double x, double y, double z) {
        GL11.glTranslated((double)x, (double)y, (double)z);
    }

    public static void multMatrix(FloatBuffer matrix) {
        GL11.glMultMatrix((FloatBuffer)matrix);
    }

    public static void rotate(Quaternion quaternionIn) {
        GlStateManager.multMatrix(GlStateManager.quatToGlMatrix(BUF_FLOAT_16, quaternionIn));
    }

    public static FloatBuffer quatToGlMatrix(FloatBuffer buffer, Quaternion quaternionIn) {
        buffer.clear();
        float f = quaternionIn.x * quaternionIn.x;
        float f1 = quaternionIn.x * quaternionIn.y;
        float f2 = quaternionIn.x * quaternionIn.z;
        float f3 = quaternionIn.x * quaternionIn.w;
        float f4 = quaternionIn.y * quaternionIn.y;
        float f5 = quaternionIn.y * quaternionIn.z;
        float f6 = quaternionIn.y * quaternionIn.w;
        float f7 = quaternionIn.z * quaternionIn.z;
        float f8 = quaternionIn.z * quaternionIn.w;
        buffer.put(1.0f - 2.0f * (f4 + f7));
        buffer.put(2.0f * (f1 + f8));
        buffer.put(2.0f * (f2 - f6));
        buffer.put(0.0f);
        buffer.put(2.0f * (f1 - f8));
        buffer.put(1.0f - 2.0f * (f + f7));
        buffer.put(2.0f * (f5 + f3));
        buffer.put(0.0f);
        buffer.put(2.0f * (f2 + f6));
        buffer.put(2.0f * (f5 - f3));
        buffer.put(1.0f - 2.0f * (f + f4));
        buffer.put(0.0f);
        buffer.put(0.0f);
        buffer.put(0.0f);
        buffer.put(0.0f);
        buffer.put(1.0f);
        buffer.rewind();
        return buffer;
    }

    public static void color(float colorRed, float colorGreen, float colorBlue, float colorAlpha) {
        if (colorRed != GlStateManager.colorState.red || colorGreen != GlStateManager.colorState.green || colorBlue != GlStateManager.colorState.blue || colorAlpha != GlStateManager.colorState.alpha) {
            GlStateManager.colorState.red = colorRed;
            GlStateManager.colorState.green = colorGreen;
            GlStateManager.colorState.blue = colorBlue;
            GlStateManager.colorState.alpha = colorAlpha;
            GL11.glColor4f((float)colorRed, (float)colorGreen, (float)colorBlue, (float)colorAlpha);
        }
    }

    public static void color(float colorRed, float colorGreen, float colorBlue) {
        GlStateManager.color(colorRed, colorGreen, colorBlue, 1.0f);
    }

    public static void glTexCoord2f(float sCoord, float tCoord) {
        GL11.glTexCoord2f((float)sCoord, (float)tCoord);
    }

    public static void glVertex3f(float x, float y, float z) {
        GL11.glVertex3f((float)x, (float)y, (float)z);
    }

    public static void resetColor() {
        GlStateManager.colorState.red = -1.0f;
        GlStateManager.colorState.green = -1.0f;
        GlStateManager.colorState.blue = -1.0f;
        GlStateManager.colorState.alpha = -1.0f;
    }

    public static void glNormalPointer(int type, int stride, ByteBuffer buffer) {
        GL11.glNormalPointer((int)type, (int)stride, (ByteBuffer)buffer);
    }

    public static void glTexCoordPointer(int size, int type, int stride, int buffer_offset) {
        GL11.glTexCoordPointer((int)size, (int)type, (int)stride, (long)buffer_offset);
    }

    public static void glTexCoordPointer(int size, int type, int stride, ByteBuffer buffer) {
        GL11.glTexCoordPointer((int)size, (int)type, (int)stride, (ByteBuffer)buffer);
    }

    public static void glVertexPointer(int size, int type, int stride, int buffer_offset) {
        GL11.glVertexPointer((int)size, (int)type, (int)stride, (long)buffer_offset);
    }

    public static void glVertexPointer(int size, int type, int stride, ByteBuffer buffer) {
        GL11.glVertexPointer((int)size, (int)type, (int)stride, (ByteBuffer)buffer);
    }

    public static void glColorPointer(int size, int type, int stride, int buffer_offset) {
        GL11.glColorPointer((int)size, (int)type, (int)stride, (long)buffer_offset);
    }

    public static void glColorPointer(int size, int type, int stride, ByteBuffer buffer) {
        GL11.glColorPointer((int)size, (int)type, (int)stride, (ByteBuffer)buffer);
    }

    public static void glDisableClientState(int cap) {
        GL11.glDisableClientState((int)cap);
    }

    public static void glEnableClientState(int cap) {
        GL11.glEnableClientState((int)cap);
    }

    public static void glBegin(int mode) {
        GL11.glBegin((int)mode);
    }

    public static void glEnd() {
        GL11.glEnd();
    }

    public static void glDrawArrays(int mode, int first, int count) {
        GL11.glDrawArrays((int)mode, (int)first, (int)count);
    }

    public static void glLineWidth(float width) {
        GL11.glLineWidth((float)width);
    }

    public static void callList(int list) {
        GL11.glCallList((int)list);
    }

    public static void glDeleteLists(int list, int range) {
        GL11.glDeleteLists((int)list, (int)range);
    }

    public static void glNewList(int list, int mode) {
        GL11.glNewList((int)list, (int)mode);
    }

    public static void glEndList() {
        GL11.glEndList();
    }

    public static int glGenLists(int range) {
        return GL11.glGenLists((int)range);
    }

    public static void glPixelStorei(int parameterName, int param) {
        GL11.glPixelStorei((int)parameterName, (int)param);
    }

    public static void glReadPixels(int x, int y, int width, int height, int format, int type, IntBuffer pixels) {
        GL11.glReadPixels((int)x, (int)y, (int)width, (int)height, (int)format, (int)type, (IntBuffer)pixels);
    }

    public static int glGetError() {
        return GL11.glGetError();
    }

    public static String glGetString(int name) {
        return GL11.glGetString((int)name);
    }

    public static void glGetInteger(int parameterName, IntBuffer parameters) {
        GL11.glGetInteger((int)parameterName, (IntBuffer)parameters);
    }

    public static int glGetInteger(int parameterName) {
        return GL11.glGetInteger((int)parameterName);
    }

    static {
        for (int i = 0; i < 8; ++i) {
            GlStateManager.lightState[i] = new BooleanState(16384 + i);
        }
        colorMaterialState = new ColorMaterialState();
        blendState = new BlendState();
        depthState = new DepthState();
        fogState = new FogState();
        cullState = new CullState();
        polygonOffsetState = new PolygonOffsetState();
        colorLogicState = new ColorLogicState();
        texGenState = new TexGenState();
        clearState = new ClearState();
        stencilState = new StencilState();
        normalizeState = new BooleanState(2977);
        textureState = new TextureState[8];
        for (int j = 0; j < 8; ++j) {
            GlStateManager.textureState[j] = new TextureState();
        }
        activeShadeModel = 7425;
        rescaleNormalState = new BooleanState(32826);
        colorMaskState = new ColorMask();
        colorState = new Color();
    }

    @SideOnly(value=Side.CLIENT)
    static class TextureState {
        public BooleanState texture2DState = new BooleanState(3553);
        public int textureName;

        private TextureState() {
        }
    }

    @SideOnly(value=Side.CLIENT)
    static class TexGenState {
        public TexGenCoord s = new TexGenCoord(8192, 3168);
        public TexGenCoord t = new TexGenCoord(8193, 3169);
        public TexGenCoord r = new TexGenCoord(8194, 3170);
        public TexGenCoord q = new TexGenCoord(8195, 3171);

        private TexGenState() {
        }
    }

    @SideOnly(value=Side.CLIENT)
    static class TexGenCoord {
        public BooleanState textureGen;
        public int coord;
        public int param = -1;

        public TexGenCoord(int coordIn, int capabilityIn) {
            this.coord = coordIn;
            this.textureGen = new BooleanState(capabilityIn);
        }
    }

    @SideOnly(value=Side.CLIENT)
    public static enum TexGen {
        S,
        T,
        R,
        Q;

    }

    @SideOnly(value=Side.CLIENT)
    static class StencilState {
        public StencilFunc func = new StencilFunc();
        public int mask = -1;
        public int fail = 7680;
        public int zfail = 7680;
        public int zpass = 7680;

        private StencilState() {
        }
    }

    @SideOnly(value=Side.CLIENT)
    static class StencilFunc {
        public int func = 519;
        public int mask = -1;

        private StencilFunc() {
        }
    }

    @SideOnly(value=Side.CLIENT)
    public static enum SourceFactor {
        CONSTANT_ALPHA(32771),
        CONSTANT_COLOR(32769),
        DST_ALPHA(772),
        DST_COLOR(774),
        ONE(1),
        ONE_MINUS_CONSTANT_ALPHA(32772),
        ONE_MINUS_CONSTANT_COLOR(32770),
        ONE_MINUS_DST_ALPHA(773),
        ONE_MINUS_DST_COLOR(775),
        ONE_MINUS_SRC_ALPHA(771),
        ONE_MINUS_SRC_COLOR(769),
        SRC_ALPHA(770),
        SRC_ALPHA_SATURATE(776),
        SRC_COLOR(768),
        ZERO(0);

        public final int factor;

        private SourceFactor(int factorIn) {
            this.factor = factorIn;
        }
    }

    @SideOnly(value=Side.CLIENT)
    static class PolygonOffsetState {
        public BooleanState polygonOffsetFill = new BooleanState(32823);
        public BooleanState polygonOffsetLine = new BooleanState(10754);
        public float factor;
        public float units;

        private PolygonOffsetState() {
        }
    }

    @SideOnly(value=Side.CLIENT)
    public static enum LogicOp {
        AND(5377),
        AND_INVERTED(5380),
        AND_REVERSE(5378),
        CLEAR(5376),
        COPY(5379),
        COPY_INVERTED(5388),
        EQUIV(5385),
        INVERT(5386),
        NAND(5390),
        NOOP(5381),
        NOR(5384),
        OR(5383),
        OR_INVERTED(5389),
        OR_REVERSE(5387),
        SET(5391),
        XOR(5382);

        public final int opcode;

        private LogicOp(int opcodeIn) {
            this.opcode = opcodeIn;
        }
    }

    @SideOnly(value=Side.CLIENT)
    static class FogState {
        public BooleanState fog = new BooleanState(2912);
        public int mode = 2048;
        public float density = 1.0f;
        public float start;
        public float end = 1.0f;

        private FogState() {
        }
    }

    @SideOnly(value=Side.CLIENT)
    public static enum FogMode {
        LINEAR(9729),
        EXP(2048),
        EXP2(2049);

        public final int capabilityId;

        private FogMode(int capabilityIn) {
            this.capabilityId = capabilityIn;
        }
    }

    @SideOnly(value=Side.CLIENT)
    public static enum DestFactor {
        CONSTANT_ALPHA(32771),
        CONSTANT_COLOR(32769),
        DST_ALPHA(772),
        DST_COLOR(774),
        ONE(1),
        ONE_MINUS_CONSTANT_ALPHA(32772),
        ONE_MINUS_CONSTANT_COLOR(32770),
        ONE_MINUS_DST_ALPHA(773),
        ONE_MINUS_DST_COLOR(775),
        ONE_MINUS_SRC_ALPHA(771),
        ONE_MINUS_SRC_COLOR(769),
        SRC_ALPHA(770),
        SRC_COLOR(768),
        ZERO(0);

        public final int factor;

        private DestFactor(int factorIn) {
            this.factor = factorIn;
        }
    }

    @SideOnly(value=Side.CLIENT)
    static class DepthState {
        public BooleanState depthTest = new BooleanState(2929);
        public boolean maskEnabled = true;
        public int depthFunc = 513;

        private DepthState() {
        }
    }

    @SideOnly(value=Side.CLIENT)
    static class CullState {
        public BooleanState cullFace = new BooleanState(2884);
        public int mode = 1029;

        private CullState() {
        }
    }

    @SideOnly(value=Side.CLIENT)
    public static enum CullFace {
        FRONT(1028),
        BACK(1029),
        FRONT_AND_BACK(1032);

        public final int mode;

        private CullFace(int modeIn) {
            this.mode = modeIn;
        }
    }

    @SideOnly(value=Side.CLIENT)
    static class ColorMaterialState {
        public BooleanState colorMaterial = new BooleanState(2903);
        public int face = 1032;
        public int mode = 5634;

        private ColorMaterialState() {
        }
    }

    @SideOnly(value=Side.CLIENT)
    static class ColorMask {
        public boolean red = true;
        public boolean green = true;
        public boolean blue = true;
        public boolean alpha = true;

        private ColorMask() {
        }
    }

    @SideOnly(value=Side.CLIENT)
    static class ColorLogicState {
        public BooleanState colorLogicOp = new BooleanState(3058);
        public int opcode = 5379;

        private ColorLogicState() {
        }
    }

    @SideOnly(value=Side.CLIENT)
    static class Color {
        public float red = 1.0f;
        public float green = 1.0f;
        public float blue = 1.0f;
        public float alpha = 1.0f;

        public Color() {
            this(1.0f, 1.0f, 1.0f, 1.0f);
        }

        public Color(float redIn, float greenIn, float blueIn, float alphaIn) {
            this.red = redIn;
            this.green = greenIn;
            this.blue = blueIn;
            this.alpha = alphaIn;
        }
    }

    @SideOnly(value=Side.CLIENT)
    static class ClearState {
        public double depth = 1.0;
        public Color color = new Color(0.0f, 0.0f, 0.0f, 0.0f);

        private ClearState() {
        }
    }

    @SideOnly(value=Side.CLIENT)
    static class BooleanState {
        private final int capability;
        private boolean currentState;

        public BooleanState(int capabilityIn) {
            this.capability = capabilityIn;
        }

        public void setDisabled() {
            this.setState(false);
        }

        public void setEnabled() {
            this.setState(true);
        }

        public void setState(boolean state) {
            if (state != this.currentState) {
                this.currentState = state;
                if (state) {
                    GL11.glEnable((int)this.capability);
                } else {
                    GL11.glDisable((int)this.capability);
                }
            }
        }
    }

    @SideOnly(value=Side.CLIENT)
    static class BlendState {
        public BooleanState blend = new BooleanState(3042);
        public int srcFactor = 1;
        public int dstFactor = 0;
        public int srcFactorAlpha = 1;
        public int dstFactorAlpha = 0;

        private BlendState() {
        }
    }

    @SideOnly(value=Side.CLIENT)
    static class AlphaState {
        public BooleanState alphaTest = new BooleanState(3008);
        public int func = 519;
        public float ref = -1.0f;

        private AlphaState() {
        }
    }
}

