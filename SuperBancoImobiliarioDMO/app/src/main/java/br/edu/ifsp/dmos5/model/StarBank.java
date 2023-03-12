package br.edu.ifsp.dmos5.model;

import android.widget.Toast;

import java.util.List;

import br.edu.ifsp.dmos5.R;

public class StarBank {

    private StarBank instance;
    private List<CreditCard> cartoes;

    private StarBank(){}

    public StarBank getInstance() {
        if(instance == null){
            instance = new StarBank();
        }
        return instance;
    }

    public void startCreditCards(){
        for(int i = 0; i < R.integer.MAX_JOGADORES; i++){
            cartoes.add(new CreditCard());
        }
    }

    public void roundCompleted(CreditCard card, double value){
        card.creditValue(value);
    }

    public boolean transfer(CreditCard payer, CreditCard receiver, double value){
        if(!pay(payer, value)){
            return false;
        }
        receive(receiver,value);
        return true;
    }

    public void receive(CreditCard card, double value){
        card.creditValue(value);
    }

    public boolean pay(CreditCard card, double value) {
        try {
            card.debitValue(value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public List<CreditCard> getCartoes(){
        return cartoes;
    }
}
