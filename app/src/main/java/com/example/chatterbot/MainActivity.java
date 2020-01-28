package com.example.chatterbot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.chatterbot.apibot.ChatterBot;
import com.example.chatterbot.apibot.ChatterBotFactory;
import com.example.chatterbot.apibot.ChatterBotSession;
import com.example.chatterbot.apibot.ChatterBotType;
import com.example.chatterbot.model.data.ChatSentence;
import com.example.chatterbot.model.data.DetectedLanguage;
import com.example.chatterbot.model.data.Message;
import com.example.chatterbot.model.data.Translation;
import com.example.chatterbot.model.data.TranslationResponse;
import com.example.chatterbot.view.adapter.BackupAdapter;
import com.example.chatterbot.view.model.MainViewModel;
import com.example.chatterbot.view.adapter.MessagesAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "xzy";
    private static final Object ARGS = "b0dafd24ee35a477";
    private static final String FROM_LANGUAGE = "auto-detect";
    private MainViewModel mainViewModel;
    private RecyclerView recyclerView;
    private MessagesAdapter messagesAdapter;
    private FirebaseDatabase database;
    private ProgressBar pbLoading;
    private ConstraintLayout clActivity;
    private TextToSpeech tts;
    private static final String ORIGINAL_LANGUAGE = "en";
    private EditText etInput;
    private FloatingActionButton btSend;
    private Toolbar tb;
    private static final int STT_CODE = 1000;
    private static final int FIRST = 0;

    ChatSentence chatSentenceUser = new ChatSentence();
    ChatSentence chatSentenceBot = new ChatSentence();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLogin();
        Log.v(TAG,  "asdsadsad");
        initComponents();
        assignEvents();

    }

    /*  INITS   */

    private void initComponents() {
        //CHAT
        etInput = findViewById(R.id.etText);
        btSend = findViewById(R.id.btSend);
        tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        recyclerView = findViewById(R.id.recyclerView);
        messagesAdapter = mainViewModel.getMessagesAdapter();
        recyclerView.setAdapter(messagesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.smoothScrollToPosition(messagesAdapter.getItemCount());

        pbLoading = findViewById(R.id.pbLoading);
        clActivity = findViewById(R.id.clActivity);

        //FIREBASE
        database = mainViewModel.getDatabase();
        chatSentenceUser.setTalker("usuario");
        chatSentenceBot.setTalker("bot");

        if(!mainViewModel.isStarted()) {
            Intent i= getIntent();

           // Log.v(TAG,  ""+ i.getStringExtra("hola"));
            mainViewModel.setUser((FirebaseUser) i.getExtras().get("user"));
            mainViewModel.setReference(mainViewModel.getDatabase().getReference("user/" + mainViewModel.getUser().getUid()));
            mainViewModel.setStarted(true);
            setActivityLoading(true);
        }


        //Message DB Event
        mainViewModel.getReference().addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(mainViewModel.isLoading())//PRIMERA PANTALLA DE CARGA
                {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
                    for(DataSnapshot dateKey : dataSnapshot.getChildren())
                    {
                        Date date = null;
                        try
                        {
                            date = simpleDateFormat.parse(dateKey.getKey());
                            //mainViewModel.setCurrentDate(date);
                           // messagesAdapter.addMessage(new Message(date));//HORA

                            for(DataSnapshot messageKey : dateKey.getChildren())
                            {
                                ChatSentence chatSentence = messageKey.getValue(ChatSentence.class);
                                //Log.v(TAG, "sadasd:   "+chatSentence.toString());

                                messagesAdapter.addMessage(toMessage(chatSentence));
                            }
                        }
                        catch(ParseException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    setActivityLoading(false);
                    recyclerView.smoothScrollToPosition(messagesAdapter.getItemCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(MainActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                Log.v("ASD", "User: " + mainViewModel.getUser().getEmail());
            }
        });
    }

    private Message toMessage(ChatSentence chatSentence) {

        Message message;

        if (chatSentence.getTalker().equals("bot")){
            String hora = chatSentence.getTime().substring(3, chatSentence.getTime().length());
            message = new Message(false, chatSentence.getSentenceEn(), hora);

        } else {
            String hora = chatSentence.getTime().substring(3, chatSentence.getTime().length());
            message = new Message(true, chatSentence.getSentenceEs(), hora);
        }

        return message;
    }

    private void initLogin() {

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword("ejemplo3@gmail.com", "usuario").
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Log.v(TAG, user.getEmail());

                        if (task.isSuccessful()) {
                            Log.v(TAG, "task succesfull");
                            //initComponents();
                            //initUid();
                        } else {
                            Log.v(TAG, task.getException().toString());
                        }

                    }
                });
    }




    private MainActivity assignEvents() {
        mainViewModel.getTranslationResponses().observe(this, new Observer<List<TranslationResponse>>() {
            @Override
            public void onChanged(List<TranslationResponse> translationResponses) {



                if (!translationResponses.isEmpty()) {
                    DetectedLanguage detectedLanguage = translationResponses.get(FIRST).getDetectedLanguage();
                    Translation translation = translationResponses.get(FIRST).getTranslations().get(FIRST);
                    String text = translation.getText();
                    String code = detectedLanguage.getLanguage();
                    if (mainViewModel.isWaitingBotTranslation()) {//tiene la respuesta
                        Log.v(TAG, "TRADUCCION: "+text+"  codigo: "+code);
                        chatSentenceBot.setSentenceEs(text);
                        chatSentenceBot.setTime(mainViewModel.getLongTime());

                        uploadMessage(chatSentenceBot);

                        addMessage(false, text);
                        mainViewModel.setWaitingResponse(false);
                        mainViewModel.setWaitingBotTranslation(false);
                    } else {
                        Log.v(TAG, "ELSE: "+text+"  codigo: "+code);
                        chatSentenceUser.setSentenceEn(text);
                        chatSentenceUser.setTime(mainViewModel.getLongTime());

                        uploadMessage(chatSentenceUser);

                        mainViewModel.setTranslateCountryCode(code);
                        tts.setLanguage(new Locale(code));
                        new DoTheJob().execute(text);//chat con el bot
                    }
                } else {

                    addMessage(false, getString(R.string.error));
                    mainViewModel.setWaitingResponse(false);
                    mainViewModel.setWaitingBotTranslation(false);
                }
            }
        });
        btSend.setOnClickListener(new View.OnClickListener() {//datos mensaje usuario
            @Override
            public void onClick(View v) {
                String text = etInput.getText().toString();
                if (!mainViewModel.isWaitingResponse() && !text.isEmpty()) {
                    addMessage(true, text);
                    etInput.setText("");
                    mainViewModel.setWaitingResponse(true);
                    mainViewModel.translate(FROM_LANGUAGE, text, ORIGINAL_LANGUAGE);//original languaje: en
                    chatSentenceUser.setSentenceEs(text);

                }
            }
        });
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

            }
        });
        btSend.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            if (intent.resolveActivity(getPackageManager()) != null)
                startActivityForResult(intent, STT_CODE);
            else
                Toast.makeText(MainActivity.this, R.string.toastNoStt, Toast.LENGTH_SHORT).show();
            return false;
        }
    });
        return this;
}

    private void uploadMessage(ChatSentence chatSentence) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String dateString = format.format( new Date());

        //insercion BD
        ChatSentence item = chatSentence;
        Log.v(TAG, "CHATSENTENCE: "+ chatSentence.toString());
        Map<String, Object> map = new HashMap<>();
        String key = mainViewModel.getReference().child("20200114").push().getKey();
        map.put(dateString+"/" + key, item.toMap());
        mainViewModel.getReference().updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.v(TAG, "task succesfull");
                } else {
                    Log.v(TAG, task.getException().toString());
                }
            }
        });
    }

    private void addMessage(boolean outcoming, String text) {

        Message mensaje = new Message(outcoming, text, mainViewModel.getShortTime());
        Log.v(TAG, "outcoming"+outcoming+"     text: "+text);
        messagesAdapter.addMessage(mensaje);
        recyclerView.smoothScrollToPosition(messagesAdapter.getItemCount());

        if (!outcoming && mainViewModel.isTts())
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    private class DoTheJob extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return chat(strings[0]);
        }

        @Override
        protected void onPostExecute(String string) {
            mainViewModel.translate(ORIGINAL_LANGUAGE, string, mainViewModel.getTranslateCountryCode());
            mainViewModel.setWaitingBotTranslation(true);
            chatSentenceBot.setSentenceEn(string);
        }
    }

    private String chat(String message) {
        String response;
        try {
            ChatterBotFactory factory = new ChatterBotFactory();
            ChatterBot bot = factory.create(ChatterBotType.PANDORABOTS, ARGS);
            ChatterBotSession botSession = bot.createSession();
            response = botSession.think(message);
        } catch (Exception e) {
            response = getString(R.string.error);
        }
        return response;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    private void setActivityLoading(boolean toggle)
    {
        mainViewModel.setLoading(toggle);
        if(mainViewModel.isLoading())
        {
            pbLoading.setVisibility(View.VISIBLE);
            clActivity.setVisibility(View.INVISIBLE);
        }
        else
        {
            pbLoading.setVisibility(View.INVISIBLE);
            clActivity.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == STT_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String result = "";
            for (int i = 0; i < results.get(FIRST).length(); i++) {
                if (i == FIRST)
                    result += Character.toUpperCase(results.get(FIRST).charAt(i));
                else
                    result += results.get(FIRST).charAt(i);
            }
            addMessage(true, result);
            etInput.setText("");
            mainViewModel.setWaitingResponse(true);
            mainViewModel.translate(FROM_LANGUAGE, result, ORIGINAL_LANGUAGE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.recuperarConversaciones) {
            SharedPreferences sharedPreferences = getSharedPreferences("recordar", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("recordar");
            editor.remove("email");
            editor.remove("password");
            editor.apply();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
