package com.spekuli.util;

import java.util.Comparator;

public class ClusterByValor implements Comparator<Cluster> {

	public int compare(Cluster sr1, Cluster sr2) {
		return sr1.media().compareTo(sr2.media());
	}

}
