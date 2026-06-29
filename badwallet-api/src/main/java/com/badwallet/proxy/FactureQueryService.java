package com.badwallet.proxy;

import com.badwallet.gateway.RemoteFacture;

import java.util.List;

public interface FactureQueryService {

    List<RemoteFacture> courantes(String walletCode, String unite);

    List<RemoteFacture> impayeesSurPeriode(String walletCode, String debut, String fin, String unite);
}
