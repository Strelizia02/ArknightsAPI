package com.strelizia.arknights.util;

import java.io.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import io.github.mzdluo123.silk4j.AudioUtils;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;

public class MusicTranscodingUtil {


    public static String convertAudioFiles(String wavfilepath,String pcmfilepath){
        FileInputStream fileInputStream;
        FileOutputStream fileOutputStream;
        try {
            fileInputStream = new FileInputStream(wavfilepath);
            fileOutputStream = new FileOutputStream(pcmfilepath);
            byte[] wavbyte = InputStreamToByte(fileInputStream);
            byte[] pcmbyte = Arrays.copyOfRange(wavbyte, 44, wavbyte.length);
            fileOutputStream.write(pcmbyte);
            fileInputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return pcmfilepath;
    }
    /**
     * 输入流转byte二进制数据
     * @param fis
     * @return
     * @throws IOException
     */
    private static byte[] InputStreamToByte(FileInputStream fis) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        long size = fis.getChannel().size();
        byte[] buffer = null;
        if (size <= Integer.MAX_VALUE) {
            buffer = new byte[(int) size];
        } else {
            buffer = new byte[8];
            for (int ix = 0; ix < 8; ++ix) {
                int offset = 64 - (ix + 1) * 8;
                buffer[ix] = (byte) ((size >> offset) & 0xff);
            }
        }
        int len;
        while ((len = fis.read(buffer)) != -1) {
            byteStream.write(buffer, 0, len);
        }
        byte[] data = byteStream.toByteArray();
        byteStream.close();
        return data;
    }

    public static boolean wavToMp3(String inPath,String outFile){
        boolean status=false;
        File file=new File(inPath);
        try {
            execute(file,outFile);
            status=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    /**
     * 执行转化
     *
     * @param source
     *            输入文件
     * @param desFileName  目标文件名
     * @return  转换之后文件
     */
    public static File execute(File source, String desFileName)
            throws Exception {
        File target = new File(desFileName);
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(36000);
        audio.setChannels(2);
        audio.setSamplingRate(44100);
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp3");
        attrs.setAudioAttributes(audio);
        Encoder encoder = new Encoder();
        encoder.encode(source, target, attrs);
        return target;
    }


    public static void main(String[] args) throws IOException {
        File mp3 = new File("F:\\MyProject\\ArknightsAPI\\target\\MP3tosilk\\test.mp3");
        File newFile = new File("F:\\MyProject\\ArknightsAPI\\target\\MP3tosilk");
        AudioUtils.init(newFile);
        AudioUtils.mp3ToSilk(mp3);
//        wavToMp3("C:\\Users\\41245\\Downloads\\安洁莉娜_任命助理.wav", "C:\\Users\\41245\\Downloads\\安洁莉娜_任命助理.mp3");
    }
}