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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import java.io.IOException;

public class Cadastrar extends AppCompatActivity {

    String user;
    String senha;
    String email;
    String nome;
    String cpf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);
        this.setTitle("TCC Comedouro - Cadastro");

        TextView txtUsuario = (TextView) findViewById(R.id.txtUsuario);
        TextView txtSenha = (TextView) findViewById(R.id.txtSenha);
        TextView txtEmail = (TextView) findViewById(R.id.txtEmail);
        TextView txtNome = (TextView) findViewById(R.id.txtNome);
        EditText txtCPF = (EditText) findViewById(R.id.txtCPF);
        Button btVoltar = (Button) findViewById(R.id.btRecupera);
        Button btConfirmar = (Button) findViewById(R.id.btConfirmar);
        txtCPF.setTransformationMethod(null);

        txtNome.requestFocus();

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Cadastrar.this.finish();
            }
        });

       /* txtSenha.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() || actionId == EditorInfo.IME_ACTION_DONE) {

                    if (txtUsuario.length() > 0 && txtSenha.length() > 0 && txtNome.length() > 0 && txtCPF.length() > 13 && Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText()).matches()){

                        if(txtSenha.length() < 4){
                            txtSenha.setError("A senha deve conter pelo menos 4 caracteres");
                        }else{
                            //cadastro
                            user = txtUsuario.getText().toString();
                            senha = txtSenha.getText().toString();
                            email = txtEmail.getText().toString();
                            nome = txtNome.getText().toString();
                            cpf = txtCPF.getText().toString();
                            if(temRede()) {
                                new Cadastro().execute();
                                limpaCampos();
                            }else{
                                Toast.makeText(Cadastrar.this, "Sem conexão com a internet",
                                        Toast.LENGTH_LONG).show();
                            }



                        }

                    }else{
                        if (txtUsuario.length() < 1){
                            txtUsuario.setError("Campo obrigatório");
                        }
                        if (txtSenha.length() < 1){
                            txtSenha.setError("Campo obrigatório");
                        }
                        if (txtCPF.length() < 1){
                            txtCPF.setError("Campo obrigatório");
                        }
                        if (txtCPF.length() > 1 && txtCPF.length() < 14){
                            txtCPF.setError("Preencha o CPF corretamente");
                        }
                        if (txtNome.length() < 1){
                            txtNome.setError("Campo obrigatório");
                        }
                        if (txtEmail.length() < 1){
                            txtEmail.setError("Campo obrigatório");
                        }
                        if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText()).matches()){
                            txtEmail.setError("E-mail inválido");
                        }
                    }
                }

                return false;
            }
            private void limpaCampos() {
                txtUsuario.setText("");
                txtSenha.setText("");
                txtEmail.setText("");
                txtNome.setText("");
                txtCPF.setText("");
                txtNome.requestFocus();
            }
        });*/


        btConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtUsuario.length() > 0 && txtSenha.length() > 0 && txtNome.length() > 0 && txtCPF.length() > 13 && Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText()).matches()){

                    if(txtSenha.length() < 4){
                        txtSenha.setError("A senha deve conter pelo menos 4 caracteres");
                    }else{
                        //cadastro
                        user = txtUsuario.getText().toString();
                        senha = txtSenha.getText().toString();
                        email = txtEmail.getText().toString();
                        nome = txtNome.getText().toString();
                        cpf = txtCPF.getText().toString();
                        if(temRede()) {
                            new Cadastro().execute();
                            limpaCampos();
                        }else{
                            Toast.makeText(Cadastrar.this, "Sem conexão com a internet",
                                    Toast.LENGTH_LONG).show();
                        }



                    }

                }else{
                    if (txtUsuario.length() < 1){
                        txtUsuario.setError("Campo obrigatório");
                    }
                    if (txtSenha.length() < 1){
                        txtSenha.setError("Campo obrigatório");
                    }
                    if (txtCPF.length() < 1){
                        txtCPF.setError("Campo obrigatório");
                    }
                    if (txtCPF.length() > 1 && txtCPF.length() < 14){
                        txtCPF.setError("Preencha o CPF corretamente");
                    }
                    if (txtNome.length() < 1){
                        txtNome.setError("Campo obrigatório");
                    }
                    if (txtEmail.length() < 1){
                        txtEmail.setError("Campo obrigatório");
                    }
                    if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText()).matches()){
                        txtEmail.setError("E-mail inválido");
                    }
                }
            }

            private void limpaCampos() {
                txtUsuario.setText("");
                txtSenha.setText("");
                txtEmail.setText("");
                txtNome.setText("");
                txtCPF.setText("");
                txtNome.requestFocus();
            }
        });

        txtCPF.addTextChangedListener(Utils.mask(txtCPF, "###.###.###-##"));
    }



    @Override
    public void onBackPressed() {
        this.finish();
    }


     class Cadastro extends AsyncTask<Void, Void, Void> {

        String toastMSG = "";

         ProgressDialog pDialog;

         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             pDialog = new ProgressDialog(Cadastrar.this);
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
            Usuario usuario = new Usuario(nome, user, email, cpf, senha);
            UsuarioDAO dao = new UsuarioDAO();
            try {
               toastMSG = (dao.cadastra(usuario));
                if(toastMSG.equals("Cadastrado com sucesso!\n"))
                enviaEmailCadastro(usuario.getNome(), usuario.getCpf(), usuario.getEmail(), usuario.getLogin(), usuario.getSenha());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

         @Override
         protected void onPostExecute(Void aVoid) {
             super.onPostExecute(aVoid);
             pDialog.dismiss();
             if(toastMSG.equals("Cadastrado com sucesso!\n")){
                 AlertDialog dialogo;
                 dialogo = new AlertDialog.Builder(Cadastrar.this)
                         .setTitle("Sucesso")
                         .setMessage(toastMSG)
                         .setCancelable(true)
                           .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   // Whatever...
                               }
                           }).create();
                 dialogo.show();
             }else{
                 Toast.makeText(Cadastrar.this, toastMSG,
                         Toast.LENGTH_LONG).show();
             }

         }
     }

    private boolean temRede() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void enviaEmailCadastro(String nome, String cpf, String email, String usuario, String senha){
        try {
            BackgroundMail.newBuilder(Cadastrar.this)
                    .withUsername("tc1unip24@gmail.com")
                    .withPassword("@Vrauzeidms009")
                    .withMailto(email)
                    .withType(BackgroundMail.TYPE_PLAIN)
                    .withSubject("TCC Comedouro - Cadastrado com sucesso!")
                    .withBody(
                            "Olá, " +
                                    nome +
                                    "!\n\n" +
                                    "Seja muito bem-vindo(a)!\n\n" +
                                    "Seu cadastro na plataforma 'TCC Comedouro' foi efetuado com sucesso!\n\n" +
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
            Log.e("enviaEmailCadastro", e.getMessage(), e);
        }
    }
}