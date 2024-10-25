package com.laru.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import com.laru.common.di.ApplicationScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton


interface NetworkConnectionManager {
    val isNetworkConnectedFlow: StateFlow<Boolean>
    val isNetworkConnected: Boolean

    fun startListeningNetworkState()
    fun stopListeningNetworkState()
}


@Singleton
class NetworkConnectionManagerImpl @Inject constructor(
    @ApplicationContext appContext: Context,
    @ApplicationScope coroutineScope: CoroutineScope,
): NetworkConnectionManager {

    private val connectivityManager: ConnectivityManager = appContext.getSystemService()!!
    private val networkCallback = NetworkCallback()
    private val _currentNetwork = MutableStateFlow(CurrentNetwork())

    override val isNetworkConnectedFlow: StateFlow<Boolean> =
        _currentNetwork
            .map { it.isConnected() }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = _currentNetwork.value.isConnected()
            )

    override val isNetworkConnected: Boolean
        get() = isNetworkConnectedFlow.value


    override fun startListeningNetworkState() {
        if (_currentNetwork.value.isListening)
            return

        _currentNetwork.update {
            CurrentNetwork().copy(isListening = true)
        }

        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    override fun stopListeningNetworkState() {
        if (!_currentNetwork.value.isListening)
            return

        _currentNetwork.update {
            it.copy(isListening = false)
        }

        connectivityManager.unregisterNetworkCallback(networkCallback)
    }


    private inner class NetworkCallback: ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _currentNetwork.update {
                it.copy(isAvailable = true)
            }
        }

        override fun onLost(network: Network) {
            _currentNetwork.update {
                it.copy(
                    isAvailable = false,
                    networkCapabilities = null
                )
            }
        }

        override fun onUnavailable() {
            _currentNetwork.update {
                it.copy(
                    isAvailable = false,
                    networkCapabilities = null
                )
            }
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            _currentNetwork.update {
                it.copy(networkCapabilities = networkCapabilities)
            }
        }

        override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
            _currentNetwork.update {
                it.copy(isBlocked = blocked)
            }
        }
    }


    private data class CurrentNetwork(
        val isListening: Boolean = false,
        val isAvailable: Boolean = false,
        val isBlocked: Boolean = false,
        val networkCapabilities: NetworkCapabilities? = null
    )

    private fun CurrentNetwork.isConnected(): Boolean =
        isListening && isAvailable && !isBlocked && networkCapabilities.isValid()

    private fun NetworkCapabilities?.isValid(): Boolean = when {
        this == null -> false
        hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
                (hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        hasTransport(NetworkCapabilities.TRANSPORT_VPN) ||
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) -> true
        else -> false
    }
}
