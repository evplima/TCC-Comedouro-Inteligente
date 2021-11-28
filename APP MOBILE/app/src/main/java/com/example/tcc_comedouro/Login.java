package com.example.tcc_comedouro;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    String user;
    String senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setTitle("TCC Comedouro - Login");

        TextView txtUsuario = (TextView) findViewById(R.id.txtUsuario);
        TextView txtSenha = (TextView) findViewById(R.id.txtSenha);
        Button btEntrar = (Button) findViewById(R.id.btEntrar);
        Button btCadastrar = (Button) findViewById(R.id.btCadastrar);
        Button btEsqueceu = (Button) findViewById(R.id.btEsqueceu);


        /*txtSenha.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() || actionId == EditorInfo.IME_ACTION_DONE) {


                    if(txtUsuario.getText().length() < 1 || txtSenha.getText().length() < 1){

                        if(txtUsuario.getText().length() < 1)
                            txtUsuario.setError("Digite o usuário");

                        if(txtSenha.getText().length() < 1)
                            txtSenha.setError("Digite a senha");
                    }else{
                        if(temRede()){
                            //evento para logar
                            user = txtUsuario.getText().toString();
                            senha = txtSenha.getText().toString();
                            new Logar().execute();
                        }else{
                            Toast.makeText(Login.this, "Sem conexão com a internet",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }

                return false;
            }

        });*/


        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(txtUsuario.getText().length() < 1 || txtSenha.getText().length() < 1){

                    if(txtUsuario.getText().length() < 1)
                    txtUsuario.setError("Digite o usuário");

                    if(txtSenha.getText().length() < 1)
                        txtSenha.setError("Digite a senha");
                }else{
                    if(temRede()){
                        //evento para logar
                        user = txtUsuario.getText().toString();
                        senha = txtSenha.getText().toString();
                        new Logar().execute();
                    }else{
                        Toast.makeText(Login.this, "Sem conexão com a internet",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Cadastrar.class);
                Login.this.startActivity(intent);
                txtUsuario.setText("");
                txtSenha.setText("");
                txtUsuario.requestFocus();
            }
        });

        btEsqueceu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, EsqueciSenha.class);
                Login.this.startActivity(intent);
                txtUsuario.setText("");
                txtSenha.setText("");
                txtUsuario.requestFocus();
            }
        });


    }

    private boolean temRede() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    class Logar extends AsyncTask<Void, Void, Void> {


        boolean loginExiste = false;

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            SpannableString ss2=  new SpannableString("Autenticando");
            ss2.setSpan(new RelativeSizeSpan(2f), 0, ss2.length(), 0);
            ss2.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ss2.length(), 0);
            pDialog.setMessage(ss2);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {

            UsuarioDAO dao = new UsuarioDAO();
            loginExiste = dao.checkLogin(user, senha);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();
            if(loginExiste){
                Intent intent = new Intent(Login.this, Principal.class);
                Login.this.startActivity(intent);
                //quando login for bem sucedido, dar um finish() nessa atividade
                finish();
            }else{
                Toast.makeText(Login.this, "Usuário não encontrado!\nTente novamente",
                        Toast.LENGTH_LONG).show();
            }

        }
    }
}