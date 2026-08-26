package online.kingdomkeys.kingdomkeys.sound;

import net.minecraft.client.sounds.AudioStream;
import net.minecraft.client.sounds.JOrbisAudioStream;
import net.minecraft.client.sounds.LoopingAudioStream;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * A track that opens with something it never plays again, then settles into a loop: the intro is read to its
 * end and the loop takes over from the next sample.
 *
 * This is one stream rather than two sounds played one after the other, which is what makes the handover
 * exact. Chaining two sounds would put the join on a tick boundary, and a tick is 50 ms, so it would land
 * either as a hole or as a stutter. Here the engine only ever sees a continuous run of samples and has no
 * idea a second file exists.
 *
 * The convention is by filename: a sound at sounds/music/<name>.ogg picks up
 * sounds/music/<name>_intro.ogg if that file is there, and behaves as it always did if it isn't.
 */
@OnlyIn(Dist.CLIENT)
public class IntroLoopAudioStream implements AudioStream {

	private static final String SUFFIX = "_intro.ogg";
	private static final String EXTENSION = ".ogg";

	/** The intro that would go with this sound file, whether or not anyone shipped it */
	@Nullable
	public static ResourceLocation introFor(ResourceLocation sound) {
		String path = sound.getPath();

		if (!path.endsWith(EXTENSION) || path.endsWith(SUFFIX)) {
			return null;
		}

		return sound.withPath(path.substring(0, path.length() - EXTENSION.length()) + SUFFIX);
	}

	@Nullable
	private AudioStream intro;
	private final AudioStream loop;

	public IntroLoopAudioStream(InputStream intro, InputStream loop) throws IOException {
		this.intro = new JOrbisAudioStream(intro);
		// The loop half is the vanilla wrapper, so the repeat behaves exactly like any other looping sound
		this.loop = new LoopingAudioStream(JOrbisAudioStream::new, loop);
	}

	@Override
	public AudioFormat getFormat() {
		return this.loop.getFormat();
	}

	@Override
	public ByteBuffer read(int size) throws IOException {
		if (this.intro != null) {
			ByteBuffer read = this.intro.read(size);

			// A short read near the end is fine, the samples stay contiguous. Only an empty one is the end.
			if (read.hasRemaining()) {
				return read;
			}

			this.intro.close();
			this.intro = null;
		}

		return this.loop.read(size);
	}

	@Override
	public void close() throws IOException {
		try {
			if (this.intro != null) {
				this.intro.close();
			}
		} finally {
			this.loop.close();
		}
	}
}
