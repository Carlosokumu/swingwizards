package com.example.core.network.data.models





data class Trades(val trades: List<Trade>)
data class Trade(val profit: String,val type: String,val symbol: String)
