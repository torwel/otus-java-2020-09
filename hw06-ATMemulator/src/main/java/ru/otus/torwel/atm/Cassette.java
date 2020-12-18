package ru.otus.torwel.atm;

public interface Cassette {
    CurrencyDenomination getCassetteDenomination();

    void setCassetteDenomination(CurrencyDenomination denomination);

    boolean placeBanknote(Banknote banknote);

    Banknote takeBanknote();

    int getBanknotesCount();

    int getBalance();

    boolean isEmpty();
}
