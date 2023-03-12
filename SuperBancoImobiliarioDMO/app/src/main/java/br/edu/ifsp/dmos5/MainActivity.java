package br.edu.ifsp.dmos5;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ifsp.dmos5.model.CreditCard;
import br.edu.ifsp.dmos5.model.StarBank;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnM;
    private Button btnK;
    private Button btnC;
    private Button btnI;
    private Button btnAdicionarJogador;
    private EditText txtValue;
    private TextView txtViewNumJogadores;
    private EditText txtNomeJogador;
    private ListView payerList;
    private ListView receiverList;

    private StarBank starBank;
    private Map<String, CreditCard> cartaoJogador;
    private Integer numeroJogadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnM.findViewById(R.id.button_M);
        btnM.setOnClickListener(this);

        btnK.findViewById(R.id.button_K);
        btnK.setOnClickListener(this);

        btnC.findViewById(R.id.button_C);
        btnC.setOnClickListener(this);

        btnI.findViewById(R.id.button_I);
        btnI.setOnClickListener(this);

        txtValue.findViewById(R.id.valor_monetario);
        payerList.findViewById(R.id.payer_list);
        receiverList.findViewById(R.id.receiver_list);
        txtViewNumJogadores.findViewById(R.id.numero_jogadores);
        txtNomeJogador.findViewById(R.id.nome_jogador);

        btnAdicionarJogador.findViewById(R.id.button_add_jogador);
        btnAdicionarJogador.setOnClickListener(this);

        cartaoJogador = new HashMap<>();
        numeroJogadores = 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_M:
                milhares();
                break;
            case R.id.button_K:
                centenas();
                break;
            case R.id.button_C:
                cancelaUltimaTransacao();
                break;
            case R.id.button_I:
                fimRodada();
                break;
            case R.id.button_add_jogador:
                adicionarJogador();
                break;
        }

    }

    private String getString(EditText edit){
        return edit.getText().toString();
    }

    private double getDouble(EditText edit){
        double value;
        try{
            value = Double.valueOf(getString(edit));
        }catch (NumberFormatException nfe){
            Toast.makeText(this, R.string.number_error_message, Toast.LENGTH_SHORT).show();
            value = 0;
        }
        return value;
    }

    private void adicionarJogador() {
        if(cartaoJogador.size()+1 >= R.integer.MAX_JOGADORES){
            Toast.makeText(this, R.string.maximo_atingido, Toast.LENGTH_SHORT).show();
        }

        String nome = getString(txtNomeJogador);
        cartaoJogador.put(nome, starBank.getInstance().getCartoes().get(cartaoJogador.size()+1));

        txtViewNumJogadores.setText(R.string.numero_de_jogadores+numeroJogadores);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getNomes());
        payerList.setAdapter(arrayAdapter);
        receiverList.setAdapter(arrayAdapter);
    }

    private void fimRodada() {
        CreditCard receiver = findReceiver();

        double value = getDouble(txtValue);

        payerReceiver(null, receiver, value);
    }

    private void cancelaUltimaTransacao() {
        CreditCard receiver = findReceiver();
        CreditCard payer = findPayer();

        double value = getDouble(txtValue);

        if(!payerReceiver(receiver, payer, value)){
            Toast.makeText(this, R.string.falha_cancelar_transacao, Toast.LENGTH_SHORT).show();
        }
    }

    private void centenas() {
        CreditCard receiver = findReceiver();
        CreditCard payer = findPayer();

        double value = getDouble(txtValue);

        if (value > 999){
            Toast.makeText(this, R.string.valor_menor_999, Toast.LENGTH_SHORT).show();
            return;
        }

        payerReceiver(payer, receiver, value);
    }

    private void milhares() {
        CreditCard receiver = findReceiver();
        CreditCard payer = findPayer();

        double value = getDouble(txtValue) * 1000;

        payerReceiver(payer, receiver, value);
    }

    private CreditCard findReceiver() {

        return null;
    }

    private CreditCard findPayer() {

        return null;
    }

    private boolean payerReceiver(CreditCard payer, CreditCard receiver, double value){
        if(payer == null && receiver == null){
            Toast.makeText(this, R.string.informar_jogador, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(payer != null && receiver != null){
            boolean transacao = starBank.getInstance().transfer(payer,receiver,value);
            if(!transacao){
                Toast.makeText(this, R.string.falha_transacao, Toast.LENGTH_SHORT).show();
            }
            return transacao;
        }

        if(payer != null){
            boolean pagou = starBank.getInstance().pay(payer,value);
            if(!pagou){
                Toast.makeText(this, R.string.SALDO_INSUFICIENTE, Toast.LENGTH_SHORT).show();
            }
            return pagou;
        }

        starBank.getInstance().receive(receiver,value);
        return true;
    }

    private List<String> getNomes(){
        List<String> nomes = new ArrayList<>();
        cartaoJogador.forEach((n,c) ->{
            nomes.add(n);
        });
        return nomes;
    }

}