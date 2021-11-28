package com.example.tcc_comedouro;


import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;


public class Utils {



    public static TextWatcher mask(final EditText ediTxt, final String mask) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            @Override
            public void afterTextChanged(final Editable s) {}

            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {}

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                final String str = Utils.unmask(s.toString());
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (final char m : mask.toCharArray()) {
                    if (m != '#' && str.length() > old.length()) {
                        mascara += m;
                        continue;
                    }
                    try {
                        mascara += str.charAt(i);
                    } catch (final Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                ediTxt.setText(mascara);
                ediTxt.setSelection(mascara.length());
            }
        };
    }

    public static String unmask(final String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[(]", "").replaceAll("[ ]","").replaceAll("[:]", "").replaceAll("[)]", "");
    }




        public static String criptografia(String x){
            StringBuffer sb = new StringBuffer(x); /*cria uma StringBuffer que
        recebe a String que é passada como argumento no método.
        */
            String a = sb.reverse().toString(); /*Cria uma string que armazena
        a string buffer revertida*/

            StringBuilder sc = new StringBuilder(a);/*cria uma stringbuilder que
        armazena a String revertida*/

            String p =  sc.insert(1, "GñopDS1G 978F 7CS7F-{54").toString();/*
        Adiciona os seguintes caracteres entre o primeiro e segundo caractere
        (no caso, a partir da posição 1, indo até a posição 24)*/
            p = sc.insert(0, "D(GS G9õmoga16*.98F {´´´798!?").toString();/*adiciona
        os seguintes caracteres antes da palavra (a partir da posição 0, indo
        até a posição 29)*/
            p = sc.insert(p.length() - 1, "´´sg  dg5d4àóîís g2dg9s7").toString();/*
        adiciona os seguintes caracteres entre o penúltimo e último*/
            p = sc.insert(p.length(), "kl sco pesaox").toString();/*adiciona os
        seguintes caracteres depois do último*/

            return p.toUpperCase(); //retorna a mensagem criptografada maiúscula
        }//fim do método criptografia


        public static String descriptografar(String i) {
            StringBuilder sl = new StringBuilder(i);/*Cria uma StringBuilder que
        recebe a mensagem criptografada*/

            sl.delete(0, 29);/*elimina os caracteres da posição 0 à 29, onde foi
        adicionado caractere*/
            sl.delete(1, 24);/*elimina os caracteres da posição 1 à 24, onde foi
        adicionado caractere*/
            sl.delete(sl.length() - 13, sl.length());/*elimina os caracteres apartir
        da posição, onde foi adicionado caractere*/
            sl.delete(sl.length() - 25, sl.length() - 1);/*elimina os caracteres
        localizados antes do último caractere até o caractere localizado na
        posição da subtração entre o tamanho do caractere subtraido à 25,
        onde foi adicionado caractere*/

            StringBuffer sf = new StringBuffer(sl.toString());/*Cria uma
        StringBuffer, que armazena a StringBuilder com a mensagem que foi
        passada para String*/

            String x = sf.reverse().toString();/*cria uma String que armazena a
        StringBuffer revertida, na qual foi passada para String*/

            return x;//retorna a mensagem descriptografada
        }//fim do método descriptografar



}
