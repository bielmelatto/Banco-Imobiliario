package br.edu.ifsp.dmos5.model;

import br.edu.ifsp.dmos5.R;

public class CreditCard {

    private Integer id;
    private double balance;
    private static Integer LAST_CARD_ID = R.integer.INICIAL_ID;

    public CreditCard(){
        id = LAST_CARD_ID;
        LAST_CARD_ID = LAST_CARD_ID++;
        balance = R.fraction.SALDO_INICIAL;
    }

    public void creditValue(double value){
        balance += Math.abs(value);
    }

    public void debitValue(double value) throws Exception {
        if(value > balance){
            throw new Exception();
        }
        balance -= Math.abs(value);
    }

}
