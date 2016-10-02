/**
 * © Copyright IBM Corporation 2015
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package com.ibm.watson.developer_cloud.android.speech_to_text.v1.audio;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Thread;

import android.os.Environment;
import android.util.Log;
import android.media.AudioRecord;
import android.media.*;
import android.media.MediaRecorder.AudioSource;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author Daniel Bolanos dbolano@us.ibm.com
 * description: this thread captures audio from the phone's microphone, whenever the buffer
 *
 */
public class AudioCaptureThread extends Thread {

    private static final String TAG = "AudioCaptureThread";
    private boolean mStop = false;
    private boolean mStopped = false;
    private int mSamplingRate = -1;
    private static int[] mSampleRates = new int[] { 8000, 11025, 22050, 44100 };
    private IAudioConsumer mIAudioConsumer = null;

    // the thread receives high priority because it needs to do real time audio capture
    // THREAD_PRIORITY_URGENT_AUDIO = "Standard priority of the most important audio threads"
    public AudioCaptureThread(int iSamplingRate, IAudioConsumer IAudioConsumer) {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
        mSamplingRate = iSamplingRate;
        mIAudioConsumer = IAudioConsumer;
    }

    public AudioRecord findAudioRecord() {
        for (int rate : mSampleRates) {
            for (short audioFormat : new short[] { AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT }) {
                for (short channelConfig : new short[] { AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO }) {
                    try {
                        Log.d("error", "Attempting rate " + rate + "Hz, bits: " + audioFormat + ", channel: "
                                + channelConfig);
                        int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);

                        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                            // check if we can instantiate and have a success
                            AudioRecord recorder = new AudioRecord(AudioSource.MIC, rate, channelConfig, audioFormat, bufferSize);

                            if (recorder.getState() == AudioRecord.STATE_INITIALIZED){
//                                mSamplingRate = rate;
                                return recorder;
                            }
                        }
                    } catch (Exception e) {
                        Log.e("error", rate + "Exception, keep trying.",e);
                    }
                }
            }
        }
        return null;
    }

    // once the thread is started it runs nonstop until it is stopped from the outside
    @Override
    public void run() {
        AudioRecord recorder = null;

        try {
            int iN = Math.max(mSamplingRate/2,AudioRecord.getMinBufferSize(mSamplingRate,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT));
            short[] buffer = new short[iN]; // ASR latency depends on the length of this buffer, a short buffer is good for latency
            // because the ASR will process the speech sooner, however it will introduce some network overhead because each packet comes
            // with a fixed amount of protocol-data), also I have noticed that some servers cannot handle too many small packages

            // initialize the recorder (buffer size will be at least 1/4th of a second)
            recorder = new AudioRecord(AudioSource.MIC, mSamplingRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, iN);
            recorder.startRecording();

            // Create a directory GuessWho to store audio recordings
            File mediaStorage = new File(Environment.getExternalStorageDirectory(),"GuessWho/recording");
            if (!mediaStorage.exists()) {
                mediaStorage.mkdirs();
            }

            File rawFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/GuessWho/recording/" + System.currentTimeMillis()/1000 + ".pcm");
            File audioFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/GuessWho/recording/" + System.currentTimeMillis()/1000 + ".wav");
            FileOutputStream fileOutputStream = new FileOutputStream(rawFile, true);
            Log.d(TAG, "recording started!");
            while(!mStop) {

                int r = recorder.read(buffer,0,buffer.length);
                long v = 0;
                for (int i = 0; i < r; i++) {
                    v += buffer[i] * buffer[i];
                }
                double amplitude = v / (double) r;
                double volume = 0;
                if(amplitude > 0)
                    volume = 10 * Math.log10(amplitude);
                mIAudioConsumer.onAmplitude(amplitude, volume);

                // convert to an array of bytes and send it to the server
                ByteBuffer bufferBytes = ByteBuffer.allocate(r*2);
                bufferBytes.order(ByteOrder.LITTLE_ENDIAN);
                bufferBytes.asShortBuffer().put(buffer,0,r);
                byte[] bytes = bufferBytes.array();
                int length = bytes.length;

                fileOutputStream.write(bytes);

                mIAudioConsumer.consume(bytes);
            }
            fileOutputStream.flush();
            fileOutputStream.close();

            rawToWave(rawFile, audioFile);
            rawFile.delete();
        }
        catch(Throwable x) {
            Log.e(TAG, "Error reading voice audio", x);
        }
        // release resources
        finally {
            if (recorder != null) {
                recorder.stop();
                recorder.release();
            }
            mStopped = true;
            Log.d(TAG, "recording stopped!");
        }
    }

    // this function is intended to be called from outside the thread in order to stop the thread
    public void end() {
        mStop = true;
        // waiting loop, it waits until the thread actually finishes
        while(!mStopped) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    }

    private void rawToWave(final File rawFile, final File waveFile) throws IOException {

        byte[] rawData = new byte[(int) rawFile.length()];
        DataInputStream input = null;
        try {
            input = new DataInputStream(new FileInputStream(rawFile));
            input.read(rawData);
        } finally {
            if (input != null) {
                input.close();
            }
        }

        DataOutputStream output = null;
        try {
            output = new DataOutputStream(new FileOutputStream(waveFile));
            // WAVE header
            // see http://ccrma.stanford.edu/courses/422/projects/WaveFormat/
            writeString(output, "RIFF"); // chunk id
            writeInt(output, 36 + rawData.length); // chunk size
            writeString(output, "WAVE"); // format
            writeString(output, "fmt "); // subchunk 1 id
            writeInt(output, 16); // subchunk 1 size
            writeShort(output, (short) 1); // audio format (1 = PCM)
            writeShort(output, (short) 1); // number of channels
            writeInt(output, mSamplingRate); // sample rate
            writeInt(output, mSamplingRate * 2); // byte rate
            writeShort(output, (short) 2); // block align
            writeShort(output, (short) 16); // bits per sample
            writeString(output, "data"); // subchunk 2 id
            writeInt(output, rawData.length); // subchunk 2 size
            // Audio data (conversion big endian -> little endian)
            short[] shorts = new short[rawData.length / 2];
            ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
            ByteBuffer bytes = ByteBuffer.allocate(shorts.length * 2);
            for (short s : shorts) {
                bytes.putShort(s);
            }
            output.write(fullyReadFileToBytes(rawFile));
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    byte[] fullyReadFileToBytes(File f) throws IOException {
        int size = (int) f.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        FileInputStream fis= new FileInputStream(f);
        try {

            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        }  catch (IOException e){
            throw e;
        } finally {
            fis.close();
        }

        return bytes;
    }

    private void writeInt(final DataOutputStream output, final int value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
        output.write(value >> 16);
        output.write(value >> 24);
    }

    private void writeShort(final DataOutputStream output, final short value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
    }

    private void writeString(final DataOutputStream output, final String value) throws IOException {
        for (int i = 0; i < value.length(); i++) {
            output.write(value.charAt(i));
        }
    }
}