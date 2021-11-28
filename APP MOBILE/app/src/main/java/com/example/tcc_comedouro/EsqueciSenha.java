package com.example.tcc_comedouro;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

public class EsqueciSenha extends AppCompatActivity {

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueci_senha);
        this.setTitle("TCC Comedouro - Recuperação de senha");

        TextView txtEmail = (TextView) findViewById(R.id.txtEmail);
        Button btRecupera = (Button) findViewById(R.id.btRecupera);


        txtEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() || actionId == EditorInfo.IME_ACTION_DONE) {
                    if(Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText()).matches()){
                        if(temRede()){
                            email = txtEmail.getText().toString();
                            new RecuperaSenha().execute();
                            txtEmail.setText("");
                            txtEmail.requestFocus();
                        }else{
                            Toast.makeText(EsqueciSenha.this, "Sem conexão com a internet",
                                    Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(EsqueciSenha.this, "Digite um e-mail válido",
                                Toast.LENGTH_LONG).show();
                        txtEmail.setText("");
                        txtEmail.requestFocus();
                    }
                }

                return false;
            }

        });

        btRecupera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText()).matches()){
                    if(temRede()){
                        email = txtEmail.getText().toString();
                        new RecuperaSenha().execute();
                        txtEmail.setText("");
                        txtEmail.requestFocus();
                    }else{
                        Toast.makeText(EsqueciSenha.this, "Sem conexão com a internet",
                                Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(EsqueciSenha.this, "Digite um e-mail válido",
                            Toast.LENGTH_LONG).show();
                    txtEmail.setText("");
                    txtEmail.requestFocus();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        this.finish();
    }

    private boolean temRede() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void enviaEmailRecuperacao(String nome, String cpf, String email, String usuario, String senha){
        try {
            BackgroundMail.newBuilder(EsqueciSenha.this)
                    .withUsername("tc1unip24@gmail.com")
                    .withPassword("@Vrauzeidms009")
                    .withMailto(email)
                    .withType(BackgroundMail.TYPE_PLAIN)
                    .withSubject("TCC Comedouro - E-mail de recuperação!")
                    .withBody(
                            "Olá, " +
                                    nome +
                                    "!\n\n" +
                                    "Conforme solicitado, este é o e-mail de recuperação das suas informações cadastradas!\n\n" +
                                    "Segue suas informações de cadastro:\n" +
                                    "Nome: " +
                                    nome +
                                    "\nCPF: " +
                                    cpf +
                                    "\nUsuário: " +
                                    usuario +
                                    "\nSenha: " +
                                    senha +
                            "\n\nAtenciosamente,\n\nEquipe TCC Comedouro")
                    .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                        @Override
                        public void onSuccess() {
                            //do some magic
                        }
                    })
                    .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                        @Override
                        public void onFail() {
                            //do some magic
                        }
                    }).withProcessVisibility(false)
                    .send();
        } catch (Exception e) {
            Log.e("enviaEmailRecuperacao", e.getMessage(), e);
        }
    }

    class RecuperaSenha extends AsyncTask<Void, Void, Void> {


        boolean emailExiste = false;

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EsqueciSenha.this);
            SpannableString ss2=  new SpannableString("Aguarde");
            ss2.setSpan(new RelativeSizeSpan(2f), 0, ss2.length(), 0);
            ss2.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ss2.length(), 0);
            pDialog.setMessage(ss2);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            Usuario usuario;
            UsuarioDAO dao = new UsuarioDAO();
            emailExiste = dao.checaEmailExiste(email);
            if(emailExiste){
                usuario = dao.busca(email);
                enviaEmailRecuperacao(usuario.getNome(), usuario.getCpf(), usuario.getEmail(), usuario.getLogin(), usuario.getSenha());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();
            if(emailExiste){
                AlertDialog dialogo;
                dialogo = new AlertDialog.Builder(EsqueciSenha.this)
                        .setTitle("Sucesso")
                        .setMessage("E-mail de recuperação enviado com sucesso!")
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Whatever...
                            }
                        }).create();
                dialogo.show();
            }else{
                Toast.makeText(EsqueciSenha.this, "Não foi encontrado cadastro com este e-mail",
                        Toast.LENGTH_LONG).show();
            }

        }
    }
}