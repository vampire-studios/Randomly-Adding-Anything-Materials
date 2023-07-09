package io.github.vampirestudios.raa_materials.utils;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.resources.metadata.animation.AnimationFrame;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;

import java.util.Arrays;
import java.util.List;

public class BufferTexture {
	int width;
	int height;
	int[] buffer;
	AnimationMetadataSection animation;
	
	public BufferTexture(int width, int height) {
		this.width = width;
		this.height = height;
		buffer = new int[width * height];
		this.animation = AnimationMetadataSection.EMPTY;
	}

	public BufferTexture(NativeImage image, AnimationMetadataSection animation) {
		this.width = image.getWidth();
		this.height = image.getHeight();
		buffer = new int[width * height];
		for (int i = 0; i < buffer.length; i++) {
			int x = i % width;
			int y = i / width;
			buffer[i] = image.getPixelRGBA(x, y);
		}
		this.animation = animation;
	}

	private BufferTexture(BufferTexture texture) {
		this.width = texture.width;
		this.height = texture.height;
		buffer = Arrays.copyOf(texture.buffer, texture.buffer.length);
		List<AnimationFrame> copyani = Lists.newArrayList();
		texture.animation.forEachFrame((index, time) -> copyani.add(new AnimationFrame(index, time)));
		this.animation = new AnimationMetadataSection(copyani,
				texture.animation.calculateFrameSize(this.width, this.height).width(),
				texture.animation.calculateFrameSize(this.width, this.height).height(),
				texture.animation.getDefaultFrameTime(),
				texture.animation.isInterpolatedFrames());
	}

	public void setPixel(int x, int y, CustomColor color) {
		buffer[y * width + x] = color.getAsInt();
	}

	public int getPixel(int x, int y) {
		return buffer[y * width + x];
	}

	public NativeImage makeImage() {
		NativeImage img = TextureHelper.makeTexture(width, height);
		for (int i = 0; i < buffer.length; i++) {
			int x = i % width;
			int y = i / width;
			img.setPixelRGBA(x, y, buffer[i]);
		}
		return img;
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public void changeSize(int width, int height) {
		this.width = width;
		this.height = height;
		this.buffer = new int[width * height];
	}

	public void upscale(int scale) {
		changeSize(width * scale, height * scale);
	}

	public void downscale(int scale) {
		changeSize(width / scale, height / scale);
	}

	public AnimationMetadataSection getAnimation() {
		return animation;
	}

	public BufferTexture cloneTexture() {
		return new BufferTexture(this);
	}
}