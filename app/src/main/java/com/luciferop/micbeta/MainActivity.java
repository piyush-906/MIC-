package com.luciferop.micbeta;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.LoudnessEnhancer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;

public class MainActivity extends Activity {
    private static final int PICK_AUDIO = 71;
    private MediaPlayer player;
    private Equalizer equalizer;
    private BassBoost bassBoost;
    private LoudnessEnhancer loudness;
    private TextView fileLabel, status;
    private int accent = 0xFFA78BFA;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(0xFF090B12);
        getWindow().setNavigationBarColor(0xFF090B12);
        buildUi();
    }

    private void buildUi() {
        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(0xFF090B12);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(20), dp(26), dp(20), dp(30));
        scroll.addView(root);

        TextView brand = new TextView(this);
        brand.setText("MIC-BETA  /  ANDROID\nLUCIFER OP AUDIO LAB");
        brand.setTextColor(0xFFFFFFFF); brand.setTextSize(25); brand.setTypeface(null, 1);
        root.addView(brand);
        TextView sub = new TextView(this);
        sub.setText("LOCAL AUDIO LIBRARY • SOUND CONTROL"); sub.setTextColor(0xFF9CA3AF); sub.setTextSize(11);
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(-1, -2); sp.bottomMargin = dp(20); root.addView(sub, sp);

        TextView warning = new TextView(this);
        warning.setText("IMPORTANT: Android does not normally allow a third-party app to replace another app's microphone input. These controls affect audio played inside MIC-BETA, not WhatsApp/DualSpace outgoing mic.");
        warning.setTextColor(0xFFFFD7A3); warning.setTextSize(13); warning.setPadding(dp(14),dp(12),dp(14),dp(12)); warning.setBackgroundColor(0xFF292014);
        root.addView(warning, bottom(18));

        section(root, "AUDIO LIBRARY");
        fileLabel = text("No audio selected", 0xFFD1D5DB, 14); root.addView(fileLabel, bottom(10));
        Button choose = button("＋  CHOOSE AUDIO FILE"); root.addView(choose, bottom(8));
        choose.setOnClickListener(v -> { Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT); i.setType("audio/*"); i.addCategory(Intent.CATEGORY_OPENABLE); startActivityForResult(i, PICK_AUDIO); });
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        Button play = button("▶ PLAY"); Button stop = button("■ STOP");
        row.addView(play, new LinearLayout.LayoutParams(0, dp(48), 1)); LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(0, dp(48), 1); rp.leftMargin=dp(8); row.addView(stop,rp); root.addView(row,bottom(8));
        play.setOnClickListener(v -> { if (player != null) { try { player.start(); status.setText("Playing local audio"); } catch (Exception e) { toast("Select an audio file first"); } } else toast("Select an audio file first"); });
        stop.setOnClickListener(v -> { if (player != null) { player.pause(); player.seekTo(0); status.setText("Playback stopped"); } });
        status = text("Choose an audio file to begin.", 0xFF9CA3AF, 12); root.addView(status,bottom(18));

        section(root, "QUICK PRESETS");
        LinearLayout presets = new LinearLayout(this); presets.setOrientation(LinearLayout.HORIZONTAL);
        Button royal = button("ROYAL CLEAR"); Button loud = button("LOUD MIC");
        presets.addView(royal,new LinearLayout.LayoutParams(0,dp(48),1)); LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,dp(48),1); lp.leftMargin=dp(8); presets.addView(loud,lp); root.addView(presets,bottom(18));
        royal.setOnClickListener(v -> { setBass(350); setLoudness(250); setEq(0.35f); status.setText("Royal Clear playback preset applied"); });
        loud.setOnClickListener(v -> { setBass(700); setLoudness(700); setEq(0.7f); status.setText("Loud Mic playback preset applied (local playback only)"); });

        section(root, "PLAYBACK SOUND CONTROLS");
        addSlider(root,"Playback loudness",0,100,35, p -> setLoudness(p*10));
        addSlider(root,"Bass",0,100,35, p -> setBass(p*10));
        addSlider(root,"Presence / clarity",0,100,50, p -> setEq(p/100f));
        TextView note = text("Effects are applied to this app's audio playback where the Android device supports them. WhatsApp microphone routing is not changed.",0xFF9CA3AF,12);
        root.addView(note,bottom(14));
        setContentView(scroll);
    }

    private interface Change { void apply(int value); }
    private void addSlider(LinearLayout root,String label,int min,int max,int value,Change change) {
        TextView t=text(label,0xFFE5E7EB,14); root.addView(t);
        SeekBar s=new SeekBar(this); s.setMax(max-min); s.setProgress(value-min); s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ public void onProgressChanged(SeekBar b,int p,boolean u){if(u)change.apply(p+min);} public void onStartTrackingTouch(SeekBar b){} public void onStopTrackingTouch(SeekBar b){} });
        root.addView(s,bottom(14));
    }
    private void section(LinearLayout root,String title){ TextView t=text(title,accent,12); t.setTypeface(null,1); root.addView(t,bottom(10)); }
    private TextView text(String value,int color,int size){ TextView t=new TextView(this); t.setText(value); t.setTextColor(color); t.setTextSize(size); return t; }
    private Button button(String value){ Button b=new Button(this); b.setText(value); b.setTextColor(0xFFFFFFFF); b.setAllCaps(false); b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF27233A)); return b; }
    private LinearLayout.LayoutParams bottom(int margin){ LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2); p.bottomMargin=dp(margin); return p; }
    private int dp(int v){ return (int)(v*getResources().getDisplayMetrics().density+0.5f); }
    private void toast(String s){ Toast.makeText(this,s,Toast.LENGTH_SHORT).show(); }

    @Override protected void onActivityResult(int requestCode,int resultCode,Intent data){ super.onActivityResult(requestCode,resultCode,data); if(requestCode==PICK_AUDIO && resultCode==RESULT_OK && data!=null){ Uri uri=data.getData(); try { getContentResolver().takePersistableUriPermission(uri,Intent.FLAG_GRANT_READ_URI_PERMISSION); } catch(Exception ignored){}; try { releasePlayer(); player=new MediaPlayer(); player.setDataSource(this,uri); player.setOnPreparedListener(mp->{fileLabel.setText("Audio ready • "+uri.getLastPathSegment()); status.setText("Ready to play"); initEffects(mp.getAudioSessionId());}); player.setOnErrorListener((mp,what,extra)->{status.setText("Could not play this audio format");return true;}); player.prepareAsync(); status.setText("Loading audio…"); } catch(IOException e){toast("Could not open audio file");} } }
    private void initEffects(int session){ try { if(equalizer!=null)equalizer.release(); equalizer=new Equalizer(0,session); equalizer.setEnabled(true); }catch(Exception ignored){} try{if(bassBoost!=null)bassBoost.release();bassBoost=new BassBoost(0,session);bassBoost.setEnabled(true);}catch(Exception ignored){} try{if(loudness!=null)loudness.release();loudness=new LoudnessEnhancer(session);loudness.setEnabled(true);}catch(Exception ignored){} }
    private void setBass(int strength){try{if(bassBoost!=null)bassBoost.setStrength((short)Math.max(0,Math.min(1000,strength)));}catch(Exception ignored){}}
    private void setLoudness(int gain){try{if(loudness!=null)loudness.setTargetGain(Math.max(0,Math.min(1000,gain)));}catch(Exception ignored){}}
    private void setEq(float amount){try{if(equalizer!=null){short[] bands=equalizer.getBandLevelRange(); for(short i=0;i<equalizer.getNumberOfBands();i++){short level=(short)(bands[0]+(bands[1]-bands[0])*Math.max(0,Math.min(1,amount)));equalizer.setBandLevel(i,level);}}}catch(Exception ignored){}}
    private void releasePlayer(){ if(player!=null){try{player.release();}catch(Exception ignored){} player=null;} }
    @Override protected void onDestroy(){releasePlayer();try{if(equalizer!=null)equalizer.release();if(bassBoost!=null)bassBoost.release();if(loudness!=null)loudness.release();}catch(Exception ignored){}super.onDestroy();}
          }
