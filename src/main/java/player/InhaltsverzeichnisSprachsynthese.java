package player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import javax.sound.sampled.AudioInputStream;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.util.data.audio.MaryAudioUtils;

/**
 * Synthetisiert das Inhaltsverzeichnis eines Artikels
 * 
 * @author marcel
 *
 */
public class InhaltsverzeichnisSprachsynthese {
	public static void inhaltsverzeichnisSprachsynthese(ArrayList<String> inhaltsverzeichnis, String pfad, String sprache)
			throws MaryConfigurationException, SynthesisException, IOException {
		MaryInterface marytts = new LocalMaryInterface();
		if (sprache.equals("Deutsch")) {
			marytts.setLocale(Locale.GERMAN);
			marytts.setVoice("bits3");
		} else {
			marytts.setLocale(Locale.US);
			marytts.setVoice("cmu-slt-hsmm");
		}
		File output = new File(pfad + "/inhaltsverzeichnis" + ".wav");
		String s1 = "";
		for (String s : inhaltsverzeichnis) {
			s1 += s + ". ";
		}
		if (s1.equals("")) {
			s1 = "Kein Inhaltsverzeichnis gefunden";
		}
		AudioInputStream audio = marytts.generateAudio(s1);
		MaryAudioUtils.writeWavFile(MaryAudioUtils.getSamplesAsDoubleArray(audio), output.getAbsolutePath(),
				audio.getFormat());
	}
}
